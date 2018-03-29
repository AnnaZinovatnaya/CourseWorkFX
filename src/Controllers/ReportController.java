package Controllers;

import Models.MeltForView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import util.ErrorMessage;
import util.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;


public class ReportController
{
    @FXML private DatePicker            startDate;
    @FXML private DatePicker            endDate;
    @FXML private TableView             reportTable = new TableView<>();
    @FXML private TableColumn           brandColumn = new TableColumn<>();
    @FXML private TableColumn           amountColumn = new TableColumn<>();
    @FXML private TableColumn           dateColumn = new TableColumn<>();
    @FXML private TableColumn           lastnameColumn = new TableColumn<>();
    private ObservableList<MeltForView> melts;

    private MenuController menuController;

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
        this.reportTable.setPlaceholder(new Label("Выберите необходимый период и нажмите кнопу 'Выбрать'"));
    }

    @FXML private void menuButtonClicked()
    {
        menuController.backToMenu();
    }

    @FXML private void selectButtonClicked()
    {
        initReportTable();

        if (this.startDate.getValue() == null && this.endDate.getValue() == null)
        {
            showAllMelts();
            return;
        }

        if(this.endDate.getValue() == null)
        {
            showMeltsFrom();
            return;
        }

        if (this.startDate.getValue() == null)
        {
            showMeltsTill();
            return;
        }

        if (this.startDate.getValue().isAfter(this.endDate.getValue()))
        {
            Helper.showErrorMessage(ErrorMessage.INCORRECT_DATES);
        }
        else
        {
            showMeltsFromTill();
        }
    }

    private void initReportTable()
    {
        this.brandColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("brand"));
        this.amountColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("mass"));
        this.dateColumn.setCellValueFactory(
                (Callback<TableColumn.CellDataFeatures<MeltForView, String>, ObservableValue<String>>) meltForView -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
                    property.setValue(dateFormat.format(meltForView.getValue().getDate()));
                    return property;
                });
        this.lastnameColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("lastname"));
        this.reportTable.setPlaceholder(new Label(ErrorMessage.NO_MELTS_FOUND));
        this.reportTable.getColumns().clear();
        this.reportTable.getColumns().addAll(brandColumn, amountColumn, dateColumn, lastnameColumn);
    }

    private void showAllMelts()
    {
        try
        {
            this.melts = MeltForView.getAllMelts();
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }

        if (this.melts != null)
        {
            this.reportTable.setItems(this.melts);
        }
    }

    private void showMeltsFrom()
    {
        try
        {
            this.melts = MeltForView.getMeltsFrom(Date.from(this.startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }

        if (this.melts != null)
        {
            this.reportTable.setItems(this.melts);
        }
    }

    private void showMeltsTill()
    {
        try
        {
            this.melts = MeltForView.getMeltsTill(Date.from(this.endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }

        if (this.melts != null)
        {
            this.reportTable.setItems(this.melts);
        }
    }

    private void showMeltsFromTill()
    {
        try
        {
            this.melts = MeltForView.getMeltsFromTill(Date.from(this.startDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(this.endDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }

        if (this.melts != null)
        {
            this.reportTable.setItems(this.melts);
        }
    }
}

package Controllers;

import Models.MeltForReport;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import Util.ErrorMessage;
import Util.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;


public class ReportController
{
    @FXML private DatePicker            startDatePicker;
    @FXML private DatePicker            endDatePicker;
    @FXML private TableView             reportTable = new TableView<>();
    @FXML private TableColumn           brandColumn = new TableColumn<>();
    @FXML private TableColumn           amountColumn = new TableColumn<>();
    @FXML private TableColumn           dateColumn = new TableColumn<>();
    @FXML private TableColumn           lastnameColumn = new TableColumn<>();
    private ObservableList<MeltForReport> melts;

    private MenuController              menuController;

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
        this.reportTable.setPlaceholder(new Label("Выберите необходимый период и нажмите кнопу 'Выбрать'"));
    }

    @FXML private void menuButtonClicked()
    {
        menuController.backToMenu();
    }

    //TODO refactor even more
    @FXML private void selectButtonClicked()
    {
        initReportTable();

        if (this.startDatePicker.getValue() != null && this.endDatePicker.getValue() != null)
        {
            if (this.startDatePicker.getValue().isAfter(this.endDatePicker.getValue()))
            {
                Message.showErrorMessage(ErrorMessage.INCORRECT_DATES);
                return;
            }
        }

        Date startDate;
        Date endDate;

        try
        {
            startDate = Date.from(Objects.requireNonNull(this.startDatePicker.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        catch (Exception e)
        {
            startDate = null;
        }

        try
        {
            endDate = Date.from(Objects.requireNonNull(this.endDatePicker.getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        catch (Exception e)
        {
            endDate = null;
        }

        try
        {
            this.melts = MeltForReport.getMelts(startDate, endDate);
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }

        if (this.melts != null)
        {
            this.reportTable.setItems(this.melts);
        }
    }

    private void initReportTable()
    {
        this.brandColumn.setCellValueFactory(new PropertyValueFactory<MeltForReport, String>("brand"));
        this.amountColumn.setCellValueFactory(new PropertyValueFactory<MeltForReport, String>("mass"));
        this.dateColumn.setCellValueFactory(
                (Callback<TableColumn.CellDataFeatures<MeltForReport, String>, ObservableValue<String>>) meltForView -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
                    property.setValue(dateFormat.format(meltForView.getValue().getDate()));
                    return property;
                });
        this.lastnameColumn.setCellValueFactory(new PropertyValueFactory<MeltForReport, String>("lastname"));
        this.reportTable.setPlaceholder(new Label(ErrorMessage.NO_MELTS_FOUND));
        this.reportTable.getColumns().clear();
        this.reportTable.getColumns().addAll(brandColumn, amountColumn, dateColumn, lastnameColumn);
    }
}

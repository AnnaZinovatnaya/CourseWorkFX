package Controllers;

import Models.MeltForView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.util.Callback;
import util.ErrorMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;


public class ReportController
{
    @FXML private DatePicker            firstDate;
    @FXML private DatePicker            secondDate;
    @FXML private TableView             reportTable = new TableView<>();
    @FXML private TableColumn           brandColumn = new TableColumn<>();
    @FXML private TableColumn           amountColumn = new TableColumn<>();
    @FXML private TableColumn           dateColumn = new TableColumn<>();
    @FXML private TableColumn           lastnameColumn = new TableColumn<>();
    private ObservableList<MeltForView> melts;
    private Alert                       alert = new Alert(Alert.AlertType.ERROR);

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

        brandColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("brand"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("mass"));
        dateColumn.setCellValueFactory(
                (Callback<TableColumn.CellDataFeatures<MeltForView, String>, ObservableValue<String>>) meltForView -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
                    property.setValue(dateFormat.format(meltForView.getValue().getDate()));
                    return property;
                });
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("lastname"));
        reportTable.setPlaceholder(new Label(ErrorMessage.NO_MELTS_FOUND));
        reportTable.getColumns().clear();
        reportTable.getColumns().addAll(brandColumn, amountColumn, dateColumn, lastnameColumn);

        if(firstDate.getValue()==null&& secondDate.getValue()==null)
        {
            try {
                melts = MeltForView.getAllMelts();
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }

            if (melts != null)
            {
                reportTable.setItems(melts);
            }
        }
        else if(secondDate.getValue()==null)
        {
            try {
                melts = MeltForView.getMeltsFrom(Date.from(firstDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }

            if (melts != null)
            {
                reportTable.setItems(melts);
            }
        }
        else if(firstDate.getValue()==null)
        {
            try {
                melts = MeltForView.getMeltsTill(Date.from(secondDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }

            if (melts != null)
            {
                reportTable.setItems(melts);
            }
        }
        else if(firstDate.getValue().isAfter(secondDate.getValue()))
        {
            alert.setContentText(ErrorMessage.INCORRECT_DATES);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
        else
        {
            try {
                melts = MeltForView.getMeltsFromTill(Date.from(this.firstDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(this.secondDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }

            if (melts != null)
            {
                reportTable.setItems(melts);
            }
        }
    }
}

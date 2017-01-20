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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;


public class ReportController {

    @FXML private DatePicker FirstDate;
    @FXML private DatePicker SecondDate;
    @FXML private TableView ReportTable = new TableView<>();
    @FXML private TableColumn BrandColumn = new TableColumn<>();
    @FXML private TableColumn AmountColumn = new TableColumn<>();
    @FXML private TableColumn DateColumn = new TableColumn<>();
    ObservableList<MeltForView> data;
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    DirectorMenuController directorMenuController;

    public void setMenuController(DirectorMenuController directorMenuController){
        this.directorMenuController = directorMenuController;
        this.ReportTable.setPlaceholder(new Label("Выберите необходимый период и нажмите кнопу 'Выбрать'"));
    }

    @FXML private void menuButtonClicked(){
        directorMenuController.backToMenu();
    }

    @FXML private void selectButtonClicked(){

        BrandColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("brand"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<MeltForView, String>("mass"));
        DateColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<MeltForView, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<MeltForView, String> meltForView) {
                        SimpleStringProperty property = new SimpleStringProperty();
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        property.setValue(dateFormat.format(meltForView.getValue().getDate()));
                        return property;
                    }
                });
        ReportTable.setPlaceholder(new Label("В этот период не было плавок"));
        ReportTable.getColumns().clear();
        ReportTable.getColumns().addAll(BrandColumn, AmountColumn, DateColumn);

        if(FirstDate.getValue()==null&&SecondDate.getValue()==null){
            data = MeltForView.getAllMelts();
            if (data != null) {
                ReportTable.setItems(data);
            }
        } else if(SecondDate.getValue()==null){
            data = MeltForView.getMeltsFrom(Date.from(FirstDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (data != null) {
                ReportTable.setItems(data);
            }
        } else if(FirstDate.getValue()==null){
            data = MeltForView.getMeltsTill(Date.from(SecondDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (data != null) {
                ReportTable.setItems(data);
            }
        }else if(FirstDate.getValue().isAfter(SecondDate.getValue())){
            alert.setContentText("Даты заданы некорректно!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        } else {
            data = MeltForView.getMeltsFromTill(Date.from(this.FirstDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(this.SecondDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (data != null) {
                ReportTable.setItems(data);
            }
        }
    }
}

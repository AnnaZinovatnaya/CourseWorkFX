package Controllers;

import Models.CompInCharge;
import Models.Register;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ChargeResultController {
    AddCharge5Controller addCharge5Controller;
    AddCharge4Controller addCharge4Controller;

    @FXML private TableColumn<CompInCharge, String> NameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> AmountColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge> ComponentsTable = new TableView<>();
    @FXML private TextField AmountField;
    @FXML private TextField MeltField;
    @FXML private TextField NumberField;

    ObservableList<CompInCharge> data;

    public void init(){
        this.NumberField.setText("1");
        this.AmountField.setText(Register.getChargeMass());
        this.MeltField.setText(Register.getChargeMeltBrand());
        data = Register.getChargeResultComps();

        this.ComponentsTable.setEditable(true);

        this.NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.AmountColumn.setCellValueFactory(new PropertyValueFactory<>("currentMass"));

        this.ComponentsTable.setItems(data);
        this.ComponentsTable.getColumns().clear();
        this.ComponentsTable.getColumns().addAll(NameColumn, AmountColumn);
    }

    @FXML
    private void backButtonClicked(ActionEvent e){
        addCharge4Controller.backToScene();
    }
    @FXML private void doneButtonClicked(ActionEvent e){}
}

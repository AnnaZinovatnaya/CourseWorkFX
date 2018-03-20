package Controllers;

import Models.CompInCharge;
import Models.Manager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ChargeResultController
{
    private AddCharge4Controller                    addCharge4Controller;

    @FXML private TableColumn<CompInCharge, String> nameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> amountColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge>           componentsTable = new TableView<>();
    @FXML private TextField                         amountField;
    @FXML private TextField                         meltField;
    @FXML private TextField                         numberField;
    @FXML private Button                            backButton;
    @FXML private Button                            doneButton;

    private Stage                                   primaryStage;
    private ObservableList<CompInCharge>            components;

    public void init()
    {
        this.primaryStage = this.addCharge4Controller.getPrimaryStage();
        this.numberField.setText("1");
        this.amountField.setText(Manager.getChargeMass());
        this.meltField.setText(Manager.getChargeMeltBrand());
        components = Manager.getChargeResultComps();

        this.componentsTable.setEditable(true);

        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.amountColumn.setCellValueFactory(new PropertyValueFactory<>("currentMass"));

        this.componentsTable.setItems(components);
        this.componentsTable.getColumns().clear();
        this.componentsTable.getColumns().addAll(nameColumn, amountColumn);
        this.primaryStage.setResizable(true);
        this.primaryStage.setMinHeight(400);
        this.primaryStage.setMinWidth(600);
    }

    @FXML private void backButtonClicked()
    {
        this.primaryStage.setResizable(false);
        addCharge4Controller.backToScene();
    }

    @FXML private void doneButtonClicked()
    {
        Manager.saveCharge();
    }

    public void setAddCharge4Controller(AddCharge4Controller addCharge4Controller) {
        this.addCharge4Controller = addCharge4Controller;
    }
}

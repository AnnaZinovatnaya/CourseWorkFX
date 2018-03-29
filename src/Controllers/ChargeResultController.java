package Controllers;

import Models.CompInCharge;
import Models.Manager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Util.Helper;

public class ChargeResultController
{
    private AddCharge4Controller                    addCharge4Controller;

    private Stage                                   primaryStage;

    @FXML private Label                             numberLabel;
    @FXML private Label                             meltLabel;
    @FXML private Label                             amountLabel;

    private ObservableList<CompInCharge>            components;

    @FXML private TableColumn<CompInCharge, String> nameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> amountColumn = new TableColumn<>();

    @FXML private TableView<CompInCharge>           componentsTable = new TableView<>();

    public void init(AddCharge4Controller addCharge4Controller)
    {
        this.addCharge4Controller = addCharge4Controller;

        this.primaryStage = this.addCharge4Controller.getPrimaryStage();
        this.primaryStage.setResizable(true);
        this.primaryStage.setMinHeight(400);
        this.primaryStage.setMinWidth(600);

        this.numberLabel.setText(String.valueOf(Manager.getMaxChargeIndex() + 1));
        this.meltLabel.setText(Manager.getChargeMeltBrand());
        this.amountLabel.setText(Manager.getChargeMass());

        this.components = Manager.getChargeResultComps();

        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.amountColumn.setCellValueFactory(new PropertyValueFactory<>("currentMass"));

        this.componentsTable.setEditable(true);
        this.componentsTable.getColumns().clear();
        this.componentsTable.getColumns().addAll(this.nameColumn, this.amountColumn);
        this.componentsTable.setItems(this.components);
    }

    @FXML private void backButtonClicked()
    {
        this.primaryStage.setResizable(false);
        this.addCharge4Controller.backToScene();
    }

    @FXML private void doneButtonClicked()
    {
        try
        {
            Manager.saveCharge();
            Helper.showInformationMessage("Шихта успешно сохранена!");
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }
    }
}

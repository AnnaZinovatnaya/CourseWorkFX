package Controllers;

import Models.CompInCharge;
import Models.Manager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.Helper;

public class ChargeResultController
{
    private AddCharge4Controller                    addCharge4Controller;

    @FXML private TableColumn<CompInCharge, String> nameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> amountColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge>           componentsTable = new TableView<>();
    @FXML private Label                             amountLabel;
    @FXML private Label                             meltLabel;
    @FXML private Label                             numberLabel;

    private Stage                                   primaryStage;
    private ObservableList<CompInCharge>            components;

    public void init()
    {
        this.primaryStage = this.addCharge4Controller.getPrimaryStage();
        this.numberLabel.setText(String.valueOf(Manager.getMaxChargeIndex() + 1));
        this.amountLabel.setText(Manager.getChargeMass());
        this.meltLabel.setText(Manager.getChargeMeltBrand());
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

    public void setAddCharge4Controller(AddCharge4Controller addCharge4Controller) {
        this.addCharge4Controller = addCharge4Controller;
    }
}

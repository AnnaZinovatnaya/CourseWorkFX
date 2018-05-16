package Controllers;

import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Util.Message;


public class Melt3Controller {
    private Melt2Controller melt2Controller;
    @FXML private Label indexLabel;
    @FXML private Label meltBrandLabel;
    @FXML private Label amountLabel;
    private Charge charge;

    @FXML private TableView<CompInCharge> componentsTable = new TableView<>();
    @FXML private TableColumn<CompInCharge, String> componentNameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> componentMassColumn = new TableColumn<>();

    public void init(Melt2Controller melt2Controller, Charge charge)
    {
        this.melt2Controller = melt2Controller;
        this.charge = charge;
        this.meltBrandLabel.setText(this.charge.getMeltBrand().getName());
        this.amountLabel.setText(String.valueOf(this.charge.getMass()));

        this.indexLabel.setText(String.valueOf(Melt.getMaxIdFromDB() + 1));

        ObservableList<CompInCharge> components = FXCollections.observableArrayList();
        components.addAll(this.charge.getMandatoryComponents());
        components.addAll(this.charge.getOptionalComponents());

        this.componentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.componentNameColumn.setEditable(false);

        this.componentMassColumn.setCellValueFactory(new PropertyValueFactory<>("currentMass"));
        this.componentMassColumn.setEditable(false);

        this.componentsTable.setItems(components);
        this.componentsTable.getColumns().clear();
        this.componentsTable.getColumns().addAll(componentNameColumn, componentMassColumn);
    }

    @FXML
    private void backButtonClicked()
    {
        this.melt2Controller.backToScene();
    }

    @FXML private void doneButtonClicked()
    {
        try
        {
            Manager.newMelt();
            Manager.setMeltCharge(this.charge);
            Manager.saveMelt();

            Message.showInformationMessage("Плавка успешно сохранена!");
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }
}

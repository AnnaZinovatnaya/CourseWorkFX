package Controllers;

import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Melt3Controller {
    private Melt2Controller melt2Controller;
    private Stage primaryStage;
    @FXML private Label indexLabel;
    @FXML private Label meltBrandLabel;
    @FXML private Label amountLabel;
    private Charge charge;

    @FXML private TableView<CompInCharge> componentsTable = new TableView<>();
    @FXML private TableColumn<CompInCharge, String> componentNameColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> componentMassColumn = new TableColumn<>();

    private ObservableList<CompInCharge> components;

    public void init(Stage primaryStage, Melt2Controller melt2Controller, Charge charge)
    {
        this.primaryStage = primaryStage;
        this.melt2Controller = melt2Controller;
        this.charge = charge;
        this.meltBrandLabel.setText(this.charge.getMeltBrand().getName());
        this.amountLabel.setText(String.valueOf(this.charge.getMass()));

        this.indexLabel.setText(String.valueOf(Manager.getMaxMeltIndex() + 1));

        this.components = FXCollections.observableArrayList();
        this.components.addAll(this.charge.getMandatoryComponents());
        this.components.addAll(this.charge.getOptionalComponents());

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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Сохранение");
            alert.setHeaderText("Сохранение");
            alert.setContentText("Плавка успешно сохранена!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
        catch (RuntimeException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getLocalizedMessage());
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }
}

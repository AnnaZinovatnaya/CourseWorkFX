package Controllers;

import Models.Component;
import Models.Register;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DBUtil;

import javax.xml.transform.Result;
import java.sql.ResultSet;

public class ShowComponentsController {
    MetallurgistMenuController metallurgistMenuController;
    @FXML private TableColumn<Component, String> NameColumnM = new TableColumn<>();
    @FXML private TableColumn<Component, String> BrandColumnM = new TableColumn<>();
    @FXML private TableColumn<Component, String> AmountColumnM = new TableColumn<>();
    @FXML private TableColumn<Component, String> PriceColumnM = new TableColumn<>();
    @FXML public TableView<Component> MandatoryComponentsTable = new TableView<>();

    @FXML private TableColumn<Component, String> NameColumnO = new TableColumn<>();
    @FXML private TableColumn<Component, String> BrandColumnO = new TableColumn<>();
    @FXML private TableColumn<Component, String> AmountColumnO = new TableColumn<>();
    @FXML private TableColumn<Component, String> PriceColumnO = new TableColumn<>();
    @FXML public TableView<Component> OptionalComponentsTable = new TableView<>();

    @FXML private Tab mandatoryTab = new Tab();
    @FXML private Tab optionalTab = new Tab();

    ObservableList<Component> mandatoryComps;
    ObservableList<Component> optionalComps;

    private boolean mandatoryChanged=false;
    private boolean optionalChanged=false;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void setMenuController(MetallurgistMenuController metallurgistMenuController){
        this.metallurgistMenuController = metallurgistMenuController;
    }

    public void init(){
        this.OptionalComponentsTable.setEditable(true);
        this.MandatoryComponentsTable.setEditable(true);

        mandatoryComps = Register.getAllMandatoryComponents();
        optionalComps = Register.getAllOptionalComponents();

        this.NameColumnM.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.BrandColumnM.setCellValueFactory(new PropertyValueFactory<>("brand"));
        this.AmountColumnM.setCellValueFactory(new PropertyValueFactory<>("amount"));
        this.AmountColumnM.setEditable(true);
        this.AmountColumnM.setCellFactory(TextFieldTableCell.forTableColumn());
        this.AmountColumnM.setOnEditCommit(
                t -> {
                    double amount;
                    boolean b= true;
                    try {
                        amount = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Количество задано некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setAmount(Double.parseDouble(t.getNewValue()));
                        mandatoryChanged = true;
                    }
                }
        );

        this.PriceColumnM.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.PriceColumnM.setEditable(true);
        this.PriceColumnM.setCellFactory(TextFieldTableCell.forTableColumn());
        this.PriceColumnM.setOnEditCommit(
                t -> {
                    double price;
                    boolean b= true;
                    try {
                        price = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Цена задана некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setPrice(Double.parseDouble(t.getNewValue()));
                        mandatoryChanged = true;
                    }
                }
        );

        this.MandatoryComponentsTable.setItems(mandatoryComps);
        this.MandatoryComponentsTable.getColumns().clear();
        this.MandatoryComponentsTable.getColumns().addAll(NameColumnM, BrandColumnM, AmountColumnM, PriceColumnM);

        this.NameColumnO.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.BrandColumnO.setCellValueFactory(new PropertyValueFactory<>("brand"));
        this.AmountColumnO.setCellValueFactory(new PropertyValueFactory<>("amount"));
        this.AmountColumnO.setEditable(true);
        this.AmountColumnO.setCellFactory(TextFieldTableCell.forTableColumn());
        this.AmountColumnO.setOnEditCommit(
                t -> {
                    double amount;
                    boolean b= true;
                    try {
                        amount = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Количество задано некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setAmount(Double.parseDouble(t.getNewValue()));
                        optionalChanged = true;
                    }
                }
        );
        this.PriceColumnO.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.PriceColumnO.setEditable(true);
        this.PriceColumnO.setCellFactory(TextFieldTableCell.forTableColumn());
        this.PriceColumnO.setOnEditCommit(
                t -> {
                    double price;
                    boolean b= true;
                    try {
                        price = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Цена задана некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setPrice(Double.parseDouble(t.getNewValue()));
                        optionalChanged = true;
                    }
                }
        );

        this.OptionalComponentsTable.setItems(optionalComps);
        this.OptionalComponentsTable.getColumns().clear();
        this.OptionalComponentsTable.getColumns().addAll(NameColumnO, BrandColumnO, AmountColumnO, PriceColumnO);
    }

    @FXML private void menuButtonClicked(ActionEvent e){
        metallurgistMenuController.backToMenu();
    }

    @FXML private void saveButtonClicked(ActionEvent e) {
        if(optionalChanged){
            Register.updateOptionalComponentsData(optionalComps);
        }
        if(mandatoryChanged){
            Register.updateMandatoryComponentsData(mandatoryComps);
        }
    }
    @FXML private void deleteButtonClicked(ActionEvent e) {


        if(this.mandatoryTab.isSelected()) {
            Component temp = this.MandatoryComponentsTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                try {
                    Stage stage = new Stage();
                    stage.setTitle("Удаление компонента");
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/Views/DeleteComponentScene.fxml")
                    );
                    Parent root = loader.load();
                    DeleteComponentController deleteComponentController = loader.getController();
                    deleteComponentController.showComponentsController=this;
                    deleteComponentController.init(temp, 0);
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(this.MandatoryComponentsTable.getScene().getWindow());
                    stage.showAndWait();

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            } else{
                alert.setContentText("Выберите компонент для удаления!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }


        }
        else{
            Component temp = this.OptionalComponentsTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                try {
                    Stage stage = new Stage();
                    stage.setTitle("Удаление компонента");
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/Views/DeleteComponentScene.fxml")
                    );
                    Parent root = loader.load();
                    DeleteComponentController deleteComponentController = loader.getController();
                    deleteComponentController.showComponentsController=this;
                    deleteComponentController.init(temp, 1);
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(this.OptionalComponentsTable.getScene().getWindow());
                    stage.showAndWait();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } else{
                alert.setContentText("Выберите компонент для удаления!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }

    }
}

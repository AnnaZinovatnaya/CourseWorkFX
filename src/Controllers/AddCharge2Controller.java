package Controllers;

import Models.Element;
import Models.Register;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddCharge2Controller {

    @FXML private TableView<Element> ElementsTable = new TableView<>();
    @FXML private TableColumn<Element, String> NameColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> MinPercentColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> MaxPercentColumn = new TableColumn<>();

    AddCharge1Controller addCharge1Controller;

    private ObservableList<Element> data;

    private Alert alert = new Alert(Alert.AlertType.ERROR);
    Stage primaryStage;

    public void init(){
        this.primaryStage = this.addCharge1Controller.primaryStage;
        this.ElementsTable.setEditable(true);
        this.data = FXCollections.observableArrayList();
        for(Element aElement: Register.getChargeElements()){
            this.data.add(new Element(aElement));
        }

        this.NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.NameColumn.setEditable(false);

        this.MinPercentColumn.setCellValueFactory(new PropertyValueFactory<>("minPercent"));
        this.MinPercentColumn.setEditable(true);
        this.MinPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.MinPercentColumn.setOnEditCommit(
                t -> {
                    double minPercent;
                    boolean b= true;
                    try {
                        minPercent = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Минимальный процент задан некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {
                            t.getTableView().getItems().get(
                                    t.getTablePosition().getRow()).setMinPercent(Double.parseDouble(t.getNewValue()));
                    }
                }
        );

        this.MaxPercentColumn.setCellValueFactory(new PropertyValueFactory<>("maxPercent"));
        this.MaxPercentColumn.setEditable(true);
        this.MaxPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.MaxPercentColumn.setOnEditCommit(
                t -> {
                    double maxPercent;
                    boolean b= true;
                    try {
                        maxPercent = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Максимальный процент задан некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();
                        b=false;
                    }

                    if(b) {
                            t.getTableView().getItems().get(
                                    t.getTablePosition().getRow()).setMaxPercent(Double.parseDouble(t.getNewValue()));
                    }
                }
        );

        this.ElementsTable.setItems(data);
        this.ElementsTable.getColumns().clear();
        this.ElementsTable.getColumns().addAll(NameColumn, MinPercentColumn, MaxPercentColumn);
    }

    @FXML private void backButtonClicked(){
        addCharge1Controller.backToScene();
    }
    @FXML private void nextButtonClicked(){
        boolean b = true;
        for(Element aElement: data){
            if(aElement.getMinPercentDouble()>aElement.getMaxPercentDouble()){
                alert.setContentText("Минимальный процен не может быть больше максимального!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b=false;
                break;
            }
        }
        if(b){
            Register.setChargeElements(data);

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/AddCharge3Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge3Controller addCharge3Controller = loader.getController();
                addCharge3Controller.addCharge2Controller = this;
                addCharge3Controller.init();
                primaryStage.setScene(new Scene(root));
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void backToScene(){
        this.primaryStage.setScene(this.ElementsTable.getScene());
    }

}

package Controllers;

import Models.CompInCharge;
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

public class AddCharge4Controller {

    AddCharge3Controller addCharge3Controller;

    @FXML
    private TableColumn<CompInCharge, String> ComponentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> MinPercentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> MaxPercentColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge> ComponentsTable = new TableView<>();

    ObservableList<CompInCharge> data;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public  void init(){
        data = Register.getChargeMandatoryComps();


        this.ComponentsTable.setEditable(true);

        this.ComponentColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        this.MinPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.MinPercentColumn.setOnEditCommit(
                t -> {
                    double percent;
                    boolean b= true;
                    try {
                        percent = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Минимальный процент задан некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMinPercent(Double.parseDouble(t.getNewValue()));
                    } else{
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMinPercent(0);
                    }
                }
        );

        this.MaxPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.MaxPercentColumn.setOnEditCommit(
                t -> {
                    double percent;
                    boolean b= true;
                    try {
                        percent = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Максимальный процент задан некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMaxPercent(Double.parseDouble(t.getNewValue()));
                    } else{
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setMaxPercent(0);
                    }
                }
        );
        this.ComponentsTable.setItems(data);
        this.ComponentsTable.getColumns().clear();
        this.ComponentsTable.getColumns().addAll(ComponentColumn, MinPercentColumn, MaxPercentColumn);
    }

    @FXML
    private void backButtonClicked(ActionEvent e){
        addCharge3Controller.backToScene();
    }
    @FXML private void nextButtonClicked(ActionEvent e){

        for(CompInCharge aComp: data){
            if(aComp.getMaxPercent()==0||aComp.getMaxPercent()==0){
                alert.setContentText("Все поля должны быть заполнены!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                return;
            }
        }

        for(CompInCharge aComp: data){
            if(aComp.getMaxPercent()<aComp.getMinPercent()){
                alert.setContentText("Максимальное значение не может быть меньше минимального: "+aComp.getName());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                return;
            }
        }
        double min=0;
        double max=0;
        for(CompInCharge aComp: data){
            min+=aComp.getMinPercent();
            max+=aComp.getMaxPercent();
        }

        if(min>100){
            alert.setContentText("Сумма минимальных значений не может превышать 100%!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            return;
        }

        if(max<100){
            alert.setContentText("Сумма максимальных значений не может быть меньше 100%!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            return;
        }

        if(Register.isChargePossible()){
            Register.calculateCheapCharge();
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/ChargeResultScene.fxml")
                );
                Parent root = loader.load();
                ChargeResultController chargeResultController = loader.getController();
                chargeResultController.addCharge4Controller = this;
                chargeResultController.init();
                addCharge3Controller.addCharge2Controller.addCharge1Controller.metallurgistMenuController.primaryStage.setScene(new Scene(root));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else{
            alert.setContentText("Набор шихты невозможен");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }


    }

    public void backToScene(){
        this.addCharge3Controller.addCharge2Controller.addCharge1Controller.metallurgistMenuController.primaryStage.setScene(this.ComponentsTable.getScene());
    }
}

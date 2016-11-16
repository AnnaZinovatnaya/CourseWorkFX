package Controllers;

import Models.CompInCharge;
import Models.Register;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;

import javax.print.DocFlavor;

public class AddCharge5Controller {
    AddCharge4Controller addCharge4Controller;

    @FXML
    private TableColumn<CompInCharge, String> ComponentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> MinPercentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> MaxPercentColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge> ComponentsTable = new TableView<>();

    ObservableList<CompInCharge> data;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    public  void init(){
        data = Register.getChargeOptionalComps();


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
        addCharge4Controller.backToScene();
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

        if(Register.isChargePossible()){
            alert.setContentText("Набор шихты возможен");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        } else{
            alert.setContentText("Набор шихты невозможен");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }
}
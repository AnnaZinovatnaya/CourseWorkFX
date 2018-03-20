package Controllers;

import Models.CompInCharge;
import Models.Manager;
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
import util.ErrorMessage;

public class AddCharge4Controller
{

    private AddCharge3Controller                    addCharge3Controller;
    private Stage                                   primaryStage;
    private Alert                                   alert;

    @FXML private TableColumn<CompInCharge, String> componentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> minComponentPercentColumn = new TableColumn<>();
    @FXML private TableColumn<CompInCharge, String> maxComponentPercentColumn = new TableColumn<>();
    @FXML private TableView<CompInCharge>           componentsTable = new TableView<>();
    private ObservableList<CompInCharge>            components;

    @FXML public  void init()
    {
        this.primaryStage = this.addCharge3Controller.getPrimaryStage();
        this.alert = new Alert(Alert.AlertType.ERROR);

        this.components = Manager.getChargeMandatoryComps();
        this.componentsTable.setEditable(true);
        this.componentColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.minComponentPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.minComponentPercentColumn.setOnEditCommit(
            t ->
        {
            double percent;
            boolean b= true;
            try
            {
                percent = Double.parseDouble(t.getNewValue());
            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.INCORRECT_MIN_PERCENT);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();

                b=false;
            }

            if(b)
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setMinPercent(Double.parseDouble(t.getNewValue()));
            }
            else
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setMinPercent(0);
            }
        }
        );

        this.maxComponentPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.maxComponentPercentColumn.setOnEditCommit(
            t ->
        {
            double percent;
            boolean b= true;
            try
            {
                percent = Double.parseDouble(t.getNewValue());
            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.INCORRECT_MAX_PERCENT);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();

                b=false;
            }

            if(b)
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setMaxPercent(Double.parseDouble(t.getNewValue()));
            }
            else
            {
                t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setMaxPercent(0);
            }
        }
        );
        this.componentsTable.setItems(components);
        this.componentsTable.getColumns().clear();
        this.componentsTable.getColumns().addAll(componentColumn, minComponentPercentColumn, maxComponentPercentColumn);
    }

    @FXML
    private void backButtonClicked()
    {
        addCharge3Controller.backToScene();
    }
    @FXML private void nextButtonClicked()
    {

        for(CompInCharge aComp: components)
        {
            if(aComp.getMaxPercent()==0||aComp.getMaxPercent()==0)
            {
                alert.setContentText(ErrorMessage.EMPTY_FIELDS);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                return;
            }
        }

        for(CompInCharge aComp: components)
        {
            if(aComp.getMaxPercent()<aComp.getMinPercent())
            {
                alert.setContentText("Максимальное значение не может быть меньше минимального: " + aComp.getName());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                return;
            }
        }
        double min=0;
        double max=0;
        for(CompInCharge aComp: components)
        {
            min+=aComp.getMinPercent();
            max+=aComp.getMaxPercent();
        }

        if(min>100)
        {
            alert.setContentText("Сумма минимальных значений не может превышать 100%!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            return;
        }

        if(max<100)
        {
            alert.setContentText("Сумма максимальных значений не может быть меньше 100%!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            return;
        }

        if(Manager.isChargePossible())
        {
            Manager.calculateCheapCharge();
            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ChargeResultScene.fxml")
                );
                Parent root = loader.load();
                ChargeResultController chargeResultController = loader.getController();
                chargeResultController.setAddCharge4Controller(this);
                chargeResultController.init();
                primaryStage.setScene(new Scene(root));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            alert.setContentText("Набор шихты невозможен");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.componentsTable.getScene());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setAddCharge3Controller(AddCharge3Controller addCharge3Controller) {
        this.addCharge3Controller = addCharge3Controller;
    }
}

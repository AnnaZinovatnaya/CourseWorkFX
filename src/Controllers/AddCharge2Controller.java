package Controllers;

import Models.Element;
import Models.Manager;
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
import util.ErrorMessage;

public class AddCharge2Controller
{
    private AddCharge1Controller               addCharge1Controller;
    private Stage                              primaryStage;
    private Alert                              alert;

    @FXML private TableView<Element>           elementsTable = new TableView<>();
    @FXML private TableColumn<Element, String> elementNameColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> minElementPercentColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> maxElementPercentColumn = new TableColumn<>();

    private ObservableList<Element>            elements;

    public void init()
    {
        this.primaryStage = this.addCharge1Controller.getPrimaryStage();
        this.alert = new Alert(Alert.AlertType.ERROR);
        this.elementsTable.setEditable(true);
        this.elements = FXCollections.observableArrayList();
        for(Element aElement: Manager.getChargeElements())
        {
            this.elements.add(new Element(aElement));
        }

        this.elementNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.elementNameColumn.setEditable(false);

        this.minElementPercentColumn.setCellValueFactory(new PropertyValueFactory<>("minPercent"));
        this.minElementPercentColumn.setEditable(true);
        this.minElementPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.minElementPercentColumn.setOnEditCommit(
            t ->
        {
            double minPercent;
            boolean b= true;
            try
            {
                minPercent = Double.parseDouble(t.getNewValue());
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
        }
        );

        this.maxElementPercentColumn.setCellValueFactory(new PropertyValueFactory<>("maxPercent"));
        this.maxElementPercentColumn.setEditable(true);
        this.maxElementPercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.maxElementPercentColumn.setOnEditCommit(
            t ->
        {
            double maxPercent;
            boolean b= true;
            try
            {
                maxPercent = Double.parseDouble(t.getNewValue());
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
        }
        );

        this.elementsTable.setItems(elements);
        this.elementsTable.getColumns().clear();
        this.elementsTable.getColumns().addAll(elementNameColumn, minElementPercentColumn, maxElementPercentColumn);
    }

    @FXML private void backButtonClicked()
    {
        addCharge1Controller.backToScene();
    }
    @FXML private void nextButtonClicked()
    {
        boolean b = true;
        for(Element aElement: elements)
        {
            if(aElement.getMinPercentDouble()>aElement.getMaxPercentDouble())
            {
                alert.setContentText(ErrorMessage.MIN_BIGGER_THAN_MAX);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b=false;
                break;
            }
        }
        if(b)
        {
            Manager.setChargeElements(elements);

            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge3Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge3Controller addCharge3Controller = loader.getController();
                addCharge3Controller.setAddCharge2Controller(this);
                addCharge3Controller.init();
                primaryStage.setScene(new Scene(root));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.elementsTable.getScene());
    }

    public void setAddCharge1Controller(AddCharge1Controller addCharge1Controller) {
        this.addCharge1Controller = addCharge1Controller;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

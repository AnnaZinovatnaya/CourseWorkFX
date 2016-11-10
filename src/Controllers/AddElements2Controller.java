package Controllers;

import Models.Component;
import Models.ElementForView;
import Models.ElementInComp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.ResultSet;


public class AddElements2Controller {
    Stage primaryStage;
    public AddElementsController addElementsController;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    ObservableList<String> selectedItems;
    ObservableList<ElementForView> data;
    Component component;

    @FXML private TableColumn<ElementForView, String> ElementColumn = new TableColumn<>();
    @FXML private TableColumn<ElementForView, String> ProcentColumn = new TableColumn<>();
    @FXML private TableColumn<ElementForView, String> AdoptColumn = new TableColumn<>();
    @FXML private TableView<ElementForView> ElementsTable = new TableView<>();

    public void setPreviousController(AddElementsController addElementsController){
        this.addElementsController = addElementsController;
        primaryStage = addElementsController.primaryStage;
        this.component = addElementsController.addComponentController.component;
    }

    @FXML
    public  void init(ObservableList<String> selectedItems){
        this.ElementsTable.setEditable(true);

        data = FXCollections.observableArrayList ();
        this.selectedItems = selectedItems;
        for(int i=0; i<selectedItems.size();i++){
            data.add(new ElementForView(selectedItems.get(i), 0, 0));
        }

        this.ElementColumn.setCellValueFactory(new PropertyValueFactory<ElementForView, String>("name"));

        this.ProcentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.ProcentColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ElementForView, String>>() {
                    public void handle(TableColumn.CellEditEvent<ElementForView, String> t) {
                        double procent;
                        boolean b= true;
                        try {
                            procent = Double.parseDouble(t.getNewValue());
                        } catch (Exception ex){
                            alert.setContentText("Процент задан некорректно!");
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                            alert.showAndWait();

                            b=false;
                        }

                        if(b) {
                            ((ElementForView) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setProcent(Double.parseDouble(t.getNewValue()));
                        } else{
                            ((ElementForView) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setProcent(0);

                        }
                    }
                }
        );


        this.AdoptColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.AdoptColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ElementForView, String>>() {
                    public void handle(TableColumn.CellEditEvent<ElementForView, String> t) {
                        double adopt;
                        boolean b= true;
                        try {
                            adopt = Double.parseDouble(t.getNewValue());
                        } catch (Exception ex){
                            alert.setContentText("Усвоение задано некорректно!");
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                            alert.showAndWait();
                            b=false;
                        }

                        if(b) {
                            ((ElementForView) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setAdopt(Double.parseDouble(t.getNewValue()));
                        } else {

                        }
                    }
                }
        );

        this.ElementsTable.setItems(data);
        this.ElementsTable.getColumns().clear();
        this.ElementsTable.getColumns().addAll(ElementColumn, ProcentColumn, AdoptColumn);

    }

    @FXML private void backButtonClicked(ActionEvent e){
        addElementsController.backToScene();
    }
    @FXML private void finishedButtonClicked(ActionEvent e){
        ResultSet rs=null;
        int idElement=0;
        int idComponent=0;
        boolean b=true;
        String name="";
        b=component.addComponent();
            if(b) {
                b=component.getIDfromDB();
                if (b) {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        name=selectedItems.get(i);
                        idElement = ElementForView.getElementIdfromDB(name);
                        if(idElement==0)
                            b = false;
                        if (b) {
                            ElementInComp elementInComp = new ElementInComp(0, ElementsTable.getItems().get(i).getProcent(), ElementsTable.getItems().get(i).getAdopt(), idElement, component.getIdComp());
                            b = elementInComp.addElementInComp();
                        }
                    }

                    if(b){
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Information");
                        alert.setContentText("Компонент сохранен!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        this.addElementsController.addComponentController.metallurgistMenuController.backToMenu();;
                    }
                }

            }

    }
}

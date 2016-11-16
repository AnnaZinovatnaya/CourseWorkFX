package Controllers;

import Models.Component;
import Models.Element;
import Models.Register;
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


public class AddElements2Controller {
    Stage primaryStage;
    public AddElementsController addElementsController;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    ObservableList<String> selectedItems;
    ObservableList<Element> data;
    Component component;

    @FXML private TableColumn<Element, String> ElementColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> PercentColumn = new TableColumn<>();
    @FXML private TableColumn<Element, String> AdoptColumn = new TableColumn<>();
    @FXML private TableView<Element> ElementsTable = new TableView<>();

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
        for (String selectedItem : selectedItems) {
            data.add(new Element(selectedItem, 0, 0, 0, 0));
        }

        this.ElementColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        this.PercentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.PercentColumn.setOnEditCommit(
                t -> {
                    double percent;
                    boolean b= true;
                    try {
                        percent = Double.parseDouble(t.getNewValue());
                    } catch (Exception ex){
                        alert.setContentText("Процент задан некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                        b=false;
                    }

                    if(b) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setPercent(Double.parseDouble(t.getNewValue()));
                    } else{
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setPercent(0);

                    }
                }
        );

        this.AdoptColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.AdoptColumn.setOnEditCommit(
                t -> {
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
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setAdopt(Double.parseDouble(t.getNewValue()));
                    } else {

                    }
                }
        );
        this.ElementsTable.setItems(data);
        this.ElementsTable.getColumns().clear();
        this.ElementsTable.getColumns().addAll(ElementColumn, PercentColumn, AdoptColumn);

    }

    @FXML private void backButtonClicked(ActionEvent e){
        addElementsController.backToScene();
    }
    @FXML private void finishedButtonClicked(ActionEvent e){

        boolean b=true; //проверить, чтобы все поля были заполнены
        String name="";

                if (b) {
                    for (int i = 0; i < selectedItems.size(); i++) {

                        Register.setComponentElement(selectedItems.get(i), ElementsTable.getItems().get(i).getPercent(), ElementsTable.getItems().get(i).getAdopt());

                    }
                    Register.saveComponentParam();
                    Register.saveComponentElements();

                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("Information");
                        alert.setContentText("Компонент сохранен!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();

                    if(this.addElementsController.addComponentController.metallurgistMenuController!=null) {
                        this.addElementsController.addComponentController.metallurgistMenuController.backToMenu();
                    }
                    else
                        this.addElementsController.addComponentController.directorMenuController.backToMenu();
                }



    }
}

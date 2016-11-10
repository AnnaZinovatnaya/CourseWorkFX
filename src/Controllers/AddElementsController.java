package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddElementsController {
    public AddComponentController addComponentController;
    @FXML private Button NextButton;
    @FXML private ListView<String> AllElementsList = new ListView<String>();
    @FXML private ListView<String> SelectedElementsList = new ListView<String>();
    private ObservableList<String> items;
    private ObservableList<String> selectedItems;
    Stage primaryStage;
    private AddElements2Controller addElements2Controller;



    public void setPreviousController(AddComponentController addComponentController){
        this.addComponentController = addComponentController;
    }

    @FXML public  void init(){
        items =FXCollections.observableArrayList (
                "C", "S", "Si");
        selectedItems =FXCollections.observableArrayList ();
        this.AllElementsList.setItems(items);
        this.SelectedElementsList.setItems(selectedItems);
    }

    @FXML private void oneForwardButtonClicked(ActionEvent e){
        String temp = AllElementsList.getSelectionModel().getSelectedItem();
        if(!selectedItems.contains(temp)&&temp!=null) {
            this.SelectedElementsList.getItems().add(temp);
        }
    }

    @FXML private void allForwardButtonClicked(ActionEvent e){
            this.SelectedElementsList.getItems().clear();
            for (int i = 0; i < items.size(); i++) {
                this.SelectedElementsList.getItems().add(this.items.get(i));
            }
    }

    @FXML private void oneBackButtonClicked(ActionEvent e){
        String temp = SelectedElementsList.getSelectionModel().getSelectedItem();
        if(temp!=null) {
            this.SelectedElementsList.getItems().remove(temp);
        }
    }

    @FXML private void allBackButtonClicked(ActionEvent e){
        this.SelectedElementsList.getItems().clear();
    }

    @FXML private void backButtonClicked(ActionEvent e){
        addComponentController.backToScene();
    }

    @FXML private void nextButtonClicked(ActionEvent e){
        if(selectedItems.size()>0) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("../Views/AddElements2Scene.fxml")
                );
                Parent root = loader.load();
                addElements2Controller = loader.getController();
                addElements2Controller.setPreviousController(this);
                addElements2Controller.init(selectedItems);
                primaryStage = addComponentController.primaryStage;
                primaryStage.setScene(new Scene(root));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ни один элемент не выбран!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public void backToScene(){
        primaryStage.setScene(this.NextButton.getScene());
    }




}

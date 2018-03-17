package Controllers;

import Models.Register;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddCharge3Controller {

    AddCharge2Controller addCharge2Controller;

    @FXML private ListView<String> AllComponentsList = new ListView<>();
    @FXML private ListView<String> SelectedComponentsList = new ListView<>();
    private ObservableList<String> items;
    private ObservableList<String> selectedItems;
    Stage primaryStage;

    @FXML private void backButtonClicked(){
        addCharge2Controller.backToScene();
    }
    @FXML private void nextButtonClicked(){
        if(selectedItems.size()>0) {
            Register.setMandatoryComponents(selectedItems);
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/AddCharge4Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge4Controller addCharge4Controller = loader.getController();
                addCharge4Controller.addCharge3Controller = this;
                addCharge4Controller.init();
                primaryStage.setScene(new Scene(root));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ни один компонент не выбран!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public void init(){
        this.primaryStage = this.addCharge2Controller.primaryStage;
        items = Register.getAllMandatoryComponentsString();
        selectedItems = FXCollections.observableArrayList ();
        this.AllComponentsList.setItems(items);
        this.SelectedComponentsList.setItems(selectedItems);
    }

    @FXML private void oneForwardButtonClicked(){
        String temp = AllComponentsList.getSelectionModel().getSelectedItem();
        if(!selectedItems.contains(temp)&&temp!=null) {
            this.SelectedComponentsList.getItems().add(temp);
        }
    }

    @FXML private void allForwardButtonClicked(){
        this.SelectedComponentsList.getItems().clear();
        for (String item : items) {
            this.SelectedComponentsList.getItems().add(item);
        }
    }

    @FXML private void oneBackButtonClicked(){
        String temp = SelectedComponentsList.getSelectionModel().getSelectedItem();
        if(temp!=null) {
            this.SelectedComponentsList.getItems().remove(temp);
        }
    }

    @FXML private void allBackButtonClicked(){
        this.SelectedComponentsList.getItems().clear();
    }


    public void backToScene(){
        this.primaryStage.setScene(this.AllComponentsList.getScene());
    }
}

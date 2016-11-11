package Controllers;

import Models.Register;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class AddCharge1Controller {
    MetallurgistMenuController metallurgistMenuController;
    @FXML private ChoiceBox<String> BrandBox;
    @FXML private TextField MassField;
    @FXML private TextField DeltaMassField;
    ObservableList<String> data;
    private Alert alert = new Alert(Alert.AlertType.ERROR);


    public void setMenuController(MetallurgistMenuController metallurgistMenuController){
        this.metallurgistMenuController = metallurgistMenuController;
    }

    public void init(){
        this.data = FXCollections.observableArrayList ("");
        ObservableList<String> temp = Register.getAllBrands();
        for (String aTemp : temp) {
            this.data.add(aTemp);
        }

        this.BrandBox.setItems(this.data);
        this.BrandBox.setValue("");
    }

    @FXML public void menuButtonClicked(ActionEvent e){
        metallurgistMenuController.backToMenu();
    }

    @FXML public void nextButtonClicked(ActionEvent e){
        boolean b=true;
        if(BrandBox.getValue().isEmpty()||MassField.getText().isEmpty()||DeltaMassField.getText().isEmpty()){
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            b=false;
        } else if(b){

        }

    }

}

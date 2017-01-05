package Controllers;

import Models.Register;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        double mass=0;
        double deltaMass=0;
        if(BrandBox.getValue().isEmpty()||MassField.getText().isEmpty()||DeltaMassField.getText().isEmpty()){
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            b=false;
        }
        if(b) {


            try {
                mass = Double.parseDouble(MassField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Масса задана некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
        }
        if(b){
            try{
                deltaMass = Double.parseDouble(DeltaMassField.getText());
            } catch (Exception ex){
                ex.printStackTrace();
                alert.setContentText("Отклонение по массе задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b=false;
            }
        }
        if(b){
            Register.newCharge();
            Register.setChargeBrand(BrandBox.getValue());
            Register.setChatgeMassAndDelta(mass, deltaMass);

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/AddCharge2Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge2Controller addCharge2Controller = loader.getController();
                addCharge2Controller.addCharge1Controller = this;
                addCharge2Controller.init();
                metallurgistMenuController.primaryStage.setScene(new Scene(root));
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void backToScene(){
        this.metallurgistMenuController.primaryStage.setScene(this.BrandBox.getScene());
    }
}



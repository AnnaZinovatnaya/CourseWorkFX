package Controllers;

import Models.Component;
import Models.Register;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddComponentController {

    MenuController menuController;
    //DirectorMenuController directorMenuController;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML private TextField NameField;
    @FXML private TextField BrandField;
    @FXML private TextField AdoptField;
    @FXML private TextField AmountField;
    @FXML private TextField PriceField;
    @FXML private RadioButton MandatoryButton;
    @FXML private RadioButton OptionalButton;

    public Stage primaryStage;
    public Component component;

    public void setMenuController(MenuController menuController){
        this.menuController = menuController;
        this.primaryStage = this.menuController.primaryStage;
    }

    @FXML private void mandatoryButtonClicked(){
        this.OptionalButton.setSelected(false);
    }

    @FXML private void optionalButtonClicked(){
        this.MandatoryButton.setSelected(false);
    }

    @FXML private void menuButtonClicked(){
        Register.setComponentToNull();
        menuController.backToMenu();
    }

    public void backToScene(){
        this.primaryStage.setScene(this.AdoptField.getScene());
    }

    @FXML private void nextButtonClicked(){
        Register.newComponent();
        if(NameField.getText().isEmpty()||BrandField.getText().isEmpty()||AdoptField.getText().isEmpty()||AmountField.getText().isEmpty()||PriceField.getText().isEmpty()||!MandatoryButton.isSelected()&&!OptionalButton.isSelected()){

            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        } else {
            double adopt=-1;
            double amount=-1;
            double price=-1;
            boolean b=true;
            if(Register.componentExists(NameField.getText())){
                alert.setContentText("Компонент с таким названием уже есть!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b=false;
            }
            if(b){
                try {
                    adopt = Double.parseDouble(AdoptField.getText());
                } catch (Exception ex){
                    alert.setContentText("Усвоение базового элемента задано некорректно!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                    alert.showAndWait();
                    b=false;
                }
                if(b) {
                    if (adopt < -1 || adopt > 100) {
                        alert.setContentText("Усвоение базового элемента задано некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();
                        b = false;
                    }
                    if (b) {
                        try {
                            amount = Double.parseDouble(AmountField.getText());
                        } catch (Exception ex) {
                            alert.setContentText("Количество на складе задано некорректно!");
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                            alert.showAndWait();
                            b = false;
                        }
                        if (b) {
                            if (adopt < -1) {
                                alert.setContentText("Количество на складе задано некорректно!");
                                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                                alert.showAndWait();
                                b = false;
                            }
                            if (b) {
                                try {
                                    price = Double.parseDouble(PriceField.getText());
                                } catch (Exception ex) {
                                    alert.setContentText("Цена задана некорректно!");
                                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                                    alert.showAndWait();
                                    b = false;
                                }
                                if (b) {
                                    if (price < -1) {
                                        alert.setContentText("Цена задана некорректно!");
                                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                                        alert.showAndWait();
                                        b=false;
                                    }
                                    if(b){
                                            int mandatory=0;
                                            if(this.MandatoryButton.isSelected())
                                                mandatory=1;
                                            Register.setComponentParam(NameField.getText(), BrandField.getText(), adopt, amount, price, mandatory);
                                            try {

                                                FXMLLoader loader = new FXMLLoader(
                                                        getClass().getResource("/Views/AddElements2Scene.fxml")
                                                );
                                                Parent root = loader.load();

                                                AddElements2Controller addElements2Controller = loader.getController();
                                                addElements2Controller.setPreviousController(this);
                                                addElements2Controller.init(FXCollections.observableArrayList("C", "S", "Si"));
                                                primaryStage.setScene(new Scene(root));
                                            } catch (Exception ex){
                                                ex.printStackTrace();
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

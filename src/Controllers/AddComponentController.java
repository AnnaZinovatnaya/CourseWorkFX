package Controllers;

import Models.Component;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AddComponentController {

    MetallurgistMenuController metallurgistMenuController;
    DirectorMenuController directorMenuController;
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
    private AddElementsController addElementsController;

    public void setMenuController(MetallurgistMenuController metallurgistMenuController){
        this.metallurgistMenuController = metallurgistMenuController;
    }

    public void setMenuController(DirectorMenuController directorMenuController){
        this.directorMenuController = directorMenuController;
    }

    @FXML private void mandatoryButtonClicked(ActionEvent e){
        this.OptionalButton.setSelected(false);
    }

    @FXML private void optionalButtonClicked(ActionEvent e){
        this.MandatoryButton.setSelected(false);
    }

    @FXML private void menuButtonClicked(ActionEvent e){
        if(metallurgistMenuController!=null) {
            metallurgistMenuController.backToMenu();
        }
        else
            directorMenuController.backToMenu();
    }

    public void backToScene(){
        primaryStage.setScene(this.AdoptField.getScene());
    }

    @FXML private void nextButtonClicked(ActionEvent e){
        if(NameField.getText().isEmpty()||BrandField.getText().isEmpty()||AdoptField.getText().isEmpty()||AmountField.getText().isEmpty()||PriceField.getText().isEmpty()||!MandatoryButton.isSelected()&&!OptionalButton.isSelected()){

            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        } else {
            double adopt=-1;
            double amount=-1;
            double price=-1;
            boolean b=true;
            if(Component.componentExists(NameField.getText())){
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
                                            this.component = new Component(0, NameField.getText(),BrandField.getText(), adopt, amount, price, mandatory);

                                            try {
                                                FXMLLoader loader = new FXMLLoader(
                                                        getClass().getResource("../Views/AddElementsScene.fxml")
                                                );
                                                Parent root = loader.load();
                                                addElementsController = loader.getController();
                                                addElementsController.setPreviousController(this);
                                                addElementsController.init();
                                                if(metallurgistMenuController!=null)
                                                    primaryStage = metallurgistMenuController.primaryStage;
                                                else
                                                    primaryStage = directorMenuController.primaryStage;
                                                primaryStage.setScene(new Scene(root));
                                                primaryStage.setTitle("Новый компонент");
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

package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MetallurgistMenuController {

    @FXML public Button AddMeltButton;
    AddComponentController addComponentController;
    AddCharge1Controller addCharge1Controller;
    ShowComponentsController showComponentsController;
    ShowMeltsController showMeltsController;
    Stage primaryStage;

    public void init(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML private void addMeltButtonClicked(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge1Scene.fxml")
            );
            Parent root = loader.load();
            addCharge1Controller = loader.getController();
            addCharge1Controller.setMenuController(this);
            addCharge1Controller.init();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Новая плавка");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML private void showMeltsButtonClicked(ActionEvent e){

    }

    @FXML private void addComponentButtonClicked(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddComponentScene.fxml")
            );
            Parent root = loader.load();
            addComponentController = loader.getController();
            addComponentController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Новый компонент");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML private void showComponentsButtonClicked(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ShowComponentsScene.fxml")
            );
            Parent root = loader.load();
            showComponentsController = loader.getController();
            showComponentsController.setMenuController(this);
            showComponentsController.init();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Просмотр компонентов");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void backToMenu(){
        primaryStage.setScene(this.AddMeltButton.getScene());
        primaryStage.setTitle("Меню");
        primaryStage.show();
    }


}

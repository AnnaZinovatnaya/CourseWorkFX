package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by Анюта on 01.11.2016.
 */
public class MetallurgistMenuController {

    private MainController mainController;
    @FXML public Button AddMeltButton;
    AddComponentController addComponentController;
    AddMeltController addMeltController;
    ShowComponentsController showComponentsController;
    ShowMeltsController showMeltsController;
    Stage primaryStage;

    public void setMainController(MainController main){
        mainController=main;
        primaryStage = mainController.stage;
    }

    @FXML private void addMeltButtonClicked(ActionEvent e){

    }

    @FXML private void showMeltsButtonClicked(ActionEvent e){

    }

    @FXML private void addComponentButtonClicked(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../Views/AddComponentScene.fxml")
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

    }

    public void backToMenu(){
        primaryStage.setScene(this.AddMeltButton.getScene());
        primaryStage.setTitle("Меню");
        primaryStage.show();
    }


}

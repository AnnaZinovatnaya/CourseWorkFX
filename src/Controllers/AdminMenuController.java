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
public class AdminMenuController {
    @FXML private Button AddUserButton;

    private MainController mainController;
    Stage primaryStage;
    AddUserController addUserController;
    FindUserController findUserController;


    public void setMainController(MainController main){
        mainController=main;
        primaryStage = mainController.stage;
    }

    @FXML private void addUserButtonClicked(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../Views/AddUserScene.fxml")
            );
            Parent root = loader.load();
            addUserController = loader.getController();
            addUserController.setMenuController(this);
            addUserController.init();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Добавление пользователя");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML private void findUserButtonClicked(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../Views/FindUserScene.fxml")
            );
            Parent root = loader.load();
            findUserController = loader.getController();
            findUserController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Поиск пользователя");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void backToMenu(){
        primaryStage.setScene(this.AddUserButton.getScene());
        primaryStage.setTitle("Меню");
        primaryStage.show();
    }
}

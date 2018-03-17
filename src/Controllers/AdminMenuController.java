package Controllers;

import Models.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminMenuController {
    @FXML private Button AddUserButton;

    Stage primaryStage;

    public void init(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML private void addUserButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddUserScene.fxml")
            );
            Parent root = loader.load();

            UserController userController = loader.getController();
            userController.setMenuController(this);
            userController.init();

            userController.init();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Добавление пользователя");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @FXML private void findUserButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/FindUserScene.fxml")
            );
            Parent root = loader.load();

            UserController userController = loader.getController();
            userController.setMenuController(this);
            Register.setUserToNull();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Поиск пользователя");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void backToMenu()
    {
        primaryStage.setScene(this.AddUserButton.getScene());
        primaryStage.setTitle("Меню - Администратор");
        primaryStage.show();
    }
}

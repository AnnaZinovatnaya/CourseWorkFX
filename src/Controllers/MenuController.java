package Controllers;

import Models.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.ErrorMessage;

public class MenuController
{
    private Stage                    primaryStage;
    private String                   windowTitle;

    @FXML private Button             addUserButton;
    @FXML private Button             addMeltButton;
    @FXML private Button             reportButton;

    private AddComponentController   addComponentController;
    private AddCharge1Controller     addCharge1Controller;
    private ShowComponentsController showComponentsController;
    private ShowMeltsController      showMeltsController;
    private ReportController         reportController;
    private UserController           userController;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void init(Stage primaryStage, String windowTitle, UserController userController)
    {
        this.primaryStage = primaryStage;
        this.windowTitle = windowTitle;
        this.userController = userController;
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
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
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
            Manager.setUserToNull();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Поиск пользователя");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public void backToMenu()
    {
        if (null != this.addUserButton)
        {
            primaryStage.setScene(this.addUserButton.getScene());
        }
        else if (null != this.reportButton)
        {
            primaryStage.setScene(this.reportButton.getScene());
        }
        else if (null != this.addMeltButton)
        {
            primaryStage.setScene(this.addMeltButton.getScene());
        }

        primaryStage.setTitle(windowTitle);
        primaryStage.show();
    }

    @FXML private void addComponentButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/AddComponentScene.fxml")
            );
            Parent root = loader.load();
            addComponentController = loader.getController();
            addComponentController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Новый компонент");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML private void showComponentsButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/ShowComponentsScene.fxml")
            );
            Parent root = loader.load();
            ShowComponentsController showComponentsController = loader.getController();
            showComponentsController.setMenuController(this);
            showComponentsController.init();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Просмотр компонентов");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML private void reportButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/ReportScene.fxml")
            );
            Parent root = loader.load();
            reportController = loader.getController();
            reportController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Отчет по плавкам");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML private void addMeltButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/AddCharge1Scene.fxml")
            );
            Parent root = loader.load();
            addCharge1Controller = loader.getController();
            addCharge1Controller.init(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Новая плавка");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML private void showMeltsButtonClicked()
    {

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML public void logoutButtonClicked()
    {
        this.userController.backToScene();
    }
}

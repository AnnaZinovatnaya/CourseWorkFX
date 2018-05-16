package Controllers;

import Models.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Message;

public class MenuController
{
    private Stage                    primaryStage;
    private String menuWindowTitle;

    @FXML private Button             addUserButton;
    @FXML private Button             addMeltButton;
    @FXML private Button             reportButton;
    @FXML private Button             showMeltsButton;

    private UserController           userController;

    public void init(Stage primaryStage, String menuWindowTitle, UserController userController)
    {
        this.primaryStage    = primaryStage;
        this.menuWindowTitle = menuWindowTitle;
        this.userController  = userController;

        if (this.showMeltsButton != null)
        {
            this.showMeltsButton.setDisable(true);
        }
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
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Добавление пользователя");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
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
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
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

        primaryStage.setTitle(menuWindowTitle);
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
            AddComponentController addComponentController = loader.getController();
            addComponentController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Новый компонент");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
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
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
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
            ReportController reportController = loader.getController();
            reportController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Отчет по плавкам");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
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
            AddCharge1Controller addCharge1Controller = loader.getController();
            addCharge1Controller.init(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Расчет шихты");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    @FXML private void showMeltsButtonClicked()
    {
        //TODO
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML public void logoutButtonClicked()
    {
        this.userController.backToScene();
    }

    @FXML private void addMeltBrandButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddMeltBrandScene.fxml")
            );
            Parent root = loader.load();
            AddMeltBrandController addMeltBrandController = loader.getController();
            addMeltBrandController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Добавление марки");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    @FXML private void showMeltBrandsButtonClicked()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ShowMeltBrandsScene.fxml")
            );
            Parent root = loader.load();
            ShowMeltBrandsController showMeltBrandsController = loader.getController();
            showMeltBrandsController.init();
            showMeltBrandsController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Просмотр марок");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }
}

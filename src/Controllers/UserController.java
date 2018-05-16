package Controllers;

import Models.Manager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Message;

public class UserController
{
    @FXML private AnchorPane        primaryPane;
    private Stage                   primaryStage;

    @FXML private TextField         loginName;
    @FXML private TextField         loginLastname;
    @FXML private TextField         loginPassword;

    @FXML private TextField         nameToAdd;
    @FXML private TextField         lastnameToAdd;
    @FXML private TextField         passwordToAdd;
    @FXML private ChoiceBox<String> roles;

    @FXML private TextField         nameToSearch;
    @FXML private TextField         lastnameToSearch;
    @FXML private TextArea          searchResult;

    private MenuController          menuController;

    public void init(Stage primaryStage)
    {
        this.primaryStage = primaryStage;

        this.primaryPane.setOnKeyPressed(key ->
        {
            if (key.getCode() == KeyCode.ENTER)
            {
                loginButtonClicked();
            }
        });
    }

    @FXML public void init()
    {
        this.roles.setItems(FXCollections.observableArrayList("","руководитель",  "металлург", "плавильщик"));
    }

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
    }

    @FXML private void menuButtonClicked()
    {
        this.menuController.backToMenu();
    }

    @FXML private void loginButtonClicked()
    {
        if(this.loginName.getText().isEmpty()     ||
           this.loginLastname.getText().isEmpty() ||
           this.loginPassword.getText().isEmpty())
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
        }
        else
        {
            login();
        }
    }

    private void login()
    {
        boolean isLoginSuccessful = false;

        try
        {
            isLoginSuccessful = Manager.login(this.loginName.getText(),
                    this.loginLastname.getText(),
                    this.loginPassword.getText());

            if(!isLoginSuccessful)
            {
                Message.showErrorMessage(ErrorMessage.WRONG_LOGIN_OR_PASSWORD);
                return;
            }

            loadMenu();
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }

    private void loadMenu()
    {
        switch (Manager.getRole())
        {
            case "администратор":
                loadAdminMenu();
                break;
            case "металлург":
                loadMetallurgistMenu();
                break;
            case "плавильщик":
                loadMakeMeltScene();
                break;
            case "руководитель":
                loadDirectorMenu();
                break;
        }
    }

    private void loadAdminMenu()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AdminMenuScene.fxml")
            );
            Parent root = loader.load();
            MenuController menuController = loader.getController();
            menuController.init(this.primaryStage, "Меню - Администратор", this);
            this.primaryStage.setTitle("Меню - Администратор");
            this.primaryStage.setScene(new Scene(root));
            this.primaryStage.show();
        }
        catch (Exception e)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    private void loadMetallurgistMenu()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/MetallurgistMenuScene.fxml")
            );
            Parent root = loader.load();
            MenuController menuController = loader.getController();
            menuController.init(this.primaryStage, "Меню - Металлург", this);
            this.primaryStage.setTitle("Меню - Металлург");
            this.primaryStage.setScene(new Scene(root));
            this.primaryStage.show();
        }
        catch (Exception e)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    private void loadMakeMeltScene()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/MakeMelt1Scene.fxml")
            );
            Parent root = loader.load();
            Melt1Controller melt1Controller = loader.getController();
            melt1Controller.init(this.primaryStage, this);
            this.primaryStage.setTitle("Выполнение плавки");
            this.primaryStage.setScene(new Scene(root));
            this.primaryStage.show();
        }
        catch (Exception e)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    private void loadDirectorMenu()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/DirectorMenuScene.fxml")
            );
            Parent root = loader.load();
            MenuController menuController = loader.getController();
            menuController.init(this.primaryStage, "Меню - Руководитель", this);
            this.primaryStage.setTitle("Меню - Руководитель");
            this.primaryStage.setScene(new Scene(root));
            this.primaryStage.show();
        }
        catch (Exception e)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    @FXML private void addUserButtonClicked()
    {
        if (this.nameToAdd.getText().isEmpty()     ||
            this.lastnameToAdd.getText().isEmpty() ||
            this.passwordToAdd.getText().isEmpty() ||
            this.roles.getValue().isEmpty())
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
            return;
        }

        if (!userExists())
        {
            addNewUser();
            Message.showInformationMessage("Пользователь добавлен!");
            clearAddUserFields();
        }
        else
        {
            Message.showErrorMessage(ErrorMessage.USER_ALREADY_EXISTS);
        }
    }

    private boolean userExists()
    {
        try
        {
            if (Manager.userExists(this.nameToAdd.getText(), this.lastnameToAdd.getText()))
            {
                return true;
            }
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }

        return false;
    }

    private void addNewUser()
    {
        try
        {
            Manager.saveNewUser(this.nameToAdd.getText(),
                                this.lastnameToAdd.getText(),
                                this.passwordToAdd.getText(),
                                this.roles.getValue());
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }

    private void clearAddUserFields()
    {
        this.nameToAdd.setText("");
        this.lastnameToAdd.setText("");
        this.passwordToAdd.setText("");
        this.roles.setValue("");
    }

    @FXML private void searchButtonClicked()
    {
        if (this.nameToSearch.getText().isEmpty() || this.lastnameToSearch.getText().isEmpty())
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
        }
        else
        {
            if (isUserFound())
            {
                this.searchResult.setText("Имя - "     + Manager.getFoundName()     + "\n" +
                                          "Фамилия - " + Manager.getFoundLastname() + "\n" +
                                          "Пароль - "  + Manager.getFoundPassword() + "\n" +
                                          "Роль - "    + Manager.getFoundRole());
            }
            else
            {
                this.searchResult.setText(ErrorMessage.USER_NOT_FOUND);
            }
        }
    }

    private boolean isUserFound()
    {
        boolean result = false;

        try
        {
            result = Manager.findUser(nameToSearch.getText(), lastnameToSearch.getText());
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }

        return result;
    }

    @FXML private void deleteButtonClicked()
    {
        if (Manager.getUser() == null)
        {
            this.searchResult.setText(ErrorMessage.EMPTY_USER_CHOICE);
            return;
        }

        if (Manager.isUserDefaultAdmin())
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_DELETE_ADMIN);
            return;
        }

        loadDeletionConfirmation();
    }

    private void loadDeletionConfirmation()
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("Удаление пользователя");
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/DeleteUserScene.fxml")
            );

            Parent root = loader.load();
            DeleteUserController deleteUserController = loader.getController();
            deleteUserController.setUserController(this);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.lastnameToSearch.getScene().getWindow());
            stage.showAndWait();
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    public TextArea getSearchResult()
    {
        return searchResult;
    }

    public void backToScene()
    {
        Manager.logout();

        this.loginName.setText("");
        this.loginLastname.setText("");
        this.loginPassword.setText("");
        this.primaryStage.setTitle("Вход в систему");
        this.primaryStage.setScene(this.loginLastname.getScene());
    }
}

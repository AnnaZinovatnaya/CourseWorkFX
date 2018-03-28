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
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ErrorMessage;

public class UserController
{
    private Alert                   alert = new Alert(Alert.AlertType.ERROR);

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
        menuController.backToMenu();
    }

    @FXML private void loginButtonClicked()
    {

        if(this.loginName.getText().isEmpty()     ||
           this.loginLastname.getText().isEmpty() ||
           this.loginPassword.getText().isEmpty())
        {
            showErrorMessage(ErrorMessage.EMPTY_FIELDS);
        }
        else
        {
            boolean isLoginSuccessful;
            try
            {
                isLoginSuccessful = Manager.login(loginName.getText(),
                                                  loginLastname.getText(),
                                                  loginPassword.getText());
                if(!isLoginSuccessful)
                {
                    showErrorMessage(ErrorMessage.WRONG_LOGIN_OR_PASSWORD);
                }
                else
                {
                    try
                    {
                        if (Manager.getRole().equals("администратор"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/Views/AdminMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            MenuController menuController = loader.getController();
                            menuController.init(primaryStage, "Меню - Администратор", this);
                            this.primaryStage.setTitle("Меню - Администратор");
                            primaryStage.setScene(new Scene(root));
                        }
                        else if (Manager.getRole().equals("металлург"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/Views/MetallurgistMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            MenuController menuController = loader.getController();
                            menuController.init(primaryStage, "Меню - Металлург", this);
                            this.primaryStage.setTitle("Меню - Металлург");
                            primaryStage.setScene(new Scene(root));
                        }
                        else if (Manager.getRole().equals("плавильщик"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/Views/MakeMelt1Scene.fxml")
                            );
                            Parent root = loader.load();
                            Melt1Controller melt1Controller = loader.getController();
                            melt1Controller.init(primaryStage, this);
                            this.primaryStage.setTitle("Выполнение плавки");
                            primaryStage.setScene(new Scene(root));
                        }
                        else if (Manager.getRole().equals("руководитель"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/Views/DirectorMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            MenuController menuController = loader.getController();
                            menuController.init(primaryStage, "Меню - Руководитель", this);
                            this.primaryStage.setTitle("Меню - Руководитель");
                            primaryStage.setScene(new Scene(root));
                        }
                    }
                    catch (Exception ex)
                    {
                        showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
                    }

                    primaryStage.show();
                }
            }
            catch (RuntimeException e)
            {
                showErrorMessage(e.getLocalizedMessage());
            }
        }
    }

    @FXML private void addUserButtonClicked()
    {
        if(nameToAdd.getText().isEmpty()     ||
                lastnameToAdd.getText().isEmpty() ||
                passwordToAdd.getText().isEmpty() ||
                roles.getValue().isEmpty())
        {
            showErrorMessage(ErrorMessage.EMPTY_FIELDS);

        }
        else
        {
            Manager.newUser();
            if(Manager.setNameLastname(nameToAdd.getText(), lastnameToAdd.getText()))
            {
                Manager.setPasswordRole(passwordToAdd.getText(), roles.getValue());
                try
                {
                    Manager.saveUser();

                    showInformationMessage("Пользователь добавлен!");

                    this.nameToAdd.setText("");
                    this.lastnameToAdd.setText("");
                    this.passwordToAdd.setText("");
                    this.roles.setValue("");
                }
                catch (RuntimeException e)
                {
                    showErrorMessage(e.getLocalizedMessage());
                }
            }
            else
            {
                showErrorMessage(ErrorMessage.USER_ALREADY_EXISTS);
            }
        }
    }

    @FXML private void searchButtonClicked()
    {
        if(nameToSearch.getText().isEmpty() || lastnameToSearch.getText().isEmpty())
        {
            showErrorMessage(ErrorMessage.EMPTY_FIELDS);
        }
        else
        {
            try {
                if(Manager.findUser(nameToSearch.getText(), lastnameToSearch.getText()))
                {
                    this.searchResult.setText("Имя - " + Manager.getFoundName() + "\nФамилия - " +
                            Manager.getFoundLastname() + "\nПароль - " + Manager.getFoundPassword() +
                            "\nРоль - " + Manager.getFoundRole());
                }
                else
                {
                    this.searchResult.setText(ErrorMessage.USER_DOES_NOT_EXIST);
                }
            }
            catch (RuntimeException e)
            {
                showErrorMessage(e.getLocalizedMessage());
            }
        }
    }

    @FXML private void deleteButtonClicked()
    {
        if(Manager.canDelete())
        {
            if (!Manager.isDefaultAdmin()) {
                try {
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
                } catch (Exception ex) {
                    showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
                }
            }
            else
            {
                showErrorMessage(ErrorMessage.CANNOT_DELETE_ADMIN);
            }
        }
        else
        {
            this.searchResult.setText(ErrorMessage.EMPTY_USER_CHOICE);
        }
    }

    public TextArea getSearchResult() {
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

    private void showErrorMessage(String message)
    {
        this.alert.setAlertType(Alert.AlertType.ERROR);
        this.alert.setContentText(message);
        this.alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label)node).setFont(Font.font(16)));
        this.alert.showAndWait();
    }

    private void showInformationMessage(String message)
    {
        this.alert.setAlertType(Alert.AlertType.INFORMATION);
        this.alert.setTitle("Information");
        this.alert.setHeaderText("Information");
        this.alert.setContentText(message);
        this.alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label)node).setFont(Font.font(16)));
        this.alert.showAndWait();
    }
}

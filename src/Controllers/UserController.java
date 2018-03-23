package Controllers;

import Models.Manager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ErrorMessage;

public class UserController
{
    private Alert                   alert = new Alert(Alert.AlertType.ERROR);

    @FXML private Pane              primaryPane;
    private Stage                   primaryStage;

    @FXML private TextField         loginNameField;
    @FXML private TextField         loginLastnameField;
    @FXML private TextField         loginPasswordField;
    @FXML private TextField         addNameField;
    @FXML private TextField         addLastnameField;
    @FXML private TextField         addPasswordField;
    @FXML private ChoiceBox<String> roleChoiceBox;
    @FXML private TextField         searchNameField;
    @FXML private TextField         searchLastnameField;
    @FXML private TextArea          resultArea;

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
        this.roleChoiceBox.setItems(FXCollections.observableArrayList("","руководитель",  "металлург", "плавильщик"));
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

        if(this.loginNameField.getText().isEmpty() ||
           this.loginLastnameField.getText().isEmpty() ||
           this.loginPasswordField.getText().isEmpty())
        {
            alert.setContentText(ErrorMessage.EMPTY_FIELDS);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
        else
        {
            boolean isLoginSuccessful;
            try
            {
                isLoginSuccessful = Manager.login(loginNameField.getText(),
                                                  loginLastnameField.getText(),
                                                  loginPasswordField.getText());
                if(!isLoginSuccessful)
                {
                    alert.setContentText(ErrorMessage.WRONG_LOGIN_OR_PASSWORD);
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                            .forEach(node -> ((Label)node).setFont(Font.font(16)));
                    alert.showAndWait();
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
                            MeltController meltController = loader.getController();
                            meltController.init(primaryStage);
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
                        alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                .forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();
                    }

                    primaryStage.show();
                }
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
    }

    @FXML private void addUserButtonClicked()
    {
        if(addNameField.getText().isEmpty()     ||
                addLastnameField.getText().isEmpty() ||
                addPasswordField.getText().isEmpty() ||
                roleChoiceBox.getValue().isEmpty())
        {
            alert.setContentText(ErrorMessage.EMPTY_FIELDS);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        }
        else
        {
            Manager.newUser();
            if(Manager.setNameLastname(addNameField.getText(), addLastnameField.getText()))
            {
                Manager.setPasswordRole(addPasswordField.getText(), roleChoiceBox.getValue());
                try {
                    Manager.saveUser();

                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Information");
                    alert.setContentText("Пользователь добавлен!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                            .forEach(node -> ((Label)node).setFont(Font.font(16)));
                    alert.showAndWait();
                    alert.setAlertType(Alert.AlertType.ERROR);

                    this.addNameField.setText("");
                    this.addLastnameField.setText("");
                    this.addPasswordField.setText("");
                    this.roleChoiceBox.setValue("");
                }
                catch (RuntimeException e) {
                    alert.setContentText(e.getLocalizedMessage());
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                            .forEach(node -> ((Label) node).setFont(Font.font(16)));
                    alert.showAndWait();
                }
            }
            else
            {
                alert.setContentText(ErrorMessage.USER_ALREADY_EXISTS);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
    }

    @FXML private void searchButtonClicked()
    {
        if(searchNameField.getText().isEmpty() || searchLastnameField.getText().isEmpty())
        {
            alert.setContentText(ErrorMessage.EMPTY_FIELDS);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        }
        else
        {
            try {
                if(Manager.findUser(searchNameField.getText(), searchLastnameField.getText()))
                {
                    this.resultArea.setText("Имя - " + Manager.getFoundName() + "\nФамилия - " +
                            Manager.getFoundLastname() + "\nПароль - " + Manager.getFoundPassword() +
                            "\nРоль - " + Manager.getFoundRole());
                }
                else
                {
                    this.resultArea.setText(ErrorMessage.USER_DOES_NOT_EXIST);
                }
            }
            catch (RuntimeException e)
            {
                alert.setContentText(e.getLocalizedMessage());
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
    }

    @FXML private void deleteButtonClicked()
    {
        if(Manager.canDelete())
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
                stage.initOwner(this.searchLastnameField.getScene().getWindow());
                stage.showAndWait();
            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
        else
        {
            this.resultArea.setText(ErrorMessage.EMPTY_USER_CHOICE);
        }
    }

    public TextArea getResultArea() {
        return resultArea;
    }

    public void backToScene()
    {
        Manager.logout();
        this.loginNameField.setText("");
        this.loginLastnameField.setText("");
        this.loginPasswordField.setText("");
        this.primaryStage.setTitle("Вход в систему");
        this.primaryStage.setScene(this.loginLastnameField.getScene());
    }
}

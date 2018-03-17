package Controllers;

import Models.Register;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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

public class UserController {
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML private Pane PrimaryPane;
    Stage primaryStage;

    @FXML private TextField LoginNameField;
    @FXML private TextField LoginLastnameField;
    @FXML private javafx.scene.control.PasswordField LoginPasswordField;

    @FXML private TextField AddNameField;
    @FXML private TextField AddLastnameField;
    @FXML private TextField AddPasswordField;
    @FXML private ChoiceBox<String> RoleBox;

    @FXML private TextField SearchNameField;
    @FXML private TextField SearchLastnameField;
    @FXML public TextArea ResultArea;

    AdminMenuController adminMenuController;

    public void init(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.PrimaryPane.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER)
            {
                LoginButtonClicked();
            }
        });
    }

    @FXML public void init()
    {
        this.RoleBox.setItems(FXCollections.observableArrayList("","руководитель",  "металлург", "плавильщик"));
    }

    public void setMenuController(AdminMenuController adminMenuController)
    {
        this.adminMenuController = adminMenuController;
    }

    @FXML private void menuButtonClicked(){
        adminMenuController.backToMenu();
    }

    @FXML private void LoginButtonClicked(){

        if(this.LoginNameField.getText().isEmpty() ||
           this.LoginLastnameField.getText().isEmpty() ||
           this.LoginPasswordField.getText().isEmpty())
        {
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
        else
        {
            boolean isLoginSuccessful = false;
            try
            {
                isLoginSuccessful = Register.login(LoginNameField.getText(),
                                                   LoginLastnameField.getText(),
                                                   LoginPasswordField.getText());

                if(false == isLoginSuccessful)
                {
                    alert.setContentText("Неверное имя или пароль!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                            .forEach(node -> ((Label)node).setFont(Font.font(16)));
                    alert.showAndWait();
                } else
                {
                    try
                    {
                        if (Register.getRole().equals("администратор"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/AdminMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            AdminMenuController adminMenuController = loader.getController();
                            adminMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Администратор");

                        }
                        else if (Register.getRole().equals("металлург"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/MetallurgistMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            MetallurgistMenuController metallurgistMenuController = loader.getController();
                            metallurgistMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Металлург");

                        }
                        else if (Register.getRole().equals("плавильщик"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/FounderMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            FounderMenuController founderMenuController = loader.getController();
                            founderMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Плавильщик");
                        }
                        else if (Register.getRole().equals("руководитель"))
                        {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/DirectorMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            DirectorMenuController directorMenuController = loader.getController();
                            directorMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Руководитель");
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
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
        if(AddNameField.getText().isEmpty()     ||
           AddLastnameField.getText().isEmpty() ||
           AddPasswordField.getText().isEmpty() ||
           RoleBox.getValue().isEmpty())
        {
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        }
        else
        {
            Register.newUser();
            if(Register.setNameLastname(AddNameField.getText(), AddLastnameField.getText())){
                Register.setPasswordRole(AddPasswordField.getText(), RoleBox.getValue());
                Register.saveUser();

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Information");
                alert.setContentText("Пользователь добавлен!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                alert.setAlertType(Alert.AlertType.ERROR);

                this.AddNameField.setText("");
                this.AddLastnameField.setText("");
                this.AddPasswordField.setText("");
                this.RoleBox.setValue("");
            } else{
                alert.setContentText("Пользователь с таким именем уже есть!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
    }

    @FXML private void searchButtonClicked()
    {
        if(SearchNameField.getText().isEmpty() || SearchLastnameField.getText().isEmpty())
        {
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        }
        else
        {
            if(Register.findUser(SearchNameField.getText(), SearchLastnameField.getText()))
            {
                this.ResultArea.setText("Имя - " + Register.getFoundName() + "\nФамилия - " +
                        Register.getFoundLastname() + "\nПароль - " + Register.getFoundPassword() +
                        "\nРоль - " + Register.getFoundRole());
            }
            else
            {
                this.ResultArea.setText("Такого пользователя нет!");
            }
        }
    }

    @FXML private void deleteButtonClicked()
    {
        if(true == Register.canDelete())
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
                deleteUserController.userController=this;
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.SearchLastnameField.getScene().getWindow());
                stage.showAndWait();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            this.ResultArea.setText("Выберите пользователя для удаления!");
        }
    }
}

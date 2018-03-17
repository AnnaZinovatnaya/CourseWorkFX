package Controllers;

import Models.Register;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginController{

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    private TextField NameField;
    @FXML
    private TextField LastnameField;
    @FXML
    private PasswordField PasswordField;

    @FXML private Pane PrimaryPane;
    Stage primaryStage;

    public void init(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.PrimaryPane.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                LoginButtonClicked();
            }

        });
    }


    @FXML private void LoginButtonClicked(){

        if(NameField.getText().isEmpty()||LastnameField.getText().isEmpty()||PasswordField.getText().isEmpty()){
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        } else {
            boolean isLoginSuccessful = false;
            try
            {
                isLoginSuccessful = Register.login(NameField.getText(), LastnameField.getText(), PasswordField.getText());

                if(false == isLoginSuccessful){
                    alert.setContentText("Неверное имя или пароль!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                    alert.showAndWait();
                } else{
                    try {
                        if (Register.getRole().equals("администратор")) {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/AdminMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            AdminMenuController adminMenuController = loader.getController();
                            adminMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Администратор");

                        } else if (Register.getRole().equals("металлург")) {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/MetallurgistMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            MetallurgistMenuController metallurgistMenuController = loader.getController();
                            metallurgistMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Металлург");

                        } else if (Register.getRole().equals("плавильщик")) {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/Views/FounderMenuScene.fxml")
                            );
                            Parent root = loader.load();
                            FounderMenuController founderMenuController = loader.getController();
                            founderMenuController.init(primaryStage);
                            primaryStage.setScene(new Scene(root));
                            primaryStage.setTitle("Меню - Плавильщик");
                        } else if (Register.getRole().equals("руководитель")) {
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
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }

        }
    }

}

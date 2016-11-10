package Controllers;

import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

public class LoginController{

    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private MainController mainController;
    @FXML
    private Button LoginButton;
    @FXML
    private TextField NameField;
    @FXML
    private TextField LastnameField;
    @FXML
    private PasswordField PasswordField;

    private User user;

    @FXML private void LoginButtonClicked(ActionEvent e){

        if(NameField.getText().isEmpty()||LastnameField.getText().isEmpty()||PasswordField.getText().isEmpty()){
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        } else {

            user = User.login(NameField.getText(), LastnameField.getText(), PasswordField.getText());
            if(user==null){
                alert.setContentText("Неверное имя или пароль!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();

            } else{
                mainController.setUser(user);
            }
        }
    }

    public void setMainController(MainController main){
        mainController=main;
    }

}

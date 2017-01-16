package Controllers;

import Models.Register;
import Models.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import util.DBUtil;
import java.sql.ResultSet;


public class AddUserController {

    AdminMenuController adminMenuController;
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML private TextField NameField;
    @FXML private TextField LastnameField;
    @FXML private TextField PasswordField;
    @FXML private ChoiceBox<String> RoleBox;

    public void setMenuController(AdminMenuController adminMenuController){
        this.adminMenuController = adminMenuController;
    }

    @FXML public void init(){
        this.RoleBox.setItems(FXCollections.observableArrayList("","руководитель",  "металлург", "плавильщик"));
    }

    @FXML private void menuButtonClicked(ActionEvent e){
        adminMenuController.backToMenu();
    }

    @FXML private void addUserButtonClicked(ActionEvent e){
        if(NameField.getText().isEmpty()||LastnameField.getText().isEmpty()||PasswordField.getText().isEmpty()||RoleBox.getValue().isEmpty()){

            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        } else {

            Register.newUser();
            if(Register.setNameLastname(NameField.getText(), LastnameField.getText())){
                Register.setPasswordRole(PasswordField.getText(), RoleBox.getValue());
                Register.saveUser();

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Information");
                alert.setContentText("Пользователь добавлен!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                alert.setAlertType(Alert.AlertType.ERROR);

                this.NameField.setText("");
                this.LastnameField.setText("");
                this.PasswordField.setText("");
                this.RoleBox.setValue("");
            } else{
                alert.setContentText("Пользователь с таким именем уже есть!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
    }

}

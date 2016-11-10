package Controllers;

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
    @FXML private Button MenuButton;
    @FXML private Button AddUserButton;

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
            User user = new User(0, NameField.getText(), LastnameField.getText(), PasswordField.getText(), RoleBox.getValue());
            int n = user.addUser();
            if(n == 0){
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
            } else if(n == 1) {
                alert.setContentText("Пользователь с таким именем уже есть!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();

            } else {
                alert.setContentText("Неизвестная ошибка!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();

            }
        }
    }

}

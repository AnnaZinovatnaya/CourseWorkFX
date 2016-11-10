package Controllers;

import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DBUtil;

import java.sql.ResultSet;

/**
 * Created by Анюта on 01.11.2016.
 */
public class FindUserController {
    @FXML private TextField NameField;

    @FXML private TextField LastnameField;

    @FXML public TextArea ResultArea;

    AdminMenuController adminMenuController;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private User user;

    public void setMenuController(AdminMenuController adminMenuController){
        this.adminMenuController = adminMenuController;
    }


    @FXML private void menuButtonClicked(ActionEvent e){
        adminMenuController.backToMenu();
    }

    @FXML private void searchButtonClicked(ActionEvent e){
        if(NameField.getText().isEmpty()||LastnameField.getText().isEmpty()){

            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        } else {

            user = User.findUser(NameField.getText(), LastnameField.getText());
            if(user==null){
                this.ResultArea.setText("Такого пользователя нет!");
            } else{
                this.ResultArea.setText("Имя - "+user.getName()+"\nФамилия - "+user.getLastname()+"\nПароль - "+user.getPassword()+"\nРоль - "+user.getRole());
            }
        }
    }

    @FXML private void deleteButtonClicked(ActionEvent e){

        if(user!=null) {
            try {
                Stage stage = new Stage();
                stage.setTitle("Удаление пользователя");
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("../Views/DeleteUserScene.fxml")
                );

                Parent root = loader.load();
                DeleteUserController deleteUserController = loader.getController();
                deleteUserController.user=user;
                deleteUserController.findUserController=this;
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.LastnameField.getScene().getWindow());
                stage.showAndWait();

            } catch (Exception ex){}
        }
        else
            this.ResultArea.setText("Выберите пользователя для удаления!");
    }


}

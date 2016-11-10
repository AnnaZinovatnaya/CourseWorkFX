package Controllers;

import Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.DBUtil;


/**
 * Created by Анюта on 01.11.2016.
 */
public class DeleteUserController {

    public User user;
    FindUserController findUserController;

    @FXML private void deleteButtonClicked(ActionEvent e){
        Stage stage = new Stage();
        if(user.deleteUser()){
            findUserController.ResultArea.appendText("\nПользователь удален!");

            stage = (Stage)((Button) e.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML private void cancelButtonClicked(ActionEvent e){
        Stage stage = new Stage();
        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();
    }
}

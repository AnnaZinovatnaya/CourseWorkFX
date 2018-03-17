package Controllers;

import Models.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteUserController {

    UserController userController;

    @FXML private void deleteButtonClicked(ActionEvent e){
        Stage stage;
        Register.deleteUser();
        userController.ResultArea.appendText("\nПользователь удален!");

        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML private void cancelButtonClicked(ActionEvent e){
        Stage stage;
        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();
    }
}

package Controllers;

import Models.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteUserController
{
    private UserController userController;

    @FXML private void deleteButtonClicked(ActionEvent e)
    {
        Stage stage;
        Manager.deleteUser();
        userController.resultArea.appendText("\nПользователь удален!");

        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML private void cancelButtonClicked(ActionEvent e)
    {
        Stage stage;
        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}

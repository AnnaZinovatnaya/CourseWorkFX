package Controllers;

import Models.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DeleteUserController
{
    private UserController userController;
    private Alert          alert = new Alert(Alert.AlertType.ERROR);

    @FXML private void deleteButtonClicked(ActionEvent e)
    {
        Stage stage;
        try
        {
            Manager.deleteUser();
            userController.getSearchResult().appendText("\nПользователь удален!");
        }
        catch (RuntimeException ex)
        {
            alert.setContentText(ex.getLocalizedMessage());
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }

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

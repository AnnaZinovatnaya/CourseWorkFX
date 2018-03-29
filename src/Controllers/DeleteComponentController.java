package Controllers;

import Models.Manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.Helper;

public class DeleteComponentController
{
    private ShowComponentController showComponentController;
    private String                  componentName;

    public void init(String temp)
    {
        this.componentName = temp;
    }

    @FXML private void deleteButtonClicked(ActionEvent e)
    {
        Stage stage;
        try
        {
            Manager.deleteComponent(componentName);

            Helper.showInformationMessage("Компонент удален!");

            this.showComponentController.setDeleted(true);
        }
        catch (RuntimeException ex)
        {
            Helper.showErrorMessage(ex.getLocalizedMessage());
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

    public void setShowComponentController(ShowComponentController showComponentController) {
        this.showComponentController = showComponentController;
    }
}

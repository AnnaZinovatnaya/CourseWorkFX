package Controllers;

import Models.MeltBrand;
import Util.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteMeltBrandController {
    private ShowMeltBrandController showMeltBrandController;
    private MeltBrand meltBrand;

    public void init(MeltBrand meltBrand)
    {
        this.meltBrand = meltBrand;
    }

    @FXML
    private void deleteButtonClicked(ActionEvent e)
    {
        Stage stage;
        try
        {
            meltBrand.deleteFromDB();

            Message.showInformationMessage("Марка удалена!");

            this.showMeltBrandController.setDeleted(true);
        }
        catch (RuntimeException ex)
        {
            Message.showErrorMessage(ex.getLocalizedMessage());
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

    public void setShowMeltBrandController(ShowMeltBrandController showMeltBrandController) {
        this.showMeltBrandController = showMeltBrandController;
    }
}

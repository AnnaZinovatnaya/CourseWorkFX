package Controllers;

import Models.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Helper;

public class AddCharge1Controller
{
    private MenuController          menuController;
    private Stage                   primaryStage;

    @FXML private ChoiceBox<String> brandChoiceBox;
    @FXML private TextField         massField;
    @FXML private TextField         deltaMassField;

    public void init(MenuController menuController)
    {
        this.menuController = menuController;
        this.primaryStage = this.menuController.getPrimaryStage();

        ObservableList<String> brandNames = FXCollections.observableArrayList("");
        try
        {
            brandNames.addAll(Manager.getAllBrands());
            this.brandChoiceBox.setItems(brandNames);
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }

        this.brandChoiceBox.setValue("");
    }

    @FXML public void menuButtonClicked()
    {
        menuController.backToMenu();
    }

    @FXML public void nextButtonClicked()
    {
        double mass = 0;
        double deltaMass = 0;

        if (this.brandChoiceBox.getValue().isEmpty() ||
            this.massField.getText().isEmpty()       ||
            this.deltaMassField.getText().isEmpty())
        {
            Helper.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
            return;
        }

        try
        {
            mass = Double.parseDouble(this.massField.getText());

            if (mass <= 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_MASS);
            }
        }
        catch (Exception ex)
        {
            Helper.showErrorMessage(ErrorMessage.INCORRECT_MASS);
            return;
        }

        try
        {
            deltaMass = Double.parseDouble(this.deltaMassField.getText());

            if (deltaMass <= 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_DELTA_MASS);
            }
        }
        catch (Exception ex)
        {
            Helper.showErrorMessage(ErrorMessage.INCORRECT_DELTA_MASS);
            return;
        }

        Manager.createNewCharge(mass, deltaMass, this.brandChoiceBox.getValue());

        loadAddCharge2Scene();
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.brandChoiceBox.getScene());
    }

    public Stage getPrimaryStage()
    {
        return this.primaryStage;
    }

    private void loadAddCharge2Scene()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge2Scene.fxml")
            );
            Parent root = loader.load();
            AddCharge2Controller addCharge2Controller = loader.getController();
            addCharge2Controller.init(this);
            this.primaryStage.setScene(new Scene(root));
        }
        catch (Exception ex)
        {
            Helper.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }
}



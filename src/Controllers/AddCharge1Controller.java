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
import util.ErrorMessage;
import util.Helper;

public class AddCharge1Controller
{
    private MenuController          menuController;
    private Stage                   primaryStage;

    @FXML private ChoiceBox<String> brandChoiceBox;
    @FXML private TextField         massField;
    @FXML private TextField         deltaMassField;
    private ObservableList<String>  brandNames;

    public void init(MenuController menuController)
    {
        this.menuController = menuController;
        this.primaryStage = this.menuController.getPrimaryStage();

        this.brandNames = FXCollections.observableArrayList ("");
        try
        {
            this.brandNames.addAll(Manager.getAllBrands());
            this.brandChoiceBox.setItems(this.brandNames);
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
        boolean b = true;
        double mass = 0;
        double deltaMass = 0;
        if(brandChoiceBox.getValue().isEmpty() || massField.getText().isEmpty() || deltaMassField.getText().isEmpty())
        {
            Helper.showErrorMessage(ErrorMessage.EMPTY_FIELDS);

            b = false;
        }
        if(b)
        {
            try
            {
                mass = Double.parseDouble(massField.getText());

                if (mass <= 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_DELTA_MASS);
                }
            }
            catch (Exception ex)
            {
                Helper.showErrorMessage(ErrorMessage.INCORRECT_MASS);

                b = false;
            }
        }
        if(b)
        {
            try
            {
                deltaMass = Double.parseDouble(deltaMassField.getText());

                if (deltaMass <= 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_DELTA_MASS);
                }
            }
            catch (Exception ex)
            {
                Helper.showErrorMessage(ErrorMessage.INCORRECT_DELTA_MASS);

                b = false;
            }
        }
        if(b)
        {
            Manager.newCharge();
            Manager.setChargeBrand(brandChoiceBox.getValue());
            Manager.setChargeMassAndDelta(mass, deltaMass);

            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge2Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge2Controller addCharge2Controller = loader.getController();
                addCharge2Controller.setAddCharge1Controller(this);
                addCharge2Controller.init();
                primaryStage.setScene(new Scene(root));
            }
            catch (Exception ex)
            {
                Helper.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
            }
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.brandChoiceBox.getScene());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}



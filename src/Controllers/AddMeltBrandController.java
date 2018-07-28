package Controllers;

import Models.Element;
import Models.MeltBrand;
import Util.ErrorMessage;
import Util.Message;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class AddMeltBrandController
{
    @FXML private TextField nameField;
    @FXML private TextField standardField;
    @FXML private TextField cMinPercentField;
    @FXML private TextField cMaxPercentField;
    @FXML private TextField siMinPercentField;
    @FXML private TextField siMaxPercentField;
    @FXML private TextField sMinPercentField;
    @FXML private TextField sMaxPercentField;
    @FXML private TextField mnMinPercentField;
    @FXML private TextField mnMaxPercentField;

    private MenuController  menuController;

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
    }

    @FXML private void menuButtonClicked()
    {
        this.menuController.backToMenu();
    }

    @FXML private void saveButtonClicked()
    {
        if (this.nameField.getText().isEmpty()         ||
            this.standardField.getText().isEmpty()     ||
            this.cMinPercentField.getText().isEmpty()  ||
            this.cMaxPercentField.getText().isEmpty()  ||
            this.siMinPercentField.getText().isEmpty() ||
            this.siMaxPercentField.getText().isEmpty() ||
            this.sMinPercentField.getText().isEmpty()  ||
            this.sMaxPercentField.getText().isEmpty()  ||
            this.mnMinPercentField.getText().isEmpty() ||
            this.mnMaxPercentField.getText().isEmpty())
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
            return;
        }

        try
        {
            if (MeltBrand.meltBrandExists(this.nameField.getText()))
            {
                Message.showErrorMessage(ErrorMessage.MELTBRAND_ALREADY_EXISTS);
                return;
            }

            double cMinPercent = 0;
            double cMaxPercent = 0;
            double siMinPercent = 0;
            double siMaxPercent = 0;
            double sMinPercent = 0;
            double sMaxPercent = 0;
            double mnMinPercent = 0;
            double mnMaxPercent = 0;

            try
            {
                cMinPercent = Double.parseDouble(cMinPercentField.getText());
                if (cMinPercent < 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                cMaxPercent = Double.parseDouble(cMaxPercentField.getText());
                if (cMaxPercent <= 0)
                {
                    throw new RuntimeException(ErrorMessage.SPECIAL_CASE);
                }

                if (cMinPercent > cMaxPercent)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                siMinPercent = Double.parseDouble(siMinPercentField.getText());
                if (siMinPercent < 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                siMaxPercent = Double.parseDouble(siMaxPercentField.getText());
                if (siMaxPercent <= 0)
                {
                    throw new RuntimeException(ErrorMessage.SPECIAL_CASE);
                }

                if (siMinPercent > siMaxPercent)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                sMinPercent = Double.parseDouble(sMinPercentField.getText());
                if (sMinPercent < 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                sMaxPercent = Double.parseDouble(sMaxPercentField.getText());
                if (sMaxPercent <= 0)
                {
                    throw new RuntimeException(ErrorMessage.SPECIAL_CASE);
                }

                if (sMinPercent > sMaxPercent)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                mnMinPercent = Double.parseDouble(mnMinPercentField.getText());
                if (mnMinPercent < 0)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }

                mnMaxPercent = Double.parseDouble(mnMaxPercentField.getText());
                if (mnMaxPercent <= 0)
                {
                    throw new RuntimeException(ErrorMessage.SPECIAL_CASE);
                }

                if (mnMinPercent > mnMaxPercent)
                {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
                }
            }
            catch (Exception ex)
            {
                if (ex.getMessage().equals(ErrorMessage.SPECIAL_CASE))
                {
                    Message.showErrorMessage(ex.getMessage());
                }
                else
                {
                    Message.showErrorMessage(ErrorMessage.INCORRECT_PERCENT);
                }

                return;
            }

            MeltBrand newMeltBrand = new MeltBrand(this.nameField.getText(), new ArrayList<>());
            newMeltBrand.setStandard(this.standardField.getText());
            newMeltBrand.getElements().add(new Element("C", cMinPercent, cMaxPercent, 0, 0));
            newMeltBrand.getElements().add(new Element("Si", siMinPercent, siMaxPercent, 0, 0));
            newMeltBrand.getElements().add(new Element("S", sMinPercent, sMaxPercent, 0, 0));
            newMeltBrand.getElements().add(new Element("Mn", mnMinPercent, mnMaxPercent, 0, 0));

            newMeltBrand.saveToDB();
            Message.showInformationMessage("Марка успешно сохранена!");
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }
}

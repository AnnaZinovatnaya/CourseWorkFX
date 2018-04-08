package Controllers;

import Models.Element;
import Models.MeltBrand;
import Util.Helper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ShowMeltBrandController {
    private ShowMeltBrandsController showMeltBrandsController;
    private MeltBrand                meltBrand;
    @FXML private TextField          standardField;
    @FXML private Label              nameLabel;
    @FXML private TextField          cMinPercentField;
    @FXML private TextField          cMaxPercentField;
    @FXML private TextField          siMinPercentField;
    @FXML private TextField          siMaxPercentField;
    @FXML private TextField          sMinPercentField;
    @FXML private TextField          sMaxPercentField;

    public void init(ShowMeltBrandsController showMeltBrandsController, String name)
    {
        this.showMeltBrandsController = showMeltBrandsController;
        try
        {
            this.meltBrand = MeltBrand.readMeltBrandFromDB(name);
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }
        this.nameLabel.setText(this.meltBrand.getName());
        this.standardField.setText(this.meltBrand.getStandard());

        for (Element el : this.meltBrand.getElements())
        {
            if (el.getName().equals("C"))
            {
                this.cMinPercentField.setText(el.getMinPercent());
                this.cMaxPercentField.setText(el.getMaxPercent());
            }
            else if (el.getName().equals("Si"))
            {
                this.siMinPercentField.setText(el.getMinPercent());
                this.siMaxPercentField.setText(el.getMaxPercent());
            }
            else if (el.getName().equals("S"))
            {
                this.sMinPercentField.setText(el.getMinPercent());
                this.sMaxPercentField.setText(el.getMaxPercent());
            }
        }
    }

    @FXML private void backButtonClicked()
    {
        this.showMeltBrandsController.backToScene();
    }

    @FXML private void saveButtonClicked()
    {}

    @FXML private void deleteButtonClicked()
    {}
}

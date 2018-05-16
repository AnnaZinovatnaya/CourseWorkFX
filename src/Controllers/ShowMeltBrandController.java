package Controllers;

import Models.Element;
import Models.MeltBrand;
import Util.ErrorMessage;
import Util.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    @FXML private Button             saveButton;
    @FXML private Button             deleteButton;
    private boolean                  isDeleted;

    public void init(ShowMeltBrandsController showMeltBrandsController, String name)
    {
        this.showMeltBrandsController = showMeltBrandsController;
        try
        {
            this.meltBrand = MeltBrand.readMeltBrandFromDB(name);
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
        this.nameLabel.setText(this.meltBrand.getName());
        this.standardField.setText(this.meltBrand.getStandard());

        for (Element el : this.meltBrand.getElements())
        {
            switch (el.getName()) {
                case "C":
                    this.cMinPercentField.setText(el.getMinPercent());
                    this.cMaxPercentField.setText(el.getMaxPercent());
                    break;
                case "Si":
                    this.siMinPercentField.setText(el.getMinPercent());
                    this.siMaxPercentField.setText(el.getMaxPercent());
                    break;
                case "S":
                    this.sMinPercentField.setText(el.getMinPercent());
                    this.sMaxPercentField.setText(el.getMaxPercent());
                    break;
            }
        }

        this.saveButton.setDisable(true);
        //this.deleteButton.setDisable(true);
    }

    @FXML private void backButtonClicked()
    {
        this.showMeltBrandsController.backToScene();
    }

    @FXML private void saveButtonClicked()
    {
        if (this.standardField.getText().isEmpty()     ||
            this.cMinPercentField.getText().isEmpty()  ||
            this.cMaxPercentField.getText().isEmpty()  ||
            this.siMinPercentField.getText().isEmpty() ||
            this.siMaxPercentField.getText().isEmpty() ||
            this.sMinPercentField.getText().isEmpty()  ||
            this.sMaxPercentField.getText().isEmpty())
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
            return;
        }

        this.meltBrand.setStandard(this.standardField.getText());

        try
        {
            double cMinPercent = Double.parseDouble(cMinPercentField.getText());
            if (cMinPercent < 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            double cMaxPercent = Double.parseDouble(cMaxPercentField.getText());
            if (cMaxPercent < 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            if (cMinPercent > cMaxPercent)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            double siMinPercent = Double.parseDouble(siMinPercentField.getText());
            if (siMinPercent < 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            double siMaxPercent = Double.parseDouble(siMaxPercentField.getText());
            if (siMaxPercent < 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            if (siMinPercent > siMaxPercent)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            double sMinPercent = Double.parseDouble(sMinPercentField.getText());
            if (sMinPercent < 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            double sMaxPercent = Double.parseDouble(sMaxPercentField.getText());
            if (sMaxPercent < 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            if (sMinPercent > sMaxPercent)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PERCENT);
            }

            for (Element el : this.meltBrand.getElements()) {
                switch (el.getName()) {
                    case "C":
                        el.setMinPercent(cMinPercent);
                        el.setMaxPercent(cMaxPercent);
                        break;
                    case "Si":
                        el.setMinPercent(siMinPercent);
                        el.setMaxPercent(siMaxPercent);
                        break;
                    case "S":
                        el.setMinPercent(sMinPercent);
                        el.setMaxPercent(sMaxPercent);
                        break;
                }
            }
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.INCORRECT_PERCENT);
            return;
        }

        this.meltBrand.update();
        Message.showInformationMessage("Данные о марке сохранены!");
    }

    @FXML private void deleteButtonClicked()
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("Удаление марки");
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/DeleteMeltBrandScene.fxml")
            );
            Parent root = loader.load();
            DeleteMeltBrandController deleteMeltBrandController = loader.getController();
            deleteMeltBrandController.setShowMeltBrandController(this);
            deleteMeltBrandController.init(this.meltBrand);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.standardField.getScene().getWindow());
            stage.showAndWait();
            if (isDeleted)
            {
                showMeltBrandsController.refreshItems();
                showMeltBrandsController.getMenuController().getPrimaryStage().setScene(showMeltBrandsController.getMeltBrandsView().getScene());
            }

        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

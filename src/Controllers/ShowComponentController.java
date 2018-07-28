package Controllers;

import Models.Component;
import Models.Element;
import Models.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Message;

public class ShowComponentController
{
    @FXML private Label     brandLabel;
    @FXML private Label     adoptLabel;
    @FXML private Label     cAdoptLabel;
    @FXML private Label     siAdoptLabel;
    @FXML private Label     sAdoptLabel;
    @FXML private Label     mnAdoptLabel;
    @FXML private Label     cPercentLabel;
    @FXML private Label     siPercentLabel;
    @FXML private Label     sPercentLabel;
    @FXML private Label     mnPercentLabel;
    @FXML private TextField priceField;
    @FXML private TextField amountField;
    @FXML private Label     nameLabel;

    private ShowComponentsController showComponentsController;
    private Component                component;
    private boolean                  isDeleted = false;

    public void init(ShowComponentsController showComponentsController, String name)
    {
        this.showComponentsController = showComponentsController;
        this.component = Manager.readComponentFromDB(name);
        this.nameLabel.setText(name);
        this.brandLabel.setText(this.component.getBrand());
        this.priceField.setText(String.valueOf(this.component.getPrice()));
        this.amountField.setText(String.valueOf(this.component.getAmount()));
        this.adoptLabel.setText(String.valueOf(this.component.getAdoptBase()));

        for(Element aElement: component.getElements())
        {
            if (aElement.getName().equals("C"))
            {
                this.cAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.cPercentLabel.setText(String.valueOf(aElement.getPercent()));
            }
            if (aElement.getName().equals("Si"))
            {
                this.siAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.siPercentLabel.setText(String.valueOf(aElement.getPercent()));
            }
            if (aElement.getName().equals("S"))
            {
                this.sAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.sPercentLabel.setText(String.valueOf(aElement.getPercent()));
            }
            if (aElement.getName().equals("Mn"))
            {
                this.mnAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.mnPercentLabel.setText(String.valueOf(aElement.getPercent()));
            }
        }

    }

    @FXML private void backButtonClicked()
    {
        showComponentsController.getOptionalView().getSelectionModel().clearSelection();
        showComponentsController.getMandatoryView().getSelectionModel().clearSelection();
        showComponentsController.getMenuController().getPrimaryStage().setTitle("Просмотр компонентов");
        showComponentsController.getMenuController().getPrimaryStage().setScene(showComponentsController.getMandatoryView().getScene());
    }

    @FXML private void deleteButtonClicked()
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("Удаление компонента");
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/DeleteComponentScene.fxml")
            );
            Parent root = loader.load();
            DeleteComponentController deleteComponentController = loader.getController();
            deleteComponentController.setShowComponentController(this);
            deleteComponentController.init(this.component);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.brandLabel.getScene().getWindow());
            stage.showAndWait();
            if (isDeleted)
            {
                showComponentsController.refreshItems();
                showComponentsController.getMenuController().getPrimaryStage().setScene(showComponentsController.getMandatoryView().getScene());
            }

        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
        }
    }
    @FXML
    private void saveButtonClicked()
    {
        boolean validPrice = false;
        boolean validAmount = false;
        double temp = 0;

        if (priceField.getText().isEmpty() || amountField.getText().isEmpty())
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_FIELDS);
            return;
        }

        try
        {
            temp = Double.parseDouble(priceField.getText());

            if (temp <= 0)
            {
                throw new RuntimeException(ErrorMessage.INCORRECT_PRICE);
            }

            validPrice = true;
        }
        catch (Exception ex)
        {
            Message.showErrorMessage(ErrorMessage.INCORRECT_PRICE);
        }

        if (validPrice) {
            component.setPrice(temp);

            try {
                temp = Double.parseDouble(amountField.getText());

                if (temp <= 0) {
                    throw new RuntimeException(ErrorMessage.INCORRECT_PRICE);
                }

                validAmount = true;
            } catch (Exception ex) {
                Message.showErrorMessage(ErrorMessage.INCORRECT_AMOUNT);
            }

            if (validAmount) {
                component.setAmount(temp);

                try
                {
                    component.update();
                    Message.showInformationMessage("Изменения сохранены!");
                }
                catch (RuntimeException e)
                {
                    Message.showErrorMessage(e.getLocalizedMessage());
                }
            }
        }
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

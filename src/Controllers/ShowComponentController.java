package Controllers;

import Models.Component;
import Models.Element;
import Models.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.ErrorMessage;

public class ShowComponentController
{
    @FXML private Label     brandLabel;
    @FXML private Label     adoptLabel;
    @FXML private Label     cAdoptLabel;
    @FXML private Label     siAdoptLabel;
    @FXML private Label     sAdoptLabel;
    @FXML private Label     cPercentLabel;
    @FXML private Label     siPercentLabel;
    @FXML private Label     sPercentLabel;
    @FXML private TextField priceField;
    @FXML private TextField amountField;
    @FXML private Label     nameLabel;

    private Alert           alert = new Alert(Alert.AlertType.ERROR);

    private ShowComponentsController showComponentsController;
    private Component                component;
    private boolean                  isDeleted = false;

    public void init(ShowComponentsController showComponentsController, String name)
    {
        this.showComponentsController = showComponentsController;
        this.component = Manager.findComponent(name);
        this.nameLabel.setText(name);
        this.brandLabel.setText(this.component.getBrand());
        this.priceField.setText(String.valueOf(this.component.getPrice()));
        this.amountField.setText(String.valueOf(this.component.getAmount()));
        this.adoptLabel.setText(String.valueOf(this.component.getAdoptBase()));

        for(Element aElement: component.getElements())
        {
            if(aElement.getName().equals("C"))
            {
                this.cAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.cPercentLabel.setText(String.valueOf(aElement.getPercent()));
            }
            if(aElement.getName().equals("Si"))
            {
                this.siAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.siPercentLabel.setText(String.valueOf(aElement.getPercent()));
            }
            if(aElement.getName().equals("S"))
            {
                this.sAdoptLabel.setText(String.valueOf(aElement.getAdopt()));
                this.sPercentLabel.setText(String.valueOf(aElement.getPercent()));
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
            deleteComponentController.init(this.nameLabel.getText());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.brandLabel.getScene().getWindow());
            stage.showAndWait();
            if(isDeleted)
            {
                showComponentsController.refreshItems();
                showComponentsController.getMenuController().getPrimaryStage().setScene(showComponentsController.getMandatoryView().getScene());
            }

        }
        catch (Exception ex)
        {
            alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }
    @FXML
    private void saveButtonClicked()
    {
        boolean validPrice = false;
        boolean validAmount = false;
        double temp=0;

        if(priceField.getText().isEmpty()||amountField.getText().isEmpty())
        {
            alert.setContentText(ErrorMessage.EMPTY_FIELDS);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
        else
        {
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
                alert.setContentText(ErrorMessage.INCORRECT_PRICE);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
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
                    alert.setContentText(ErrorMessage.INCORRECT_AMOUNT);
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                            .forEach(node -> ((Label) node).setFont(Font.font(16)));
                    alert.showAndWait();
                }

                if (validAmount) {
                    component.setAmount(temp);

                    try {
                        Manager.updateComponentData(component);

                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle("Сохранение");
                        alert.setHeaderText("Сохранение");
                        alert.setContentText("Изменения сохранены!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                .forEach(node -> ((Label) node).setFont(Font.font(16)));
                        alert.showAndWait();
                    }
                    catch (RuntimeException e)
                    {
                        alert.setContentText(e.getLocalizedMessage());
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                .forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

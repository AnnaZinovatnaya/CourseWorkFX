package Controllers;

import Models.Component;
import Models.Manager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.ErrorMessage;

public class AddComponentController
{
    private MenuController    menuController;
    private Alert             alert = new Alert(Alert.AlertType.ERROR);
    @FXML private TextField   nameField;
    @FXML private TextField   brandField;
    @FXML private TextField   adoptField;
    @FXML private TextField   amountField;
    @FXML private TextField   priceField;
    @FXML private RadioButton mandatoryButton;
    @FXML private RadioButton optionalButton;

    private Stage             primaryStage;
    private Component         component;

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
        this.primaryStage = this.menuController.getPrimaryStage();
    }

    @FXML private void mandatoryButtonClicked()
    {
        this.optionalButton.setSelected(false);
    }

    @FXML private void optionalButtonClicked()
    {
        this.mandatoryButton.setSelected(false);
    }

    @FXML private void menuButtonClicked()
    {
        Manager.setComponentToNull();
        menuController.backToMenu();
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.adoptField.getScene());
    }

    @FXML private void nextButtonClicked()
    {
        Manager.newComponent();
        if(nameField.getText().isEmpty()  || brandField.getText().isEmpty()  ||
           adoptField.getText().isEmpty() || amountField.getText().isEmpty() ||
           priceField.getText().isEmpty() || !mandatoryButton.isSelected()   &&
           !optionalButton.isSelected())
        {
            alert.setContentText(ErrorMessage.EMPTY_FIELDS);
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
        else
        {
            double adopt = -1;
            double amount = -1;
            double price = -1;
            boolean b = true;
            if(Manager.componentExists(nameField.getText()))
            {
                alert.setContentText("Компонент с таким названием уже есть!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b=false;
            }
            if(b)
            {
                try
                {
                    adopt = Double.parseDouble(adoptField.getText());
                }
                catch (Exception ex)
                {
                    alert.setContentText("Усвоение базового элемента задано некорректно!");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
                    alert.showAndWait();
                    b=false;
                }
                if(b)
                {
                    if (adopt < -1 || adopt > 100)
                    {
                        alert.setContentText("Усвоение базового элемента задано некорректно!");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                .forEach(node -> ((Label)node).setFont(Font.font(16)));
                        alert.showAndWait();
                        b = false;
                    }
                    if (b)
                    {
                        try
                        {
                            amount = Double.parseDouble(amountField.getText());
                        }
                        catch (Exception ex)
                        {
                            alert.setContentText("Количество на складе задано некорректно!");
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
                            alert.showAndWait();
                            b = false;
                        }
                        if (b)
                        {
                            if (adopt < -1)
                            {
                                alert.setContentText("Количество на складе задано некорректно!");
                                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                                alert.showAndWait();
                                b = false;
                            }
                            if (b)
                            {
                                try
                                {
                                    price = Double.parseDouble(priceField.getText());
                                }
                                catch (Exception ex)
                                {
                                    alert.setContentText("Цена задана некорректно!");
                                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                            .forEach(node -> ((Label)node).setFont(Font.font(16)));
                                    alert.showAndWait();
                                    b = false;
                                }
                                if (b)
                                {
                                    if (price < -1)
                                    {
                                        alert.setContentText("Цена задана некорректно!");
                                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                                                .forEach(node -> ((Label)node).setFont(Font.font(16)));
                                        alert.showAndWait();
                                        b=false;
                                    }
                                    if(b)
                                    {
                                        int mandatory=0;
                                        if(this.mandatoryButton.isSelected())
                                            mandatory=1;
                                        Manager.setComponentParam(nameField.getText(),
                                                                   brandField.getText(),
                                                                   adopt, amount, price,
                                                                   mandatory);
                                        try
                                        {
                                            FXMLLoader loader = new FXMLLoader(
                                                getClass().getResource("/Views/AddElements2Scene.fxml")
                                            );
                                            Parent root = loader.load();

                                            AddElements2Controller addElements2Controller = loader.getController();
                                            addElements2Controller.setPreviousController(this);
                                            addElements2Controller.init(FXCollections.observableArrayList("C", "S", "Si"));
                                            primaryStage.setScene(new Scene(root));
                                        }
                                        catch (Exception ex)
                                        {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Component getComponent() {
        return component;
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

package Controllers;

import Models.Manager;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import util.ErrorMessage;

public class ShowComponentsController
{
    private MenuController          menuController;
    @FXML private ListView <String> mandatoryView = new ListView<>();
    @FXML private ListView <String> optionalView = new ListView<>();
    @FXML private Tab               mandatoryTab = new Tab();
    @FXML private Tab               optionalTab = new Tab();
    private ObservableList<String>  mandatoryComps;
    private ObservableList<String>  optionalComps;

    private Alert                   alert = new Alert(Alert.AlertType.ERROR);

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
    }

    public void init()
    {
        try
        {
            this.mandatoryComps = Manager.getAllMandatoryComponentsString();
            this.optionalComps = Manager.getAllOptionalComponentsString();

            this.mandatoryView.setItems(mandatoryComps);
            this.optionalView.setItems(optionalComps);

            this.mandatoryView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent click)
                {

                    if (click.getClickCount() == 2)
                    {
                        String currentItemSelected = mandatoryView.getSelectionModel().getSelectedItem();
                        if(currentItemSelected!=null)
                        {
                            selectButtonClicked();
                        }
                    }
                }
            });

            this.optionalView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent click)
                {

                    if (click.getClickCount() == 2)
                    {
                        String currentItemSelected = optionalView.getSelectionModel().getSelectedItem();
                        if(currentItemSelected!=null)
                        {
                            selectButtonClicked();
                        }
                    }
                }
            });
            this.optionalView.getSelectionModel().clearSelection();
        }
        catch (RuntimeException e)
        {
            alert.setContentText(e.getLocalizedMessage());
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML private void menuButtonClicked()
    {
        menuController.backToMenu();
    }

    @FXML private void selectButtonClicked()
    {
        String temp;
        if(this.mandatoryTab.isSelected())
        {
            temp = this.mandatoryView.getSelectionModel().getSelectedItem();
        }
        else
        {
            temp = this.optionalView.getSelectionModel().getSelectedItem();
        }
        if (temp != null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ShowComponentScene.fxml")
                );
                Parent root = loader.load();
                ShowComponentController showComponentController = loader.getController();
                showComponentController.init(this, temp);

                this.menuController.getPrimaryStage().setScene(new Scene(root));
                this.menuController.getPrimaryStage().setTitle("Просмотр компонента");

            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }
        else
        {
            alert.setContentText("Выберите компонент!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public void refreshItems()
    {
        this.mandatoryComps.removeAll();
        this.optionalComps.removeAll();

        try
        {
            this.mandatoryComps = Manager.getAllMandatoryComponentsString();
            this.optionalComps = Manager.getAllOptionalComponentsString();

            this.mandatoryView.setItems(mandatoryComps);
            this.optionalView.setItems(optionalComps);
        }
        catch (RuntimeException e)
        {
            alert.setContentText(e.getLocalizedMessage());
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public ListView<String> getMandatoryView() {
        return mandatoryView;
    }

    public ListView<String> getOptionalView() {
        return optionalView;
    }
}

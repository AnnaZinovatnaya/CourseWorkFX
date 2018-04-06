package Controllers;

import Models.Manager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Util.ErrorMessage;
import Util.Helper;

public class ShowComponentsController
{
    private MenuController          menuController;
    @FXML private ListView <String> mandatoryView = new ListView<>();
    @FXML private ListView <String> optionalView = new ListView<>();
    @FXML private Tab               mandatoryTab = new Tab();
    @FXML private Tab               optionalTab = new Tab();
    private ObservableList<String>  mandatoryComps;
    private ObservableList<String>  optionalComps;

    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
    }

    public void init()
    {
        try
        {
            this.mandatoryComps = Manager.getAllMandatoryComponentNames();
            this.optionalComps = Manager.getAllOptionalComponentNames();

            this.mandatoryView.setItems(mandatoryComps);
            this.optionalView.setItems(optionalComps);

            this.mandatoryView.setOnMouseClicked(click -> {

                if (click.getClickCount() == 2)
                {
                    String currentItemSelected = mandatoryView.getSelectionModel().getSelectedItem();
                    if(currentItemSelected!=null)
                    {
                        selectButtonClicked();
                    }
                }
            });

            this.optionalView.setOnMouseClicked(click -> {

                if (click.getClickCount() == 2)
                {
                    String currentItemSelected = optionalView.getSelectionModel().getSelectedItem();
                    if(currentItemSelected!=null)
                    {
                        selectButtonClicked();
                    }
                }
            });
            this.optionalView.getSelectionModel().clearSelection();
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
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
                Helper.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
            }
        }
        else
        {
            Helper.showErrorMessage(ErrorMessage.EMPTY_COMPONENT_CHOICE);
        }
    }

    public void refreshItems()
    {
        this.mandatoryComps.removeAll();
        this.optionalComps.removeAll();

        try
        {
            this.mandatoryComps = Manager.getAllMandatoryComponentNames();
            this.optionalComps = Manager.getAllOptionalComponentNames();

            this.mandatoryView.setItems(mandatoryComps);
            this.optionalView.setItems(optionalComps);
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
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

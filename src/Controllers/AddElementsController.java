package Controllers;

import Models.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Helper;

public class AddElementsController
{
    private AddComponentController addComponentController;
    @FXML private ListView<String> allElementsList = new ListView<>();
    @FXML private ListView<String> selectedElementsList = new ListView<>();
    private ObservableList<String> items;
    private ObservableList<String> selectedItems;
    private Stage                  primaryStage;

    public void setPreviousController(AddComponentController addComponentController)
    {
        this.addComponentController = addComponentController;
    }

    @FXML public  void init()
    {
        try
        {
            items = Manager.getAllElements();
            selectedItems = FXCollections.observableArrayList();
            this.allElementsList.setItems(items);
            this.selectedElementsList.setItems(selectedItems);
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }
    }

    @FXML private void oneForwardButtonClicked()
    {
        String temp = allElementsList.getSelectionModel().getSelectedItem();
        if(!selectedItems.contains(temp)&&temp!=null)
        {
            this.selectedElementsList.getItems().add(temp);
        }
    }

    @FXML private void allForwardButtonClicked()
    {
        this.selectedElementsList.getItems().clear();
        for (String item : items)
        {
            this.selectedElementsList.getItems().add(item);
        }
    }

    @FXML private void oneBackButtonClicked()
    {
        String temp = selectedElementsList.getSelectionModel().getSelectedItem();
        if(temp!=null)
        {
            this.selectedElementsList.getItems().remove(temp);
        }
    }

    @FXML private void allBackButtonClicked()
    {
        this.selectedElementsList.getItems().clear();
    }

    @FXML private void backButtonClicked()
    {
        addComponentController.backToScene();
    }

    @FXML private void nextButtonClicked()
    {
        if(selectedItems.size()>0)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddElements2Scene.fxml")
                );
                Parent root = loader.load();
                AddElements2Controller addElements2Controller = loader.getController();
                addElements2Controller.setPreviousController(this);
                addElements2Controller.init(items);
                primaryStage = addComponentController.getPrimaryStage();
                primaryStage.setScene(new Scene(root));
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

    public void backToScene()
    {
        primaryStage.setScene(this.allElementsList.getScene());
    }

    public AddComponentController getAddComponentController() {
        return addComponentController;
    }
}

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
import util.ErrorMessage;
import util.Helper;

public class AddCharge3Controller
{
    private AddCharge2Controller   addCharge2Controller;
    private Stage                  primaryStage;

    @FXML private ListView<String> allComponentsList = new ListView<>();
    @FXML private ListView<String> selectedComponentsList = new ListView<>();
    private ObservableList<String> items;
    private ObservableList<String> selectedItems;

    public void init()
    {
        this.primaryStage = this.addCharge2Controller.getPrimaryStage();
        try
        {
            items = Manager.getAllMandatoryComponentsString();
            selectedItems = FXCollections.observableArrayList ();
            this.allComponentsList.setItems(items);
            this.selectedComponentsList.setItems(selectedItems);
        }
        catch (RuntimeException e)
        {
            Helper.showErrorMessage(e.getLocalizedMessage());
        }
    }

    @FXML private void backButtonClicked()
    {
        addCharge2Controller.backToScene();
    }
    @FXML private void nextButtonClicked()
    {
        if(selectedItems.size() > 0)
        {
            Manager.setMandatoryComponents(selectedItems);
            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge4Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge4Controller addCharge4Controller = loader.getController();
                addCharge4Controller.setAddCharge3Controller(this);
                addCharge4Controller.init();
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

    @FXML private void oneForwardButtonClicked()
    {
        String temp = allComponentsList.getSelectionModel().getSelectedItem();
        if(!selectedItems.contains(temp) && temp != null)
        {
            this.selectedComponentsList.getItems().add(temp);
        }
    }

    @FXML private void allForwardButtonClicked()
    {
        this.selectedComponentsList.getItems().clear();
        for (String item : items)
        {
            this.selectedComponentsList.getItems().add(item);
        }
    }

    @FXML private void oneBackButtonClicked()
    {
        String temp = selectedComponentsList.getSelectionModel().getSelectedItem();
        if(temp != null)
        {
            this.selectedComponentsList.getItems().remove(temp);
        }
    }

    @FXML private void allBackButtonClicked()
    {
        this.selectedComponentsList.getItems().clear();
    }


    public void backToScene()
    {
        this.primaryStage.setScene(this.allComponentsList.getScene());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setAddCharge2Controller(AddCharge2Controller addCharge2Controller) {
        this.addCharge2Controller = addCharge2Controller;
    }
}

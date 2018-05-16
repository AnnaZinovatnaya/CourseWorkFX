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
import Util.Message;

public class AddCharge3Controller
{
    private AddCharge2Controller   addCharge2Controller;
    private Stage                  primaryStage;

    private ObservableList<String> items;
    private ObservableList<String> selectedItems;

    @FXML private ListView<String> allComponentsList = new ListView<>();
    @FXML private ListView<String> selectedComponentsList = new ListView<>();

    public void init(AddCharge2Controller addCharge2Controller)
    {
        this.addCharge2Controller = addCharge2Controller;
        this.primaryStage = this.addCharge2Controller.getPrimaryStage();

        try
        {
            this.items = Manager.getAllMandatoryComponentNames();
            this.selectedItems = FXCollections.observableArrayList ();
            this.allComponentsList.setItems(this.items);
            this.selectedComponentsList.setItems(this.selectedItems);
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }

    @FXML private void backButtonClicked()
    {
        this.addCharge2Controller.backToScene();
    }

    @FXML private void nextButtonClicked()
    {
        if (this.selectedItems.size() != 0)
        {
            Manager.setMandatoryComponents(this.selectedItems);
            try
            {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddCharge4Scene.fxml")
                );
                Parent root = loader.load();
                AddCharge4Controller addCharge4Controller = loader.getController();
                addCharge4Controller.init(this);
                this.primaryStage.setScene(new Scene(root));
            }
            catch (Exception ex)
            {
                Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
            }
        }
        else
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_COMPONENT_CHOICE);
        }
    }

    @FXML private void oneForwardButtonClicked()
    {
        String selectedComponent = this.allComponentsList.getSelectionModel().getSelectedItem();
        if (!this.selectedItems.contains(selectedComponent) && selectedComponent != null)
        {
            this.selectedComponentsList.getItems().add(selectedComponent);
        }
    }

    @FXML private void allForwardButtonClicked()
    {
        this.selectedComponentsList.getItems().clear();
        this.selectedComponentsList.getItems().addAll(items);
    }

    @FXML private void oneBackButtonClicked()
    {
        String selectedComponent = this.selectedComponentsList.getSelectionModel().getSelectedItem();
        if (selectedComponent != null)
        {
            this.selectedComponentsList.getItems().remove(selectedComponent);
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
        return this.primaryStage;
    }
}

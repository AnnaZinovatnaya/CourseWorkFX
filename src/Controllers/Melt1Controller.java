package Controllers;

import Models.Manager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Message;

public class Melt1Controller {
    @FXML private ListView<String> brandListView = new ListView<>();
    private Stage                  primaryStage;
    private UserController         userController;

    public void init(Stage primaryStage, UserController userController){
        this.primaryStage = primaryStage;
        this.userController = userController;
        try
        {
            ObservableList<String> brands = Manager.getAllBrands();

            this.brandListView.setItems(brands);

            this.brandListView.setOnMouseClicked(click -> {
                if (click.getClickCount() == 2)
                {
                    String currentItemSelected = brandListView.getSelectionModel().getSelectedItem();
                    if (currentItemSelected != null)
                    {
                        selectBrandButtonClicked();
                    }
                }
            });
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }

    @FXML public void selectBrandButtonClicked()
    {
        String chosenBrand = this.brandListView.getSelectionModel().getSelectedItem();
        if (null != chosenBrand)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/MakeMelt2Scene.fxml")
                );
                Parent root = loader.load();
                Melt2Controller melt2Controller = loader.getController();
                melt2Controller.init(this.primaryStage, this, chosenBrand);
                primaryStage.setScene(new Scene(root));
            }
            catch (Exception ex)
            {
                Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
            }

            primaryStage.show();
        }
        else
        {
            Message.showErrorMessage("Выберите марку сплава!");
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.brandListView.getScene());
    }

    @FXML public void logoutButtonClicked()
    {
        this.userController.backToScene();
    }
}

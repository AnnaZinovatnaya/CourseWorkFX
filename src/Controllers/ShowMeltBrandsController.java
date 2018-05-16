package Controllers;

import Models.MeltBrand;
import Util.ErrorMessage;
import Util.Message;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

public class ShowMeltBrandsController {
    private MenuController          menuController;
    @FXML private ListView<String>  meltBrandsView = new ListView<>();
    private ObservableList<String>  meltBrands;


    public void setMenuController(MenuController menuController)
    {
        this.menuController = menuController;
    }

    public void init()
    {
        try
        {
            this.meltBrands = MeltBrand.getAllBrandNamesFromDB();
            this.meltBrandsView.setItems(this.meltBrands);

            this.meltBrandsView.setOnMouseClicked(click -> {

                if (click.getClickCount() == 2)
                {
                    String currentItemSelected = meltBrandsView.getSelectionModel().getSelectedItem();
                    if(currentItemSelected != null)
                    {
                        selectButtonClicked();
                    }
                }
            });
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }

    @FXML private void menuButtonClicked()
    {
        menuController.backToMenu();
    }

    @FXML private void selectButtonClicked()
    {
        String meltBrandName = this.meltBrandsView.getSelectionModel().getSelectedItem();

        if (meltBrandName != null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/ShowMeltBrandScene.fxml")
                );
                Parent root = loader.load();
                ShowMeltBrandController showMeltBrandController = loader.getController();
                showMeltBrandController.init(this, meltBrandName);

                this.menuController.getPrimaryStage().setScene(new Scene(root));
                this.menuController.getPrimaryStage().setTitle("Просмотр марки сплава");

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Message.showErrorMessage(ErrorMessage.CANNOT_LOAD_SCENE);
            }
        }
        else
        {
            Message.showErrorMessage(ErrorMessage.EMPTY_MELT_BRAND_CHOICE);
        }
    }

    public void refreshItems()
    {
        this.meltBrands.removeAll();

        try
        {
            this.meltBrands = MeltBrand.getAllBrandNamesFromDB();
            this.meltBrandsView.setItems(meltBrands);
        }
        catch (RuntimeException e)
        {
            Message.showErrorMessage(e.getLocalizedMessage());
        }
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public void backToScene()
    {
        this.menuController.getPrimaryStage().setScene(this.meltBrandsView.getScene());
    }

    public ListView<String> getMeltBrandsView() {
        return meltBrandsView;
    }
}

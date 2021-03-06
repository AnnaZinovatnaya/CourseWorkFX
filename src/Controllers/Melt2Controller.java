package Controllers;

import Models.Charge;
import Models.Element;
import Models.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Util.ErrorMessage;
import Util.Message;

public class Melt2Controller {
    private Melt1Controller melt1Controller;
    private Stage           primaryStage;

    @FXML private ListView<String> chargesListView  = new ListView<>();

    private ObservableList<Charge> charges;
    private ObservableList<String> chargesString;

    public void init(Stage primaryStage, Melt1Controller melt1Controller, String meltBrand)
    {
        this.primaryStage = primaryStage;
        this.melt1Controller = melt1Controller;

        this.charges = FXCollections.observableArrayList();
        this.chargesString = FXCollections.observableArrayList();
        for(Charge aCharge: Manager.getCharges(meltBrand))
        {
            this.charges.add(aCharge);
            StringBuilder tempString = new StringBuilder("№" + aCharge.getId() + " " + aCharge.getMeltBrand().getName() + " " + aCharge.getMass() + " кг ");

            for (Element aElement : aCharge.getElements())
            {
                tempString.append(aElement.getName()).append(" (").append(aElement.getMinPercent()).append(" - ").append(aElement.getMaxPercent()).append(") ");
            }

            this.chargesString.add(tempString.toString());
        }

        this.chargesListView.setItems(chargesString);

        this.chargesListView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2)
            {
                String currentItemSelected = chargesListView.getSelectionModel().getSelectedItem();
                if(currentItemSelected != null)
                {
                    selectButtonClicked();
                }
            }
        });
    }

    @FXML
    public void backButtonClicked()
    {
        this.melt1Controller.backToScene();
    }
    @FXML
    public void selectButtonClicked()
    {
        String chosenCharge = this.chargesListView.getSelectionModel().getSelectedItem();
        if (null != chosenCharge)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/MakeMelt3Scene.fxml")
                );
                Parent root = loader.load();
                Melt3Controller melt3Controller = loader.getController();
                melt3Controller.init(this, this.charges.get(this.chargesString.indexOf(chosenCharge)));
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
            Message.showErrorMessage("Выберите шихту!");
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.chargesListView.getScene());
    }
}

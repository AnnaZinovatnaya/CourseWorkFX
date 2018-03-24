package Controllers;

import Models.Charge;
import Models.Element;
import Models.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Melt2Controller {
    private Melt1Controller melt1Controller;
    private Stage primaryStage;
    private String meltBrand;

    @FXML private ListView<String> chargesListView  = new ListView<>();;

    private ObservableList<Charge> charges;
    private ObservableList<String> chargesString;

    public void init(Stage primaryStage, Melt1Controller melt1Controller, String meltBrand)
    {
        this.primaryStage = primaryStage;
        this.melt1Controller = melt1Controller;
        this.meltBrand = meltBrand;

        this.charges = FXCollections.observableArrayList();
        this.chargesString = FXCollections.observableArrayList();
        for(Charge aCharge: Manager.getCharges(meltBrand))
        {
            this.charges.add(aCharge);
            String tempString = "№" + aCharge.getId() + " " + aCharge.getMeltBrand().getName() + " " + aCharge.getMass() + " кг ";

            for (Element aElement : aCharge.getElements())
            {
                tempString += aElement.getName() + " (" + aElement.getMinPercent() + " - " + aElement.getMaxPercent() + ") ";
            }

            this.chargesString.add(tempString);
        }

        this.chargesListView.setItems(chargesString);
    }

    @FXML
    public void backButtonClicked()
    {
        this.melt1Controller.backToScene();
    }
    @FXML
    public void selectButtonClicked()
    {

    }
}

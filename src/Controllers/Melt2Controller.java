package Controllers;

import Models.Charge;
import Models.Element;
import Models.Manager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.ErrorMessage;

public class Melt2Controller {
    private Melt1Controller melt1Controller;
    private Stage           primaryStage;
    private String          meltBrand;
    private Alert           alert = new Alert(Alert.AlertType.ERROR);

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
                melt3Controller.init(this.primaryStage, this, this.charges.get(this.chargesString.indexOf(chosenCharge)));
                primaryStage.setScene(new Scene(root));
            }
            catch (Exception ex)
            {
                alert.setContentText(ErrorMessage.CANNOT_LOAD_SCENE);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                        .forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }

            primaryStage.show();
        }
        else
        {
            alert.setContentText("Выберите шихту!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    public void backToScene()
    {
        this.primaryStage.setScene(this.chargesListView.getScene());
    }
}

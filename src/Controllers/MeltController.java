package Controllers;

import Models.Manager;
import Models.MeltForView;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.ErrorMessage;

public class MeltController {
    @FXML private ListView<String> brandListView = new ListView<>();
    private ObservableList<String> brands;
    private String                 chosenBrand;
    private Alert                  alert = new Alert(Alert.AlertType.ERROR);
    private Stage                  primaryStage;

    public void init(Stage primaryStage){
        this.primaryStage = primaryStage;
        try
        {
            this.brands = Manager.getAllBrands();

            this.brandListView.setItems(brands);

            this.brandListView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent click)
                {
                    if (click.getClickCount() == 2)
                    {
                        String currentItemSelected = brandListView.getSelectionModel().getSelectedItem();
                        if(currentItemSelected!=null)
                        {
                            selectBrandButtonClicked();
                        }
                    }
                }
            });
        }
        catch (RuntimeException e)
        {
            alert.setContentText(e.getLocalizedMessage());
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML public void selectBrandButtonClicked()
    {
        chosenBrand = this.brandListView.getSelectionModel().getSelectedItem();
        if (null != chosenBrand)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/MakeMelt2Scene.fxml")
                );
                Parent root = loader.load();
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
            alert.setContentText("Выберите марку сплава!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                    .forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }

    @FXML public void backButtonClicked()
    {
        this.primaryStage.setScene(this.brandListView.getScene());
    }
}

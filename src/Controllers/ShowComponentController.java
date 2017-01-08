package Controllers;

import Models.Component;
import Models.Element;
import Models.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowComponentController {
    @FXML private TextField BrandField;
    @FXML private TextField AdoptField;
    @FXML private TextField CAdoptField;
    @FXML private TextField SiAdoptField;
    @FXML private TextField SAdoptField;
    @FXML private TextField CPercentField;
    @FXML private TextField SiPercentField;
    @FXML private TextField SPercentField;
    @FXML private TextField PriceField;
    @FXML private TextField AmountField;
    @FXML private ChoiceBox<String> TypeBox;
    @FXML private Label NameLabel;

    private ShowComponentsController showComponentsController;
    Component component;
    public boolean deleted = false;

    public void init(ShowComponentsController showComponentsController, String name){
        this.showComponentsController = showComponentsController;
        this.component = Register.findComponent(name);
        this.NameLabel.setText(name);
        this.BrandField.setText(this.component.getBrand());
        this.PriceField.setText(String.valueOf(this.component.getPrice()));
        this.AmountField.setText(String.valueOf(this.component.getAmount()));
        this.AdoptField.setText(String.valueOf(this.component.getAdoptBase()));

        for(Element aElement: component.getElements()){
            if(aElement.getName().equals("C")){
                this.CAdoptField.setText(String.valueOf(aElement.getAdopt()));
                this.CPercentField.setText(String.valueOf(aElement.getPercent()));
            }
            if(aElement.getName().equals("Si")){
                this.SiAdoptField.setText(String.valueOf(aElement.getAdopt()));
                this.SiPercentField.setText(String.valueOf(aElement.getPercent()));
            }
            if(aElement.getName().equals("S")){
                this.SAdoptField.setText(String.valueOf(aElement.getAdopt()));
                this.SPercentField.setText(String.valueOf(aElement.getPercent()));
            }
        }
    }


    @FXML
    private void backButtonClicked(ActionEvent e) {
        this.showComponentsController.metallurgistMenuController.primaryStage.setScene(this.showComponentsController.MandatoryView.getScene());

    }
    @FXML
    private void deleteButtonClicked(ActionEvent e) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Удаление компонента");
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/DeleteComponentScene.fxml")
            );
            Parent root = loader.load();
            DeleteComponentController deleteComponentController = loader.getController();
            deleteComponentController.showComponentController=this;
            deleteComponentController.init(this.NameLabel.getText());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.BrandField.getScene().getWindow());
            stage.showAndWait();
            if(deleted) {
                this.showComponentsController.refreshItems();
                this.showComponentsController.metallurgistMenuController.primaryStage.setScene(this.showComponentsController.MandatoryView.getScene());
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void saveButtonClicked(ActionEvent e) {

    }
}

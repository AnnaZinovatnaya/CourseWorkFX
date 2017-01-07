package Controllers;

import Models.Component;
import Models.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    public void init(ShowComponentsController showComponentsController, String name){
        this.showComponentsController = showComponentsController;
        this.component = Register.findComponent(name);
        this.NameLabel.setText(name);
        this.BrandField.setText(this.component.getBrand());
        this.PriceField.setText(String.valueOf(this.component.getPrice()));
        this.AmountField.setText(String.valueOf(this.component.getAmount()));
        this.AdoptField.setText(String.valueOf(this.component.getAdoptBase()));
    }


    @FXML
    private void backButtonClicked(ActionEvent e) {

    }
    @FXML
    private void deleteButtonClicked(ActionEvent e) {

    }
    @FXML
    private void saveButtonClicked(ActionEvent e) {

    }
}

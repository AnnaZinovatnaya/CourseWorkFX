package Controllers;

import Models.Component;
import Models.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DeleteComponentController {

    ShowComponentsController showComponentsController;
    Component temp;
    int type;
    private Alert alert = new Alert(Alert.AlertType.ERROR);


    public void init(Component temp, int type){
        this.temp = temp;
        this.type = type;
    }
    @FXML
    private void deleteButtonClicked(ActionEvent e){
        Stage stage;
        Register.deleteComponent(temp.getName());

        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Удаление компонента");
        alert.setTitle("Удаление компонента");
        alert.setContentText("Компонент удален!");
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
        alert.showAndWait();
        alert.setAlertType(Alert.AlertType.ERROR);
        if(type==0) {
            this.showComponentsController.MandatoryComponentsTable.getItems().remove(temp);
            this.showComponentsController.MandatoryComponentsTable.refresh();
        }
        else{
            this.showComponentsController.OptionalComponentsTable.getItems().remove(temp);
            this.showComponentsController.OptionalComponentsTable.refresh();
        }

        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML private void cancelButtonClicked(ActionEvent e){
        Stage stage;
        stage = (Stage)((Button) e.getSource()).getScene().getWindow();
        stage.close();
    }
}

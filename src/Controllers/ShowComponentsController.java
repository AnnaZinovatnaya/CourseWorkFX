package Controllers;

import Models.Component;
import Models.Register;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;

public class ShowComponentsController {
    MetallurgistMenuController metallurgistMenuController;
    @FXML public ListView <String> MandatoryView= new ListView<>();
    @FXML public ListView <String> OptionalView= new ListView<>();


    @FXML private Tab mandatoryTab = new Tab();
    @FXML private Tab optionalTab = new Tab();

    ObservableList<String> mandatoryComps;
    ObservableList<String> optionalComps;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void setMenuController(MetallurgistMenuController metallurgistMenuController){
        this.metallurgistMenuController = metallurgistMenuController;
    }

    public void init(){

        this.mandatoryComps = Register.getAllMandatoryComponentsString();
        this.optionalComps = Register.getAllOptionalComponentsString();

        this.MandatoryView.setItems(mandatoryComps);
        this.OptionalView.setItems(optionalComps);
    }

    @FXML private void menuButtonClicked(ActionEvent e){
        metallurgistMenuController.backToMenu();
    }

    /*
    @FXML private void deleteButtonClicked(ActionEvent e) {


        if(this.mandatoryTab.isSelected()) {
            Component temp = this.MandatoryComponentsTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                try {
                    Stage stage = new Stage();
                    stage.setTitle("Удаление компонента");
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/Views/DeleteComponentScene.fxml")
                    );
                    Parent root = loader.load();
                    DeleteComponentController deleteComponentController = loader.getController();
                    deleteComponentController.showComponentsController=this;
                    deleteComponentController.init(temp, 0);
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(this.MandatoryComponentsTable.getScene().getWindow());
                    stage.showAndWait();

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            } else{
                alert.setContentText("Выберите компонент для удаления!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }


        }
        else{
            Component temp = this.OptionalComponentsTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                try {
                    Stage stage = new Stage();
                    stage.setTitle("Удаление компонента");
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/Views/DeleteComponentScene.fxml")
                    );
                    Parent root = loader.load();
                    DeleteComponentController deleteComponentController = loader.getController();
                    deleteComponentController.showComponentsController=this;
                    deleteComponentController.init(temp, 1);
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(this.OptionalComponentsTable.getScene().getWindow());
                    stage.showAndWait();

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } else{
                alert.setContentText("Выберите компонент для удаления!");

                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
        }

    }
    */

    @FXML private void selectButtonClicked(ActionEvent e) {
        String temp;
        if(this.mandatoryTab.isSelected()) {
            temp = this.MandatoryView.getSelectionModel().getSelectedItem();
        }else {
            temp = this.OptionalView.getSelectionModel().getSelectedItem();
        }
        if (temp != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/Views/ShowComponentScene.fxml")
                    );
                    Parent root = loader.load();
                    ShowComponentController showComponentController = loader.getController();
                    showComponentController.init(this, temp);
                    this.metallurgistMenuController.primaryStage.setScene(new Scene(root));
                    this.metallurgistMenuController.primaryStage.setTitle("Просмотр компонента");

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            } else{
                alert.setContentText("Выберите компонент!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
            }
    }

    public void refreshItems(){
        this.mandatoryComps.removeAll();
        this.optionalComps.removeAll();

        this.mandatoryComps = Register.getAllMandatoryComponentsString();
        this.optionalComps = Register.getAllOptionalComponentsString();

        this.MandatoryView.setItems(mandatoryComps);
        this.OptionalView.setItems(optionalComps);
    }
}

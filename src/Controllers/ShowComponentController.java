package Controllers;

import Models.Component;
import Models.Element;
import Models.Register;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
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
    @FXML private Label NameLabel;

    private Alert alert = new Alert(Alert.AlertType.ERROR);

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
    private void backButtonClicked() {
        showComponentsController.OptionalView.getSelectionModel().clearSelection();
        showComponentsController.MandatoryView.getSelectionModel().clearSelection();
        if(showComponentsController.menuController!=null){
            showComponentsController.menuController.primaryStage.setTitle("Просмотр компонентов");
            showComponentsController.menuController.primaryStage.setScene(showComponentsController.MandatoryView.getScene());
        }else{
            showComponentsController.menuController.primaryStage.setTitle("Просмотр компонентов");
            showComponentsController.menuController.primaryStage.setScene(showComponentsController.MandatoryView.getScene());

        }

    }
    @FXML
    private void deleteButtonClicked() {
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
                showComponentsController.refreshItems();
                showComponentsController.menuController.primaryStage.setScene(showComponentsController.MandatoryView.getScene());

            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void saveButtonClicked() {
        boolean b=true;
        double temp=0;

        if(BrandField.getText().isEmpty()||AdoptField.getText().isEmpty()||PriceField.getText().isEmpty()||AmountField.getText().isEmpty()||CAdoptField.getText().isEmpty()||CPercentField.getText().isEmpty()||SiAdoptField.getText().isEmpty()||SiPercentField.getText().isEmpty()||SAdoptField.getText().isEmpty()||SPercentField.getText().isEmpty()){
            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
            b=false;
        }
        if(b){
            component.setBrand(BrandField.getText());
            try {
                temp = Double.parseDouble(AdoptField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Усвоение базового элемента задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            component.setAdoptBase(temp);
        }

        if(b){
            try {
                temp = Double.parseDouble(PriceField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Цена задана некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            component.setPrice(temp);
        }
        if(b){
            try {
                temp = Double.parseDouble(AmountField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Количество на складе задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            component.setAmount(temp);
        }

        if(b){
            try {
                temp = Double.parseDouble(CAdoptField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Усвоение С задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            for(Element aElement: component.getElements()){
                if(aElement.getName().equals("C")){
                    aElement.setAdopt(temp);
                }
            }
        }

        if(b){
            try {
                temp = Double.parseDouble(SiAdoptField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Усвоение Si задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            for(Element aElement: component.getElements()){
                if(aElement.getName().equals("Si")){
                    aElement.setAdopt(temp);
                }
            }
        }

        if(b){
            try {
                temp = Double.parseDouble(SAdoptField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Усвоение S задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            for(Element aElement: component.getElements()){
                if(aElement.getName().equals("S")){
                    aElement.setAdopt(temp);
                }
            }
        }

        if(b){
            try {
                temp = Double.parseDouble(CPercentField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Вхождение С задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            for(Element aElement: component.getElements()){
                if(aElement.getName().equals("C")){
                    aElement.setPercent(temp);
                }
            }
        }

        if(b){
            try {
                temp = Double.parseDouble(SiPercentField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Вхождение Si задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            for(Element aElement: component.getElements()){
                if(aElement.getName().equals("Si")){
                    aElement.setPercent(temp);
                }
            }
        }

        if(b){
            try {
                temp = Double.parseDouble(SPercentField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                alert.setContentText("Вхождение S задано некорректно!");
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
                alert.showAndWait();
                b = false;
            }
            for(Element aElement: component.getElements()){
                if(aElement.getName().equals("S")){
                    aElement.setPercent(temp);
                }
            }
        }
        if(b){
            Register.updateComponentData(component);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Сохранение");
            alert.setHeaderText("Сохранение");
            alert.setContentText("Изменения сохранены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();
        }
    }
}

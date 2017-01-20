package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class DirectorMenuController {

    AddComponentController addComponentController;
    ReportController reportController;
    Stage primaryStage;

    @FXML private Button ReportButton;

    public void init(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    private void addComponentButtonClicked(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/AddComponentScene.fxml")
            );
            Parent root = loader.load();
            addComponentController = loader.getController();
            addComponentController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Новый компонент");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @FXML private void showComponentsButtonClicked(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ShowComponentsScene.fxml")
            );
            Parent root = loader.load();
            ShowComponentsController showComponentsController = loader.getController();
            showComponentsController.setMenuController(this);
            showComponentsController.init();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Просмотр компонентов");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML private void reportButtonClicked(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Views/ReportScene.fxml")
            );
            Parent root = loader.load();
            reportController = loader.getController();
            reportController.setMenuController(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Отчет по плавкам");
            primaryStage.show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void backToMenu(){
        primaryStage.setScene(this.ReportButton.getScene());
        primaryStage.setTitle("Меню - Руководитель");
        primaryStage.show();
    }
}

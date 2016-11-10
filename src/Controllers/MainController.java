package Controllers;

import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    Stage stage;
    LoginController loginController;
    AdminMenuController adminMenuController;
    MetallurgistMenuController metallurgistMenuController;
    FounderMenuController founderMenuController;
    DirectorMenuController directorMenuController;

    User user;

    public void init(Stage primaryStage) throws Exception{

        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../Views/LoginScene.fxml")
        );
        Parent root = loader.load();
        loginController = loader.getController();
        loginController.setMainController(this);

        stage.setTitle("Вход в систему");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setUser(User user){
        this.user = user;
        loadMenu();
    }

    public void loadMenu(){
        try {
            if (user.getRole().equals("администратор")) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("../Views/AdminMenuScene.fxml")
                );
                Parent root = loader.load();
                adminMenuController = loader.getController();
                adminMenuController.setMainController(this);
                stage.setScene(new Scene(root));
                stage.setTitle("Меню - Администратор");

            } else if (user.getRole().equals("металлург")) {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("../Views/MetallurgistMenuScene.fxml")
                        );
                        Parent root = loader.load();
                        metallurgistMenuController = loader.getController();
                        metallurgistMenuController.setMainController(this);
                        stage.setScene(new Scene(root));
                        stage.setTitle("Меню - Металлург");

                    } else if (user.getRole().equals("плавильщик")) {
                                FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource("../Views/FounderMenuScene.fxml")
                                );
                                Parent root = loader.load();
                                founderMenuController = loader.getController();
                                founderMenuController.setMainController(this);
                                stage.setScene(new Scene(root));
                                stage.setTitle("Меню - Плавильщик");
                            } else if (user.getRole().equals("руководитель")) {
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("../Views/DirectorMenuScene.fxml")
                                    );
                                    Parent root = loader.load();
                                    directorMenuController = loader.getController();
                                    directorMenuController.setMainController(this);
                                    stage.setScene(new Scene(root));
                                    stage.setTitle("Меню - Руководитель");
                                }
        }catch (Exception e){
            e.printStackTrace();
        }

        stage.show();

    }

    public  User getUser(){
        return user;
    }
}

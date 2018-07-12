package Views;

import Controllers.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Tests.TestRunner;


public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/LoginScene.fxml"));
        Parent root = loader.load();

        UserController userController = loader.getController();
        userController.init(primaryStage);

        primaryStage.setTitle("Вход в систему");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        TestRunner.main(args);
        launch(args);
    }
}

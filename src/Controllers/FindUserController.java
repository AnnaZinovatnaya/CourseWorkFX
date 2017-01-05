package Controllers;

import Models.Register;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FindUserController {
    @FXML private TextField NameField;

    @FXML private TextField LastnameField;

    @FXML public TextArea ResultArea;

    AdminMenuController adminMenuController;
    private Alert alert = new Alert(Alert.AlertType.ERROR);

    public void setMenuController(AdminMenuController adminMenuController){
        this.adminMenuController = adminMenuController;
    }


    @FXML private void menuButtonClicked(ActionEvent e){
        adminMenuController.backToMenu();
    }

    @FXML private void searchButtonClicked(ActionEvent e){
        if(NameField.getText().isEmpty()||LastnameField.getText().isEmpty()){

            alert.setContentText("Все поля должны быть заполнены!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setFont(Font.font(16)));
            alert.showAndWait();

        } else {
            if(Register.findUser(NameField.getText(), LastnameField.getText())){
                this.ResultArea.setText("Имя - "+Register.getFoundName()+"\nФамилия - "+Register.getFoundLastname()+"\nПароль - "+Register.getFoundPassword()+"\nРоль - "+Register.getFoundRole());

            } else
                this.ResultArea.setText("Такого пользователя нет!");
        }
    }

    @FXML private void deleteButtonClicked(ActionEvent e){
        if(Register.canDelete()){
            try {
                Stage stage = new Stage();
                stage.setTitle("Удаление пользователя");
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/Views/DeleteUserScene.fxml")
                );

                Parent root = loader.load();
                DeleteUserController deleteUserController = loader.getController();
                deleteUserController.findUserController=this;
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(this.LastnameField.getScene().getWindow());
                stage.showAndWait();

            } catch (Exception ex){
                ex.printStackTrace();
            }
        } else{
            this.ResultArea.setText("Выберите пользователя для удаления!");
        }
    }


}

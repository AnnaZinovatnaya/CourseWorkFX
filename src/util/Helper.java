package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Helper {
    private static Alert alert = new Alert(Alert.AlertType.ERROR);

    public static void showErrorMessage(String message)
    {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label)node).setFont(Font.font(16)));
        alert.showAndWait();
    }

    public static void showInformationMessage(String message)
    {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.setContentText(message);
        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label)
                .forEach(node -> ((Label)node).setFont(Font.font(16)));
        alert.showAndWait();
    }
}

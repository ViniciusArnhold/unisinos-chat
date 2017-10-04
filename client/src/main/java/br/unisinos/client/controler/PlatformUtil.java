package br.unisinos.client.controler;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class PlatformUtil {

    private PlatformUtil() {

    }

    public static void showErrorDialog(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(msg);
            alert.setContentText("Failed to Connect");
            alert.showAndWait();
        });
    }
}

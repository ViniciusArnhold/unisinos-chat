package br.unisinos.client.controler;

import br.unisinos.commom.Constants;
import br.unisinos.client.chat.ChatConnection;
import br.unisinos.client.view.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class LoginController {
    @FXML
    public TextField usernameField;
    @FXML
    public Button loginBtn;
    @FXML
    private TextField serverIPField;
    @FXML
    private TextField serverPortField;

    /**
     * Initialize
     */
    @FXML
    public void initialize() {
        serverIPField.setText(Constants.DEFAULT_SERVER_HOST_NAME);
        serverPortField.setText(String.valueOf(Constants.DEFAULT_SERVER_PORT));
    }

    public void onLogin() {
        String ip = serverIPField.getText();
        Optional<Integer> optPort = tryParseInt(serverPortField.getText());
        if (!optPort.isPresent()) {
            showErrorDialog("Server Port is invalid!");
            return;
        }
        int port = optPort.get();

        String userName = usernameField.getText();

        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            showErrorDialog("Could not connect to server: [" + e.getLocalizedMessage() + "]");
            return;
        }

        handleAuth(socket);

        ChatConnection connection = new ChatConnection(socket, userName);
        Thread thread = new Thread(connection, "Chat Thread");

        thread.start();

    }

    private void handleAuth(Socket socket) {
    }

    private void showErrorDialog(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(msg);
            alert.setContentText("Failed to Connect");
            alert.showAndWait();
        });
    }

    private Optional<Integer> tryParseInt(String text) {
        try {
            return Optional.of(Integer.parseInt(text));
        } catch (NumberFormatException nfe) {
            return Optional.empty();
        }
    }

    /**
     * Exits the program, called by the closeSilently Button
     */
    public void exitProgram() {
        Platform.exit();
        System.exit(0);
    }

    public void minimize(ActionEvent actionEvent) {
        App.getPrimartyStage().setIconified(true);
    }
}

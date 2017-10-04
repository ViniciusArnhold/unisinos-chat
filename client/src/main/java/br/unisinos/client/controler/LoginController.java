package br.unisinos.client.controler;

import br.unisinos.client.socket.ChatConnection;
import br.unisinos.client.view.App;
import br.unisinos.commom.Constants;
import br.unisinos.commom.Util;
import br.unisinos.commom.message.UserLoginMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            PlatformUtil.showErrorDialog("Server Port is invalid!");
            return;
        }
        int port = optPort.get();

        String userName = usernameField.getText();

        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            PlatformUtil.showErrorDialog("Could not connect to server: [" + e.getLocalizedMessage() + "]");
            return;
        }

        UserLoginMessage userLoginMessage;
        ObjectOutputStream out;
        ObjectInputStream in;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            userLoginMessage = handleAuth(in, out, userName);
        } catch (IOException | ClassNotFoundException e) {
            PlatformUtil.showErrorDialog("Error: " + e.getMessage());
            Util.closeSilently(socket);
            return;
        }

        if (userLoginMessage.getAuthError() != null) {
            PlatformUtil.showErrorDialog("Error: " + userLoginMessage.getAuthError().getLocalizedMessage());
            return;
        }

        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/chat-view.fxml"));
        try {
            Parent window = (Pane) fmxlLoader.load();
            Scene scene = new Scene(window);

            ChatController controller = fmxlLoader.getController();
            ChatConnection connection = new ChatConnection(controller, socket, in, out, userLoginMessage.getUser());
            Thread thread = new Thread(connection, "Chat Thread");
            thread.start();

            controller.setConnection(connection);

            Stage primartyStage = App.getPrimartyStage();

            primartyStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private UserLoginMessage handleAuth(ObjectInputStream in, ObjectOutputStream out, String userName) throws IOException, ClassNotFoundException {
        out.writeObject(new UserLoginMessage(userName));

        return (UserLoginMessage) in.readObject();
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

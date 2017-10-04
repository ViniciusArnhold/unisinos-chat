package br.unisinos.client.controler;

import br.unisinos.client.socket.ChatConnection;
import br.unisinos.client.view.App;
import br.unisinos.client.view.chat.BubbleLabel;
import br.unisinos.client.view.chat.UserCellFactory;
import br.unisinos.commom.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Vinicius Pegorini Arnhold.
 */
public class ChatController {

    @FXML
    private TextArea messageArea;
    @FXML
    private ListView<HBox> messagePane;
    @FXML
    private ListView<User> onlineUserPane;
    @FXML
    private Label onlineUserCountLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private ComboBox<User.OnlineStatus> onlineSatusComboBox;

    private ChatConnection connection;


    @FXML
    public void initialize() {
        //PostConstruct
        onlineUserPane.setCellFactory(new UserCellFactory());

        App.getPrimartyStage().setOnCloseRequest(event -> {
            connection.notifyClose();
            onCloseRequest();
        });

        Platform.runLater(() -> {
            onlineSatusComboBox.getItems().clear();
            onlineSatusComboBox.getItems().addAll(User.OnlineStatus.values());
        });

    }

    public void onMessageSend() {
        String text = messageArea.getText();
        if (!text.isEmpty()) {
            connection.sendText(text);
            onSelfMessage(text);
            messageArea.clear();
        }
    }

    private void onSelfMessage(String text) {
        Platform.runLater(() -> {
            BubbleLabel bubble = new BubbleLabel(formatText(connection.getUser(), text));

            bubble.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
            bubble.setMinHeight(32);
            HBox box = new HBox();

            box.setMaxWidth(messagePane.getWidth() - 20);
            box.setAlignment(Pos.TOP_RIGHT);
            box.getChildren().addAll(bubble, getDefaultImageView());

            messagePane.getItems().add(messagePane.getItems().size(), box);
        });
    }

    public void onUserMessageReceived(User user, String text) {
        Platform.runLater(() -> {

            BubbleLabel bubble = new BubbleLabel(formatText(user, text));

            bubble.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            bubble.setMinHeight(32);
            HBox box = new HBox();
            box.setAlignment(Pos.TOP_LEFT);
            box.getChildren().addAll(getDefaultImageView(), bubble);
            messagePane.getItems().add(messagePane.getItems().size(), box);
        });
    }

    public void onServerMessageReceived(String text) {
        Task<HBox> task = new Task<HBox>() {
            @Override
            protected HBox call() throws Exception {
                BubbleLabel bubble = new BubbleLabel(formatText(null, text));

                bubble.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                bubble.setMinHeight(32);
                HBox box = new HBox();
                box.getChildren().addAll(bubble);
                return box;
            }
        };
        task.setOnSucceeded(event -> messagePane.getItems().add(task.getValue()));

        Thread thr = new Thread(task);
        thr.setDaemon(true);
        thr.start();
    }

    public void onUserLogin(User user) {
        Platform.runLater(() -> {
            onlineUserPane.getItems().add(user);
            addToOnlineUsers(1);
        });
    }

    public void onUserLogout(User user) {
        Platform.runLater(() -> {
            onlineUserPane.getItems().remove(user);
            addToOnlineUsers(-1);
        });
    }

    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
            onMessageSend();
        }
    }

    public void setConnection(ChatConnection connection) {
        this.connection = connection;
        usernameLabel.setText(connection.getUser().getName());
        onUserLogin(connection.getUser());
    }

    private void addToOnlineUsers(int i) {
        String online = onlineUserCountLabel.getText();
        int count = Integer.parseInt(online);
        count += i;
        onlineUserCountLabel.setText(String.valueOf(count));

    }

    private String formatText(User user, String text) {
        return user == null
                ? String.format("%s%nServidor: %s", getDateString(), text)
                : String.format("%s%n%s: %s", getDateString(), user.getName(), text);
    }

    public void onCloseRequest() {
        Platform.exit();
        System.exit(0);
    }

    public String getDateString() {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now());
    }

    public ImageView getDefaultImageView() {
        ImageView image = new ImageView(new Image(ChatController.class.getClassLoader().getResourceAsStream("image/annon-user.png")));
        image.setFitHeight(32);
        image.setFitWidth(32);

        return image;
    }
}

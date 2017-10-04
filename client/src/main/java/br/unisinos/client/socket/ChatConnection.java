package br.unisinos.client.socket;

import br.unisinos.client.controler.ChatController;
import br.unisinos.client.controler.PlatformUtil;
import br.unisinos.commom.User;
import br.unisinos.commom.Util;
import br.unisinos.commom.log.LoggerManager;
import br.unisinos.commom.message.*;
import br.unisinos.commom.message.error.SocketHandleDeathMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class ChatConnection implements Runnable {

    private static final Logger LOGGER = LoggerManager.getLogger(ChatConnection.class);
    private final ChatController controller;
    private final Socket socket;
    private final User user;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ChatConnection(ChatController controller, Socket socket, ObjectInputStream in, ObjectOutputStream out, User user) {
        this.controller = controller;
        this.socket = socket;
        this.input = in;
        this.output = out;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            LOGGER.info(String.format("Connected to [%s:%d]", socket.getInetAddress(), socket.getPort()));

            initialConnection();
            while (socket.isConnected()) {
                SocketMessage message = (SocketMessage) input.readObject();
                LOGGER.info("Message Received: " + message.getClass().getSimpleName() + ":" + message.getId());
                onNewMessage(message);
            }
        } catch (IOException e) {
            PlatformUtil.showErrorDialog(e.getMessage());
            controller.onCloseRequest();
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            PlatformUtil.showErrorDialog(e.getMessage());
            controller.onCloseRequest();
            throw new RuntimeException(e);
        } finally {
            Util.closeSilently(socket);
        }
    }

    private void onNewMessage(SocketMessage socketMessage) {
        if (socketMessage instanceof UserMessageMessage) {
            UserMessageMessage message = (UserMessageMessage) socketMessage;
            controller.onUserMessageReceived(message.getUser(), message.getMessage());
        } else if (socketMessage instanceof UserLoginMessage) {
            controller.onUserLogin(((UserLoginMessage) socketMessage).getUser());
        } else if (socketMessage instanceof UserLogoutMessage) {
            controller.onUserLogout(((UserLogoutMessage) socketMessage).getUser());
        } else if (socketMessage instanceof SocketHandleDeathMessage) {
            PlatformUtil.showErrorDialog("The server suffered an unrecoverable error, closing");
            controller.onCloseRequest();
        } else if (socketMessage instanceof ServerMessageMessage) {
            controller.onServerMessageReceived(((ServerMessageMessage) socketMessage).getMessage());

        } else {
            LOGGER.warning("Unknown message: " + socketMessage);
        }
    }

    private void initialConnection() {
    }

    public void sendText(String text) {
        try {
            output.writeObject(new UserMessageMessage(this.user, text));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fail to send message", e);
            throw new UncheckedIOException(e);
        }
    }

    public User getUser() {
        return user;
    }

    public void notifyClose() {
        try {
            output.writeObject(new CloseConnectionMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fail to close message", e);
            throw new UncheckedIOException(e);
        }
    }
}

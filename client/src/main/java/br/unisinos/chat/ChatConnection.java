package br.unisinos.chat;

import br.unisinos.log.LoggerManager;
import br.unisinos.message.UserLoginMessage;
import sun.plugin2.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class ChatConnection implements Runnable {

    private static final Logger LOGGER = LoggerManager.getLogger(ChatConnection.class);
    private final Socket socket;
    private final String userName;

    public ChatConnection(Socket socket, String userName) {
        this.socket = socket;
        this.userName = userName;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            LOGGER.info(String.format("Connected to [%s:%d]", socket.getInetAddress(), socket.getPort()));

            initialConnection(input, output);
            while (!socket.isConnected()) {

            }


        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void initialConnection(ObjectInputStream input, ObjectOutputStream output) {
        UserLoginMessage


    }
}

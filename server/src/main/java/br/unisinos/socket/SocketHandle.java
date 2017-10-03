package br.unisinos.socket;

import br.unisinos.User;
import br.unisinos.ex.DuplicateUserException;
import br.unisinos.log.LoggerManager;
import br.unisinos.message.*;
import br.unisinos.message.error.SocketHandleDeathMessage;
import br.unisinos.message.error.UnknowMessageTypeMessage;
import br.unisinos.socket.ex.CloseSocketException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static br.unisinos.Util.wrap;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SocketHandle implements Runnable {

    public static final Logger LOGGER = LoggerManager.getLogger(SocketHandle.class);

    private static ConcurrentHashMap<User, ObjectOutputStream> userMap = new ConcurrentHashMap<>();

    private final Socket socket;

    private boolean closed;


    public SocketHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream())) {

            //We read the first message to prepare the user.
            //It must be a UserLoginMessage
            UserLoginMessage msg = (UserLoginMessage) in.readObject();
            LOGGER.info("New SocketHandle: " + msg.getUser());

            validadeAndAddUser(msg, out);

            while (!this.closed) {
                SocketMessage socketMessage = (SocketMessage) in.readObject();
                handleNewMessage(socketMessage, out);
            }

        } catch (IOException e) {
            LOGGER.severe("Handler Fail: " + e);
            notifyFailureToSocket(e);
            close();

            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Handler Fail: " + e);
            notifyFailureToSocket(e);
            close();

            throw new RuntimeException(e);
        } catch (CloseSocketException cse) {
            //fine
            LOGGER.fine(String.format("Closing socket [%s] due to: %s", this, cse.getLocalizedMessage()));
            close();
        }
    }

    private void notifyFailureToSocket(Throwable t) {
        //if false we cant reliably notify user back.
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                writeToSocket(new SocketHandleDeathMessage(t), out);

            } catch (IOException e) {
                LOGGER.severe("Error while trying to notify user about " + e + " : " + e);
                //Handle closeSilently ourself since we are called on a catch already
                close();
                throw new UncheckedIOException(e);
            }
        }
    }

    private void handleNewMessage(SocketMessage socketMessage, ObjectOutputStream out) throws IOException {
        if (socketMessage instanceof UserMessageMessage) {
            UserMessageMessage message = (UserMessageMessage) socketMessage;
            sendToOtherUsers(message.getUser(), message);
        } else if (socketMessage instanceof CloseConnectionMessage) {
            throw new CloseSocketException(socketMessage.toString());
        } else {
            writeToSocket(new UnknowMessageTypeMessage(socketMessage), out);
        }
    }

    private void sendToOtherUsers(User user, SocketMessage socketMessage) {
        userMap.entrySet().parallelStream()
                .filter(entry -> !entry.getKey().equals(user))
                .map(Map.Entry::getValue)
                .forEach(wrap(outputStream -> writeToSocket(socketMessage, outputStream)));
    }

    private void writeToSocket(SocketMessage msg, ObjectOutputStream out) throws IOException {
        out.writeObject(msg);
    }

    private void validadeAndAddUser(UserLoginMessage msg, ObjectOutputStream out) throws IOException {
        synchronized (userMap) {
            String userName = msg.getUserName();
            User user = new User(userName);
            if (userMap.containsKey(user)) {
                LOGGER.warning("Duplicate Username in SocketHandle: " + user.getName());
                DuplicateUserException duplicateUserException = new DuplicateUserException(user);
                msg.setAuthError(duplicateUserException);

                writeToSocket(msg, out);

                throw new CloseSocketException(duplicateUserException);
            }
            LOGGER.info(String.format("New User OK! [%s]", user));

            userMap.put(user, out);

            msg.setUser(user);
            writeToSocket(msg, out);
        }
    }

    public synchronized void close() {
        if (!closed) {
            this.closed = true;
            try {
                this.socket.close();
            } catch (IOException e) {
                LOGGER.severe("Handler closeSilently(): " + e);
                throw new UncheckedIOException(e);
            }
        }
    }
}

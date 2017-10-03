package br.unisinos.message;

import br.unisinos.User;

import java.util.Objects;

public class UserMessageMessage extends SocketMessage {

    private final User user;
    private final String message;

    public UserMessageMessage(User user, String msg) {
        this.user = user;
        this.message = Objects.requireNonNull(msg);
    }


    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}

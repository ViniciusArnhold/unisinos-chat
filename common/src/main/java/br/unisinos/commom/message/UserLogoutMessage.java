package br.unisinos.commom.message;

import br.unisinos.commom.User;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class UserLogoutMessage extends SocketMessage {

    private final User user;

    public UserLogoutMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

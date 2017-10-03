package br.unisinos.message;

import br.unisinos.User;
import br.unisinos.ex.AuthenticationException;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class UserLoginMessage extends SocketMessage {

    private final String userName;
    private User user;
    private AuthenticationException authError;

    public UserLoginMessage(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthenticationException getAuthError() {
        return this.authError;
    }

    public void setAuthError(AuthenticationException authError) {
        this.authError = authError;
    }
}

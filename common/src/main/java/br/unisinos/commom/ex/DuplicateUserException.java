package br.unisinos.commom.ex;

import br.unisinos.commom.User;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class DuplicateUserException extends AuthenticationException {

    private final User user;

    public DuplicateUserException(User user) {
        super(user.getName());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

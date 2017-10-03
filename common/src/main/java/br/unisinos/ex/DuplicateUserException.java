package br.unisinos.ex;

import br.unisinos.User;

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

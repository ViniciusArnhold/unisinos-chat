package br.unisinos;

import java.util.Objects;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class User {

    private final String name;

    public User(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public enum ConnectionStatus {
        VALID,
        CONNECTED,
        INVALID
    }

    public enum OnlineStatus {
        ONLINE,
        AWAY,
        OCCUPIED,
        OFFLINE
    }


}

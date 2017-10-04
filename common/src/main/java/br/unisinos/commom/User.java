package br.unisinos.commom;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class User implements Serializable {

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

    @Override
    public String toString() {
        return getName();
    }

    public enum ConnectionStatus {
        VALID,
        CONNECTED,
        INVALID
    }

    public enum OnlineStatus {
        ONLINE,
        AWAY,
        BUSY,
        OFFLINE
    }


}

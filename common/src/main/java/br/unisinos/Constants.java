package br.unisinos;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public final class Constants {

    public static final String DEFAULT_SERVER_HOST_NAME;

    static {
        try {
            DEFAULT_SERVER_HOST_NAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static int DEFAULT_SERVER_PORT = 9090;

    private Constants() {

    }

}

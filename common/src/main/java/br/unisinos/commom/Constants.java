package br.unisinos.commom;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public final class Constants {

    public static final String DEFAULT_SERVER_HOST_NAME;

    public static String MAIN_CLASS_NAME_PROPERTY = "MAIN_CLASS_NAME";
    public static int DEFAULT_SERVER_PORT = 9090;

    static {
        try {
            DEFAULT_SERVER_HOST_NAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private Constants() {

    }

}

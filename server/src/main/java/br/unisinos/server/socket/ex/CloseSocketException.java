package br.unisinos.server.socket.ex;

public class CloseSocketException extends RuntimeException {

    public CloseSocketException(String message) {
        super(message);
    }

    public CloseSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloseSocketException(Throwable cause) {
        super(cause);
    }

    public CloseSocketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

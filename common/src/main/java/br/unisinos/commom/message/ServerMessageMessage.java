package br.unisinos.commom.message;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class ServerMessageMessage extends SocketMessage {
    private String message;

    public ServerMessageMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

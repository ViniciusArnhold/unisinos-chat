package br.unisinos.server;

import br.unisinos.commom.Constants;
import br.unisinos.commom.log.LoggerManager;
import br.unisinos.server.socket.SocketHandle;
import br.unisinos.server.socket.SocketThreadFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class ServerMain {

    static {
        System.setProperty(Constants.MAIN_CLASS_NAME_PROPERTY, ServerMain.class.getName());
    }

    public static void main(String[] args) {
        Logger logger = LoggerManager.getLogger(ServerMain.class);
        ThreadFactory threadFactory = new SocketThreadFactory();
        List<SocketHandle> handles = new ArrayList<>();

        int port = Constants.DEFAULT_SERVER_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseUnsignedInt(args[0]);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("Port Argument must be a valid integer: " + args[0]);
            }
        }

        try (ServerSocket socket = new ServerSocket(port)) {

            logger.warning(String.format("Server OK! Listening on [%s:%d]", socket.getInetAddress(), socket.getLocalPort()));
            while (true) {
                Socket con = socket.accept();

                SocketHandle handle = new SocketHandle(con);
                handles.add(handle);
                Thread thread = threadFactory.newThread(handle);
                thread.start();
            }

        } catch (IOException e) {
            logger.severe("Server Fatal Error: " + e);
            throw new UncheckedIOException(e);
        } finally {
            logger.warning("Server shutting down!");
            handles.forEach(SocketHandle::close);
        }
    }
}

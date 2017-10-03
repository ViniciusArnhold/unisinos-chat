package br.unisinos;

import br.unisinos.log.LoggerManager;
import br.unisinos.socket.SocketHandle;
import br.unisinos.socket.SocketThreadFactory;

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

    private static final Logger LOGGER = LoggerManager.getLogger(ServerMain.class);

    public static void main(String[] args) {
        ThreadFactory threadFactory = new SocketThreadFactory();
        List<SocketHandle> handles = new ArrayList<>();

        try (ServerSocket socket = new ServerSocket(Util.getDefaultServerPort())) {

            LOGGER.warning("Server OK!");
            while (true) {
                Socket con = socket.accept();

                SocketHandle handle = new SocketHandle(con);
                handles.add(handle);
                Thread thread = threadFactory.newThread(handle);
                thread.start();
            }

        } catch (IOException e) {
            LOGGER.severe("Server Fatal Error: " + e);
            throw new UncheckedIOException(e);
        } finally {
            handles.forEach(SocketHandle::close);
        }
    }
}

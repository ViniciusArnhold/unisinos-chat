package br.unisinos.socket;

import br.unisinos.log.LoggerManager;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SocketThreadFactory implements ThreadFactory {

    private static final AtomicInteger THREAD_ID_GEN = new AtomicInteger();
    private static final Logger LOGGER = LoggerManager.getLogger(SocketThreadFactory.class);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(String.format("Server-SocketHandle-[%d]", THREAD_ID_GEN.incrementAndGet()));

        thread.setPriority(Thread.MAX_PRIORITY);

        thread.setUncaughtExceptionHandler((t, e) -> LOGGER.severe(String.format("Socket Thread <%s> Failure: %s", t, e)));

        return thread;
    }
}

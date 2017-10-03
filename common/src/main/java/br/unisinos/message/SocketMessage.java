package br.unisinos.message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public abstract class SocketMessage {

    private static final AtomicInteger ID_GEN = new AtomicInteger();
    private final int id;

    protected SocketMessage() {
        this.id = ID_GEN.incrementAndGet();
    }

    public int getId() {
        return id;
    }
}

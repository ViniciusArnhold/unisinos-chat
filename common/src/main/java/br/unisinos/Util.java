package br.unisinos;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class Util {

    private Util() {

    }

    public static int getDefaultServerPort() {
        return Constants.DEFAULT_SERVER_PORT;
    }

    public static void closeSilently(Closeable closeable) {
        Objects.requireNonNull(closeable);
        try {
            closeable.close();
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static <T> Consumer<T> wrap(CheckedConsumer<T> consumer) {
        Objects.requireNonNull(consumer);

        return value -> {
            try {
                consumer.accept(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public interface CheckedConsumer<T> {
        void accept(T value) throws Exception;
    }
}

package br.unisinos.log;

import java.util.logging.Logger;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class LoggerManager {

    private static final LoggerManager INSTANCE = new LoggerManager();

    private static final String UNIQUE_LOGGER_NAME = "br.unisinos.UniqueLogger";

    private LoggerManager() {

    }

    public static Logger getLogger(Class<?> self) {
        return getInstance().getLoggerForClass(self);
    }

    private static LoggerManager getInstance() {
        return INSTANCE;
    }

    public Logger getLoggerForClass(Class<?> self) {
        return Logger.getLogger(UNIQUE_LOGGER_NAME);
    }

}

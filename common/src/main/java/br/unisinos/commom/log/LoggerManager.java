package br.unisinos.commom.log;

import br.unisinos.commom.Constants;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * @author Vinicius Pegorini Arnhold.
 */
public class LoggerManager {

    private static final String UNIQUE_LOGGER_NAME = "br.unisinos.UniqueLogger";
    private static LoggerManager instance;
    private final Logger uniqueLogger;


    private LoggerManager() {
        this.uniqueLogger = Logger.getLogger(UNIQUE_LOGGER_NAME);
        try {
            File file = new File(String.format("./log/%s.log", System.getProperty(Constants.MAIN_CLASS_NAME_PROPERTY)));
            file.getParentFile().mkdirs();
            uniqueLogger.addHandler(new FormatedFileHandler(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Logger getLogger(Class<?> self) {
        return getInstance().getLoggerForClass(self);
    }

    private static LoggerManager getInstance() {
        if (instance == null) {
            synchronized (LoggerManager.class) {
                if (instance == null) {
                    instance = new LoggerManager();
                }
            }
        }
        return instance;
    }

    public Logger getLoggerForClass(Class<?> self) {
        return this.uniqueLogger;
    }

    private static class FormatedFileHandler extends FileHandler {

        public FormatedFileHandler(String pattern) throws IOException {
            super(pattern, 1000000, 10);
            setFormatter(new SimpleFormatter());
        }
    }

}

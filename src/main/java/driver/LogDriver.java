package driver;

import java.io.*;
import java.time.LocalDateTime;
import java.util.logging.Level;

/**
 * Class LogDriver
 * <p>
 * Responsible for logging errors and warnings
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
@SuppressWarnings("unused")
public class LogDriver {
    // Singleton instance
    private static LogDriver instance;

    // Global logger instance
    private Writer writer;

    // Relative path to logfile
    private static final String logfile = "config/logfile.log";

    /**
     * Constructor
     * <p>
     * Private, instantiates global logger object
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private LogDriver() {
        File directory = new File("config/");
        if(!directory.exists()) {
            directory.mkdir();
        }

        try {
            writer = new BufferedWriter(new FileWriter(logfile, true));
        } catch(IOException e) {
            System.err.println("Unable to open logfile " + logfile);
        }
    }

    /**
     * Getter method for singleton instance
     *
     * @return LogDriver object
     */
    public static LogDriver getInstance() {
        if(instance == null) {
            instance = new LogDriver();
        }
        return instance;
    }

    /**
     * Writes a log message to the logfile
     *
     * @param classType class of object throwing the log file, pass with 'ClassName.class' for static objects
     *                  or with 'this.getClass()' for non-static
     * @param level     warning level
     * @param message   String log message
     */
    private void log(Class<?> classType, Level level, String message) {
        if(writer == null) {
            System.err.println("Error writing \'" + message + "\': logfile is closed.");
            return;
        }
        String className = classType.getName();
        String time = ClockDriver.dfFull.format(LocalDateTime.now());

        String line = time + "\t" + level.getName() + "\t" + className + "\n\t" + message;
        try {
            writer.write(line + '\n');
            writer.flush();
        } catch(IOException ignored) {
        }
    }

    /**
     * Writes a severe log message to the logfile
     *
     * @param classType class of object throwing the log file, pass with 'ClassName.class' for static objects
     *                  or with 'this.getClass()' for non-static
     * @param message   String log message
     */
    public void severe(Class<?> classType, String message) {
        log(classType, Level.SEVERE, message);
    }

    /**
     * Writes a warning log message to the logfile
     *
     * @param classType class of object throwing the log file, pass with 'ClassName.class' for static objects
     *                  or with 'this.getClass()' for non-static
     * @param message   String log message
     */
    public void warning(Class<?> classType, String message) {
        log(classType, Level.WARNING, message);
    }

    /**
     * Writes a info log message to the logfile
     *
     * @param classType class of object throwing the log file, pass with 'ClassName.class' for static objects
     *                  or with 'this.getClass()' for non-static
     * @param message   String log message
     */
    public void info(Class<?> classType, String message) {
        log(classType, Level.INFO, message);
    }

    /**
     * Closes the log file
     */
    public void close() {
        try {
            writer.close();
            writer = null;
        } catch(IOException ignored) {
        }
    }
}

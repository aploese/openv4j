/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j.testdevice;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class Main {

    private static Logger log;
    private final KW2Dummy kwDummy;

    public Main() {
        super();
        log = LoggerFactory.getLogger(Main.class);
        kwDummy = new KW2Dummy();
    }

    private void readFromStream(InputStream is) throws IOException {
        kwDummy.readFromStream(is);
    }

    private void openPort(String serialPortName) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        kwDummy.openPort(serialPortName);
    }

    public static class LogInit {

        public static final String TRACE = "trace";
        public static final String DEBUG = "debug";
        public static final String INFO = "info";
        public static final String WARN = "warn";
        public static final String ERROR = "error";
        public static final String FATAL = "fatal";

        public static synchronized void initLog(String level) {
            Properties props = new Properties();
            props.setProperty("log4j.appender.stdout",
                    "org.apache.log4j.ConsoleAppender");
            props.setProperty("log4j.appender.stdout.Target", "System.out");
            //log4j.appender.stdout=org.apache.log4j.FileAppender
            //log4j.appender.stdout.File=Easy.log
            props.setProperty("log4j.appender.stdout.layout",
                    "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.appender.stdout.layout.ConversionPattern",
                    "%d{ABSOLUTE} %5p %c{1}: %m%n");

            //set log levels - for more verbose logging change 'info' to 'debug' ###
            props.setProperty("log4j.rootLogger", level + ", stdout");
            PropertyConfigurator.configure(props);
        }
    }

    public void close() throws InterruptedException {
        kwDummy.close();
    }

    public static void main(String[] args) throws Exception {
        Main.LogInit.initLog(Main.LogInit.INFO);
        final Main device = new Main();
        try {
            device.readFromStream(new FileInputStream(args[1]));
            device.openPort(args[0]);
            System.out.print("Press any key to quit!");
            System.in.read();
        } finally {
            device.close();
        }
        log.info("Ende");
    }
}

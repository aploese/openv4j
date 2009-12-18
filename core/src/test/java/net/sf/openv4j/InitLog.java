/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j;

import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class InitLog {

    private static Logger log;

    public InitLog() {
        super();
        log = LoggerFactory.getLogger(InitLog.class);
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

}

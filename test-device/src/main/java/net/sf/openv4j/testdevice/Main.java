/*
 * Copyright 2009, openv4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * $Id: $
 *
 * @author arnep
 */
package net.sf.openv4j.testdevice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {
    private static Logger log;
    private final KW2Dummy kwDummy;

    /**
     * Creates a new Main object.
     */
    public Main() {
        super();
        log = LoggerFactory.getLogger(Main.class);
        kwDummy = new KW2Dummy();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public void close() throws InterruptedException {
        kwDummy.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
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

    private void openPort(String serialPortName) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        kwDummy.openPort(serialPortName);
    }

    private void readFromStream(InputStream is) throws IOException {
        kwDummy.readFromStream(is);
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
            props.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
            props.setProperty("log4j.appender.stdout.Target", "System.out");
            //log4j.appender.stdout=org.apache.log4j.FileAppender
            //log4j.appender.stdout.File=Easy.log
            props.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
            props.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}: %m%n");

            //set log levels - for more verbose logging change 'info' to 'debug' ###
            props.setProperty("log4j.rootLogger", level + ", stdout");
            PropertyConfigurator.configure(props);
        }
    }
}

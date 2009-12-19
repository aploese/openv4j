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
package net.sf.openv4j.memoryimage;

import java.io.IOException;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import net.sf.openv4j.DataPoint;
import net.sf.openv4j.protocolhandlers.DataContainer;
import net.sf.openv4j.protocolhandlers.ProtocolHandler;
import net.sf.openv4j.protocolhandlers.SegmentedDataContainer;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {
    private static Logger log;

    static {
        Main.LogInit.initLog(Main.LogInit.INFO);
        log = LoggerFactory.getLogger(Main.class);
    }

    /**
     * Creates a new Main object.
     */
    public Main() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            log.info("Will open SerialPort: " + args[0]);
        } else {
            log.info("no Port given, will shut down");

            return;
        }

        SerialPort masterPort = null;
        Main expl = new Main();
        ProtocolHandler protocolHandler = new ProtocolHandler();

        try {
            if (args.length > 0) {
                masterPort = ProtocolHandler.openPort(args[0]);
                protocolHandler.setStreams(masterPort.getInputStream(), masterPort.getOutputStream());
            }
        } catch (NoSuchPortException ex) {
            log.error(ex.getMessage(), ex);
        } catch (PortInUseException ex) {
            log.error(ex.getMessage(), ex);
        } catch (UnsupportedCommOperationException ex) {
            log.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        try {
            DataContainer container = new SegmentedDataContainer();

            for (DataPoint dp : DataPoint.values()) {
                container.addToDataContainer(dp);
            }

            /*
               for (int i = 0; i < DataPoint.BLOCKS.length; i ++) {
               container.addToDataContainer(DataPoint.BLOCKS[i], 16);
               }
               /*            for (int i = 0x0000; i < 0x00FFFF; i += DataContainer.DEFAULT_SEGMENT_SIZE) {
               container.addToDataContainer(i, DataContainer.DEFAULT_SEGMENT_SIZE);
               }
             */
            protocolHandler.setReadRequest(container);

            synchronized (container) {
                container.wait(container.getDataBlockCount() * 60000);
            }

            log.info("MEMORY MAP:\n" + container.toString());
            log.info("DATAPOINTS:\n" + DataPoint.printAll(container));

            StringBuilder sb = new StringBuilder();
            sb.append("ADRESSES WITH SAME DATA:\n");

            for (DataPoint p : DataPoint.values()) {
                switch (p.getValueKind()) {
                    case TEMP_ACTUAL:
                    case TEMP_DAMPED:
                    case TEMP_LOW_PASS:
                    case TEMP_NOMINAL:
                    case TEMP_PARTY:
                    case TEMP_REDUCED:
                    case CONSUPTION:
                    case CYCLES:
                    case POSITION_IN_PERCENT:
                    case POWER_IN_PERCENT:
                    case ENERGY_KWH:
                    case TEMP_MAX:
                    case CONFIG:

                        Object o = p.decode(container);

                        if (o instanceof Double) {
                            if (Double.compare(-1.0, (Double) o) == 0) {
                            } else {
                                DataPoint.printMatchingAddesses(p, container, sb);
                            }
                        } else {
                            DataPoint.printMatchingAddesses(p, container, sb);
                        }

                        break;

                    default:

                        //    DataPoint.printMatchingAddesses(p, container, sb);
                        break;
                }
            }

            log.info(sb.toString());
        } catch (Exception ex) {
            System.err.print("Error sleep " + ex);
        }

        try {
            System.out.println("CLOSE");
            protocolHandler.close();
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }

        if (masterPort != null) {
            masterPort.close();
        }
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

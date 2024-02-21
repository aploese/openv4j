/*
 * OpenV4J - Drivers for the Viessmann optolink protocol https://github.com/openv/openv/wiki
 * Copyright (C) 2009-2024, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package de.ibapl.openv4j.memoryimagereader;

import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.DataContainer;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;
import de.ibapl.openv4j.spi.protocolhandlers.OpenV4JAdapter;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.api.TimeoutIOException;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import de.ibapl.spsw.logging.SupressReadTimeoutExceptionLogWriter;
import de.ibapl.spsw.logging.TimeStampLogging;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final Logger log = Logger.getLogger("d.i.ov.mi.Main");

    public Main() {
        super();
    }

    public static void main(String[] args) {
        try {
            Options options = new Options();
            Option opt;
            OptionGroup optg;

            opt = new Option("h", "help", false, "print this help message");
            options.addOption(opt);

            opt = new Option("p", "port", true, "serial port to use");
            opt.setArgName("port");
            opt.setType(String.class);
            opt.setRequired(true);
            options.addOption(opt);

            opt = new Option("s", "segment-size", true, "segment-size default " + 16);
            opt.setArgName("segmentSize");
            opt.setType(Integer.class);
            options.addOption(opt);

            opt = null;
            optg = null;

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (ParseException ex) {
                printHelp(options);

                return;
            }

            if (cmd.hasOption("help")) {
                printHelp(options);

                return;
            }

            final DataContainer container = new DataContainer();

            final short segmentSize = Short.parseShort(cmd.getOptionValue("segment-size", "16"));
            for (int address = 0x0000; address < MemoryImage.DEFAULT_MEM_SIZE; address += segmentSize) {
                container.addMemArea(new MemArea(container, address, segmentSize));
            }

            Date startTime = new Date();
            String outName;

            ServiceLoader<SerialPortSocketFactory> spsFactory = ServiceLoader.load(SerialPortSocketFactory.class);
            SerialPortSocketFactory serialPortSocketFactory = spsFactory.iterator().next();
            System.out.println("serialPortSocketFactory " + serialPortSocketFactory.getClass().getName());
            if (args.length == 0) {
                throw new IllegalArgumentException("Portname is missing");
            }
            final File logFile = File.createTempFile("openv4j_" + LocalDateTime.now().toString() + "_", ".txt", Paths.get(".").toFile());
            final SerialPortSocket port = new LoggingSerialPortSocket(serialPortSocketFactory.open(cmd.getOptionValue("port")),
                    new SupressReadTimeoutExceptionLogWriter(new FileOutputStream(logFile), false, TimeStampLogging.UTC, false));
            log.log(Level.INFO, "SerialIO logfile is: {0}", logFile);

            final java.time.LocalDateTime start = LocalDateTime.now();
            run(port, container);
            final LocalDateTime end = LocalDateTime.now();
            log.info(String.format("Fetching all took: %s", Duration.between(start, end)));

            outName = String.format("mem-image_%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS.txt", startTime);

            PrintStream ps = new PrintStream(outName);
            MemoryImage.printAllMemory(container, ps);
            System.out.println("Memory image saved as: " + outName);

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

    }

    public static void run(SerialPortSocket port, final DataContainer dc) throws IOException {
        try (OpenV4JAdapter adapter = new OpenV4JAdapter(port)) {
            adapter.waitForTimeSlotAndSendACK();
            for (MemArea area : dc) {
                final long start = System.currentTimeMillis();
                try {
                    adapter.sendKwVirtualReadRequest(area);
                    final long end = System.currentTimeMillis();
                    System.out.print('\r');
                    System.out.append(String.format("Fetching 0x%04x 0x%02x took: %d ms", area.getBaseAddress(), area.getSize(), end - start));
                    System.out.flush();
                } catch (TimeoutIOException tioe) {
                    final long end = System.currentTimeMillis();
                    log.severe(String.format("@0x%04x 0x%02x -> Timeout after: %d ms", area.getBaseAddress(), area.getSize(), end - start));
                    log.severe("Try syncing");
                    adapter.waitForTimeSlotAndSendACK();
                    log.severe("synced");
                    adapter.sendKwVirtualReadRequest(area);
                    log.severe("data send");
                }
            }

            System.out.println("PrintAddresses");

            StringBuilder sb = new StringBuilder();

            //        DataPoint.printAddresses(sb, dc);
            System.out.print(sb.toString());
            System.out.println("PrintAddresses done");
        }
    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("openv4j-memory-image", opts);
    }
}

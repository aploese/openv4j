/*
 * OpenV4J - Drivers for the Viessmann optolink protocol https://github.com/openv/openv/wiki
 * Copyright (C) 2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.openv4j.testdevice;

import de.ibapl.openv4j.spi.emulators.KW2Emulator;
import de.ibapl.openv4j.spi.protocolhandlers.DataContainer;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import de.ibapl.spsw.logging.LoggingSerialPortSocket;
import de.ibapl.spsw.logging.SupressReadTimeoutExceptionLogWriter;
import de.ibapl.spsw.logging.TimeStampLogging;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Main {

    private final static Logger LOG = Logger.getLogger("d.i.ov.td.Main");
    private final KW2Emulator kwDummy;

    /**
     * Creates a new Main object.
     */
    public Main(SerialPortSocket port) throws IOException {
        super();
        kwDummy = new KW2Emulator(port);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InterruptedException DOCUMENT ME!
     */
    public void close() throws IOException, InterruptedException {
        kwDummy.close();
    }

    public static void main(String[] args) {
        try {
            Options options = new Options();

            Option helpOpt = new Option("h", "help", false, "print this help message");
            options.addOption(helpOpt);

            Option portOpt = new Option("p", "port", true, "serial port to use");
            portOpt.setArgName("port");
            portOpt.setType(String.class);
            portOpt.setRequired(true);
            options.addOption(portOpt);

            OptionGroup memMapOptionGroup = new OptionGroup();
            memMapOptionGroup.setRequired(true);

            Option fileMemMapOpt = new Option("f", "file", true, "read from File");
            fileMemMapOpt.setArgName("mem map file name");
            portOpt.setType(String.class);
            memMapOptionGroup.addOption(fileMemMapOpt);
            memMapOptionGroup.setRequired(false);
            options.addOptionGroup(memMapOptionGroup);

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (ParseException ex) {
                printHelp(options);

                return;
            }

            if (cmd.hasOption('h')) {
                printHelp(options);

                return;
            }

            InputStream is = null;

            if (cmd.hasOption('f')) {
                is = new FileInputStream(cmd.getOptionValue('f'));
            }
            ServiceLoader<SerialPortSocketFactory> spsFactory = ServiceLoader.load(SerialPortSocketFactory.class);
            SerialPortSocketFactory serialPortSocketFactory = spsFactory.iterator().next();
            LOG.log(Level.INFO, "serialPortSocketFactory {0}", serialPortSocketFactory.getClass().getName());
            if (args.length == 0) {
                throw new IllegalArgumentException("Portname is missing");
            }
            final File logFile = File.createTempFile("openv4j_" + LocalDateTime.now().toString() + "_", ".txt", Paths.get(".").toFile());
            final SerialPortSocket port = new LoggingSerialPortSocket(serialPortSocketFactory.open(cmd.getOptionValue("p")),
                    new SupressReadTimeoutExceptionLogWriter(new FileOutputStream(logFile), false, TimeStampLogging.UTC, false));
            LOG.log(Level.INFO, "SerialIO logfile is: {0}", logFile);

            run(port, is);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            System.exit(-1);
        }
    }

    public static void run(SerialPortSocket port, InputStream is)
            throws Exception {
        final Main device = new Main(port);
//        KW2Emulator.addDataBlocksFor_V200KW2(device.kwDummy);

        PrintStream ps = new PrintStream("./mem-image_" + LocalDateTime.now() + ".txt");
        MemoryImage.printAllMemory(device.kwDummy, ps);
        boolean active = true;
        try {
            while (active) {
                System.out.println("Press q key to quit!");
                System.out.println("Press p key to print memAreas");
                System.out.println("Press pr key to print readed memAreas");
                System.out.println("Press pw key to print written memAreas");
                System.out.println("Press c key to clear mem areas!");
                System.out.println("Press s AAAA XX .. XX to set memory at AAAA continuing with XX all in hex!");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));
                final String line = reader.readLine();
                if (line.isEmpty()) {
                    continue;
                }
                switch (line.charAt(0)) {
                    case 'q' -> {
                        System.out.print("Key pressed, will quit!");
                        device.close();
                        active = false;
                    }
                    case 'p' -> {
                        if (line.startsWith("pr")) {
                            System.out.println("READ AREAS:");
                            for (MemArea db : device.kwDummy.readAreas()) {
                                if (db.getClass() == MemArea.class) {
                                    System.out.printf(
                                            "0x%04x: 0x%02x\n", db.getBaseAddress(),
                                            db.getSize()
                                    );
                                } else {
                                    System.out.println(db.getClass().getSimpleName());
                                }
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("Memory map >>>\n");
                            DataContainer.printDataBlocksMemoryCompact(device.kwDummy, device.kwDummy.readAreas(), sb);
                            sb.append("<<< Memory map\n");
                            System.out.println(sb.toString());
                        } else if (line.startsWith("pw")) {
                            System.out.println("WRITE AREAS:");
                            for (MemArea db : device.kwDummy.writeAreas()) {
                                if (db.getClass() == MemArea.class) {
                                    System.out.printf(
                                            "0x%04x: 0x%02x\n", db.getBaseAddress(),
                                            db.getSize()
                                    );
                                } else {
                                    System.out.println(db.getClass().getSimpleName());
                                }
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("Memory map >>>\n");
                            DataContainer.printDataBlocksMemoryCompact(device.kwDummy, device.kwDummy.writeAreas(), sb);
                            sb.append("<<< Memory map\n");
                            System.out.println(sb.toString());
                        } else if (line.length() == 1) {
                            System.out.println("AREAS:");
                            for (MemArea db : device.kwDummy) {
                                if (db.getClass() == MemArea.class) {
                                    System.out.printf(
                                            "0x%04x: 0x%02x\n", db.getBaseAddress(),
                                            db.getSize()
                                    );
                                } else {
                                    System.out.println(db.getClass().getSimpleName());
                                }
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("Memory map >>>\n");
                            DataContainer.printDataBlocksMemoryCompact(device.kwDummy, sb);
                            sb.append("<<< Memory map\n");
                            System.out.println(sb.toString());
                        } else {
                            System.out.println("Unknown command line: " + line);
                        }
                    }
                    case 'c' -> {
                        device.kwDummy.clearMemAreas();
                    }
                    case 's' -> {
                        String[] lineSplitted = line.split(" ");
                        int address = Integer.parseInt(lineSplitted[1], 16);
                        for (int i = 2, len = lineSplitted.length; i < len; i++) {
                            device.kwDummy.setByte(address++, (byte) Short.parseShort(lineSplitted[i], 16));
                        }
                    }
                    default ->
                        System.out.println("Unknown command line: " + line);
                }
            }
            System.out.print("Will quit!");

        } finally {
            device.close();
        }

    }

    private static void printHelp(Options opts) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(300);
        formatter.printHelp("openv4j-test-device", opts);
    }

}

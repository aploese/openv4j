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
package de.ibapl.openv4j.spi.protocolhandlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

public class DataContainer extends MemoryImage implements Iterable<MemArea> {

    private final static Logger LOG = Logger.getLogger("d.i.ov.s.ph.DataContainer");
    private final TreeMap<Integer, MemArea> memAreas = new TreeMap<>();

    public void addMemArea(MemArea db) {
        final MemArea old = memAreas.putIfAbsent(db.getKey(), db);
        if (old != null) {
            throw new IllegalArgumentException("Container has already dataBlock: " + db);
        }
    }

    public MemArea getMemArea(int key) {
        return memAreas.get(key);
    }

    public void clearMemAreas() {
        memAreas.clear();
    }

    public static void printDataBlocksMemory(DataContainer dc, StringBuilder sb) {
        for (MemArea db : dc) {
            final int baseAddressDiv16 = (db.getBaseAddress() / 16);
            final int baseAddress = baseAddressDiv16 * 16;
            final int length;
            if (((db.getSize() + db.getBaseAddress()) % 16) == 0) {
                length = (((db.getSize() + db.getBaseAddress()) / 16) - baseAddressDiv16) * 16;
            } else {
                length = ((((db.getSize() + db.getBaseAddress()) / 16) + 1) - baseAddressDiv16) * 16;
            }
            for (int blockAddress = baseAddress; blockAddress < baseAddress + length; blockAddress += 16) {
                sb.append(String.format("%04x ", blockAddress));
                for (int offset = 0; offset < 0x10; offset++) {
                    //on , but not on 0x0f (the last).
                    if (db.containsAddress(blockAddress + offset)) {
                        sb.append(String.format(" %02x", dc.getByte(blockAddress + offset)));
                    } else {
                        sb.append(" --");
                    }
                    switch (offset) {
                        case 0x03, 0x07, 0x0b ->
                            sb.append(" |");
                        default -> {
                        }
                    }
                }
                sb.append("\n");
            }
        }
    }

    public static void printDataBlocksMemoryCompact(DataContainer dc, StringBuilder sb) {
        printDataBlocksMemoryCompact(dc, dc, sb);
    }

    public static void printDataBlocksMemoryCompact(DataContainer dc, Iterable<MemArea> memAreas, StringBuilder sb) {
        final StringBuilder line = new StringBuilder();

        for (int address = 0; address < 0x010000; address += 16) {
            boolean hasAddressBlockData = false;
            line.append(String.format("%04x ", address));
            for (int offset = 0; offset < 16; offset++) {
                boolean hasAddressData = false;
                //on , but not on 0x0f (the last).
                for (MemArea db : memAreas) {
                    if (db.containsAddress(address + offset)) {
                        line.append(String.format(" %02x", dc.getByte(address + offset)));
                        hasAddressData = true;
                        hasAddressBlockData = true;
                        break;
                    }
                }
                if (!hasAddressData) {
                    line.append(" --");
                }
                switch (offset) {
                    case 0x03, 0x07, 0x0b ->
                        line.append(" |");
                    default -> {
                    }
                }
            }
            line.append("\n");
            if (hasAddressBlockData) {
                sb.append(line);
            }
            line.setLength(0);
        }
    }

    public static void printHeatMap(DataContainer dc, boolean verbose, StringBuilder sb) {
        final StringBuilder line = new StringBuilder();
        List<MemArea> dbs = new ArrayList<>(dc.memAreas.values());
        for (int address = 0; address < 0x010000; address += 16) {
            boolean hasAddressBlockData = false;
            line.append(String.format("%04x ", address));
            for (int offset = 0; offset < 16; offset++) {
                boolean hasData = false;
                for (int i = 0; i < dbs.size(); i++) {
                    if (dbs.get(i).containsAddress(address + offset)) {
                        if (hasData) {
                            line.append(String.format("/%02d", i));
                        } else {
                            hasData = true;
                            line.append(String.format(" %02d", i));
                        }
                        hasAddressBlockData = true;
                    }
                }
                if (!hasData) {
                    line.append(" --");
                }
                //on , but not on 0x0f (the last).
                switch (offset) {
                    case 0x03, 0x07, 0x0b ->
                        line.append(" |");
                    default -> {
                    }
                }
            }
            line.append("\n");
            if (verbose || hasAddressBlockData) {
                sb.append(line);
            }
            line.setLength(0);
        }
    }

    @Override
    public Iterator<MemArea> iterator() {
        return memAreas.values().iterator();
    }

}

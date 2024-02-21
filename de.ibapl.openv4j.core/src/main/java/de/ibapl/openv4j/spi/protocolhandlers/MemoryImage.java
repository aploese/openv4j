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

import de.ibapl.openv4j.api.CycleTimeEntry;
import de.ibapl.openv4j.api.CycleTimes;
import de.ibapl.openv4j.api.ErrorListEntry;
import de.ibapl.openv4j.api.Holiday;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class MemoryImage {

    public final static int DEFAULT_MEM_SIZE = 0x10000;

    public static void readFromStream(InputStream in, DataContainer dc) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] splitted = line.split(" ");
            int address = -1;

            for (int i = 0; i < splitted.length; i++) {
                if (i == 0) {
                    address = Integer.parseInt(splitted[0], 16);
                } else {
                    if ((splitted[i].length() != 0) && (!"|".equals(splitted[i]))) {
                        dc.setByte(address, (byte) Short.parseShort(splitted[i], 16));
                        address++;
                    }
                }
            }
        }
    }

    public static void checkAddressAndSize(int baseAddress, short size) {
        if (baseAddress < 0) {
            throw new IllegalArgumentException("address must be >= 0!");
        } else if (baseAddress > 0xffff) {
            throw new IllegalArgumentException("address must be < 0xffff (64k) !");
        }
        if (size < 0) {
            throw new IllegalArgumentException("size must be >= 0!");
        } else if (size > 0x00ff) {
            throw new IllegalArgumentException("size must be < 0xff (255) !");
        }
    }

    public static void printAllMemory(MemoryImage mem, PrintStream sb) {
        for (int i = 0; i < 0x1000; i += 0x0010) {
            sb.printf("%04x  %02x %02x %02x %02x | %02x %02x %02x %02x | %02x %02x %02x %02x | %02x %02x %02x %02x\n",
                    i,
                    mem.getByte(i + 0x00), mem.getByte(i + 0x01), mem.getByte(i + 0x02), mem.getByte(i + 0x03),
                    mem.getByte(i + 0x04), mem.getByte(i + 0x05), mem.getByte(i + 0x06), mem.getByte(i + 0x07),
                    mem.getByte(i + 0x08), mem.getByte(i + 0x09), mem.getByte(i + 0x0a), mem.getByte(i + 0x0b),
                    mem.getByte(i + 0x0c), mem.getByte(i + 0x0d), mem.getByte(i + 0x0e), mem.getByte(i + 0x0f)
            );
        }
    }

    protected final byte[] buffer;

    public MemoryImage() {
        buffer = new byte[DEFAULT_MEM_SIZE];
        Arrays.fill(buffer, (byte) 0xff);
    }

    public final byte getByte(int addr) {
        return buffer[addr];
    }

    public final void setByte(int addr, byte value) {
        buffer[addr] = value;
    }

    public final boolean getBoolean(int addr) {
        return buffer[addr] != 0;
    }

    public final void setBoolean(int addr, boolean value) {
        buffer[addr] = value ? (byte) 0x01 : 0x00;
    }

    public final LocalDateTime addTimeToDate(int addr, LocalDate date) {
        final int h = getBCD(addr);
        final int min = getBCD(addr + 1);
        final int s = getBCD(addr + 2);
        final LocalTime time = LocalTime.of(h, min, s);
        return LocalDateTime.of(date, time);
    }

    public final CycleTimes getCycleTimes(int addr, int numberOfCycleTimes) {
        CycleTimes result = new CycleTimes(numberOfCycleTimes / 2);

        for (int i = 0; i < (numberOfCycleTimes / 2); i++) {
            byte dataByte = buffer[addr + (i * 2)];

            if (dataByte == 0xff) {
                result.setEntry(i, null);
            } else {
                result.setEntry(i, new CycleTimeEntry());
                result.getEntry(i).setStart((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
                dataByte = buffer[addr + (i * 2) + 1];
                result.getEntry(i).setEnd((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
            }
        }

        return result;
    }

    public final ErrorListEntry getErrorListEntry(int addr) {
        return new ErrorListEntry(buffer[addr], getDateTime(addr + 1));
    }

    public final Holiday getHoliday(int addr) {
        Holiday result = new Holiday();
        LocalDate date = getDate(addr);
        result.setStartFlag(buffer[addr + 4]);
        result.setStart(addTimeToDate(addr + 5, date));
        date = getDate(addr + 8);
        result.setEndFlag(buffer[addr + 12]);
        result.setEnd(addTimeToDate(addr + 13, date));
        return result;
    }

    public final int getInteger(int addr) {
        return (buffer[addr] & 0xFF)
                | ((buffer[addr + 1] & 0xFF) << 8)
                | ((buffer[addr + 2] & 0xFF) << 16)
                | ((buffer[addr + 3] & 0xFF) << 24);
    }

    public final short getShort(int addr) {
        return (short) ((buffer[addr] & 0xFF)
                | ((buffer[addr + 1] & 0xFF) << 8));
    }

    /**
     * short stored as Big endian
     *
     * @param addr
     * @return
     */
    public final short getShortHex(int addr) {
        return (short) (((buffer[addr] & 0xFF) << 8)
                | (buffer[addr + 1] & 0xFF));
    }

    public final LocalDateTime getDateTime(int addr) {
        int year = getBCD(addr) * 100 + getBCD(addr + 1);
        int month = getBCD(addr + 2) - 1;
        int day = getBCD(addr + 3);

        //day of week buffer.get(addr + 4);
        int h = getBCD(addr + 5);
        int min = getBCD(addr + 6);
        int s = getBCD(addr + 7);
        return LocalDateTime.of(year, month, day, h, min, s);
    }

    public final LocalDate getDate(int addr) {
        int year = getBCD(addr) * 100 + getBCD(addr + 1);
        int month = getBCD(addr + 2) - 1;
        int day = getBCD(addr + 3);
        return LocalDate.of(year, month, day);
    }

    public final short getUByte(int addr) {
        return (short) (buffer[addr] & 0xff);
    }

    public final int getUShort(int addr) {
        return getShort(addr) & 0xffff;
    }

    public final void setInteger(int addr, int value) {
        buffer[addr] = (byte) value;
        buffer[addr + 1] = (byte) ((value >> 8) & 0x00FF);
        buffer[addr + 2] = (byte) ((value >> 16) & 0x00FF);
        buffer[addr + 3] = (byte) ((value >> 24) & 0x00FF);
    }

    public final void setShort(int addr, short value) {
        buffer[addr] = (byte) value;
        buffer[addr + 1] = (byte) ((value >> 8) & 0x00FF);
    }

    public final void setShortHex(int addr, short value) {
        buffer[addr] = (byte) ((value >> 8) & 0x00FF);
        buffer[addr + 1] = (byte) value;
    }

    private short getBCD(int address) {
        final byte bcdByte = buffer[address];
        return (short) ((bcdByte & 0x0F) + (((bcdByte & 0x00F0) >> 4) * 10));
    }

}

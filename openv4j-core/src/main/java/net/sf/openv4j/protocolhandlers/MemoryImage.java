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
package net.sf.openv4j.protocolhandlers;

import java.util.Calendar;
import java.util.Date;

import net.sf.openv4j.CycleTimes;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public abstract class MemoryImage {
    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    public abstract void setByte(int addr, byte theData);

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getBool(int addr) {
        return getByte(addr) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param numberOfCycles DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CycleTimes[] getCycleTimes(int addr, int numberOfCycles) {
        CycleTimes[] result = new CycleTimes[numberOfCycles];

        for (int i = 0; i < numberOfCycles; i++) {
            int dataByte = getByte(addr + (i * 2));

            if (dataByte == 0xff) {
            } else {
                result[i] = new CycleTimes();
                result[i].setStart((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
                dataByte = getByte(addr + (i * 2) + 1);
                result[i].setEnd((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // Byte order as is
    public int getHex2(int addr) {
        int result = getByte(addr) << 8;
        result |= getByte(addr + 1);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTimeStamp_8(int addr) {
        Calendar c = Calendar.getInstance();
        int year = (decodeBCD(getByte(addr)) * 100) | decodeBCD(getByte(addr + 1));
        int month = decodeBCD(getByte(addr + 2) + 1);
        int day = decodeBCD(getByte(addr + 3));

        //day of week getByte(addr + 4);
        int h = decodeBCD(getByte(addr + 5));
        int min = decodeBCD(getByte(addr + 6));
        int s = decodeBCD(getByte(addr + 7));
        c.set(year, month, day, h, min, s);

        return c.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getUInt1(int addr) {
        int result = getByte(addr);

        if ((result & 0x80) == 0x80) {
            result |= 0xffffff00;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // swap bytes
    public int getUInt2(int addr) {
        int result = getByte(addr);
        result |= (getByte(addr + 1) << 8);

        if ((result & 0x8000) == 0x8000) {
            result |= 0xffff0000;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // swap bytes
    public int getUInt4(int addr) {
        int result = getByte(addr);
        result |= (getByte(addr + 1) << 8);
        result |= (getByte(addr + 2) << 16);
        result |= (getByte(addr + 3) << 24);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setBool(int addr, boolean value) {
        setByte(addr, (byte) (value ? 1 : 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param theData DOCUMENT ME!
     */
    public void setBytes(int addr, byte[] theData) {
        for (int i = 0; i < theData.length; i++) {
            setByte(addr + i, theData[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setHex2(int addr, int value) {
        setByte(addr, (byte) ((value >> 8) & 0x00FF));
        setByte(addr + 1, (byte) (value & 0x00FF));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setUInt1(int addr, int value) {
        setByte(addr, (byte) value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setUInt2(int addr, int value) {
        setByte(addr, (byte) value);
        setByte(addr + 1, (byte) ((value >> 8) & 0x00FF));
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setUInt4(int addr, int value) {
        setByte(addr, (byte) value);
        setByte(addr + 1, (byte) ((value >> 8) & 0x00FF));
        setByte(addr + 2, (byte) ((value >> 16) & 0x00FF));
        setByte(addr + 3, (byte) ((value >> 24) & 0x00FF));
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract int getByte(int address);

    private int decodeBCD(int bcdByte) {
        return (bcdByte & 0x0F) | (((bcdByte & 0x00F0) >> 4) * 10);
    }
}

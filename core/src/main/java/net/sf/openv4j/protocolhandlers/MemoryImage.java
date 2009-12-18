/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j.protocolhandlers;

import java.util.Calendar;
import java.util.Date;
import net.sf.openv4j.CycleTimes;

/**
 *
 * @author aploese
 */
public abstract class MemoryImage {

        protected abstract int getByte(int address);

    public abstract void setByte(int addr, byte theData);

    public void setBytes(int addr, byte[] theData) {
        for (int i = 0; i < theData.length; i++) {
            setByte(addr + i, theData[i]);
        }
    }

    public boolean getBool(int addr) {
        return getByte(addr) != 0;
    }

    public int getUInt1(int addr) {
        int result = getByte(addr);
        if ((result & 0x80) == 0x80) {
            result |= 0xffffff00;
        }
        return result;
    }

// swap bytes
    public int getUInt2(int addr) {
        int result = getByte(addr);
        result |= getByte(addr + 1) << 8;
        if ((result & 0x8000) == 0x8000) {
            result |= 0xffff0000;
        }
        return result;
    }

// swap bytes
    public int getUInt4(int addr) {
        int result = getByte(addr);
        result |= getByte(addr + 1) << 8;
        result |= getByte(addr + 2) << 16;
        result |= getByte(addr + 3) << 24;
        return result;
    }

    private int decodeBCD(int bcdByte) {
        return bcdByte & 0x0F | ((bcdByte & 0x00F0) >> 4) * 10;
    }

    public Date getTimeStamp_8(
            int addr) {
        Calendar c = Calendar.getInstance();
        int year = decodeBCD(getByte(addr)) * 100 | decodeBCD(getByte(addr + 1));
        int month = decodeBCD(getByte(addr + 2) + 1);
        int day = decodeBCD(getByte(addr + 3));
        //day of week getByte(addr + 4);
        int h = decodeBCD(getByte(addr + 5));
        int min = decodeBCD(getByte(addr + 6));
        int s = decodeBCD(getByte(addr + 7));
        c.set(year, month, day, h, min, s);
        return c.getTime();
    }

    public CycleTimes[] getCycleTimes(int addr, int numberOfCycles) {
        CycleTimes result[] = new CycleTimes[numberOfCycles];
        for (int i = 0; i < numberOfCycles; i++) {
            int dataByte = getByte(addr + i * 2);
            if (dataByte == 0xff) {
            } else {
                result[i] = new CycleTimes();
                result[i].setStart((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
                dataByte = getByte(addr + i * 2 + 1);
                result[i].setEnd((dataByte & 0xF8) >> 3, (dataByte & 7) * 10);
            }

        }
        return result;
    }

// Byte order as is
    public int getHex2(int addr) {
        int result = getByte(addr) << 8;
        result |= getByte(addr + 1);
        return result;
    }

    public void setBool(int addr, boolean value) {
        setByte(addr, (byte)(value ? 1 : 0));
    }

    public void setUInt1(int addr, int value) {
        setByte(addr, (byte)value);
    }

    public void setHex2(int addr, int value) {
        setByte(addr, (byte)((value >> 8) & 0x00FF));
        setByte(addr + 1, (byte)(value & 0x00FF));
    }

    public void setUInt2(int addr, int value) {
        setByte(addr, (byte)value);
        setByte(addr + 1, (byte)((value >> 8) & 0x00FF));
    }

    public void setUInt4(int addr, int value) {
        setByte(addr, (byte)value);
        setByte(addr + 1, (byte)((value >> 8) & 0x00FF));
        setByte(addr + 2, (byte)((value >> 16) & 0x00FF));
        setByte(addr + 3, (byte)((value >> 24) & 0x00FF));
    }
}

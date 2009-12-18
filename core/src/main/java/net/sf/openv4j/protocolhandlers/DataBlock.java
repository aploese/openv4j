/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j.protocolhandlers;

import java.util.Arrays;

/**
 *
 * @author aploese
 */
public class DataBlock {

    private byte[] data;
    private final int baseAddress;

    public DataBlock(int baseAddress, int size) {
        super();
        this.baseAddress = baseAddress;
        data = new byte[size];
        Arrays.fill(data, (byte) 0xff);
    }

    DataBlock(int baseAddress, int ... bytes) {
        super();
        this.baseAddress = baseAddress;
        data = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = (byte)bytes[i];
        }
    }

    public void writeTo(StringBuilder sb, boolean suppressFirstNewLine) {
        for (int i = 0; i < data.length; i++) {
            if (((i + baseAddress) % 16) == 0) {
                if (suppressFirstNewLine) {
                    sb.append(String.format("%04x ", i + baseAddress));
                    suppressFirstNewLine = false;
                } else {
                    sb.append(String.format("\n%04x ", i + baseAddress));
                }
            } else if (((i + baseAddress) % 4) == 0) {
                sb.append(" |");
            }
            sb.append(String.format(" %02x", data[i]));
        }
    }

    public void setByteAtPos(int pos, int theData) {
        data[pos] = (byte) theData;
    }

    public void setBytesAtPos(int pos, byte[] theData) {
        if (theData.length == data.length) {
            data = theData;
        } else {
            for (int i = 0; i < theData.length; i++) {
                data[i + pos] = theData[i];
            }
        }
    }

    public byte[] getBytes() {
        return data;
    }

    public int getBaseAddress() {
        return baseAddress;
    }

    public int getByteAtPos(int pos) {
        return 0x00FF & data[pos];
    }

    public int getLength() {
        return data.length;
    }
}

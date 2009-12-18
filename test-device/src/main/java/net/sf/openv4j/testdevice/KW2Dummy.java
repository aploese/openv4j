/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j.testdevice;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import net.sf.openv4j.DataPoint;
import net.sf.openv4j.protocolhandlers.MemoryImage;
import net.sf.openv4j.protocolhandlers.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class KW2Dummy extends MemoryImage {

    public void readFromStream(InputStream in) throws IOException {
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
                        memMap[address++] = (byte) Integer.parseInt(splitted[i], 16);
                    }
                }
            }
        }
    }

    public String streamToString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected int getByte(int address) {
        return memMap[address];
    }

    @Override
    public void setByte(int addr, byte theData) {
        memMap[addr] = theData;
    }
    private Thread t;
    private DeviceTimings dt;
    private SerialPort serialPort;
    private boolean closed;
    private long last0x05Timestamp;
    private static Logger log = LoggerFactory.getLogger(KW2Dummy.class);
    private byte[] memMap = new byte[0x010000];
    private final int default05Time;
    private final int afterDataSent05Time;
    KW2EncodeStates state = KW2EncodeStates.IDLE;
    int parsedAddress;
    int parsedData;
    byte[] writeData;
    int writeDataPos;

    private void write(byte[] bytes) throws IOException {
        serialPort.getOutputStream().write(bytes);
    }

    private void write(byte data) throws IOException {
        serialPort.getOutputStream().write(data);
    }

    private int read() throws IOException {
        return serialPort.getInputStream().read();
    }

    private void appendDataPointValues(StringBuilder sb, int address, int lenght) {
        for (DataPoint dp : DataPoint.values()) {
            if ((dp.getAddr() >= address) && (dp.getAddr() < address + lenght)) {
                sb.append(String.format("\t@%04x %s:%s (%s)", dp.getAddr(), dp.getGroup().getLabel(), dp.getLabel(), dp.decode(KW2Dummy.this).toString()));
            }
        }
    }

    private class DeviceTimings implements Runnable {

        @Override
        public void run() {
            while (!closed) {
                try {
                    write((byte) 0x05);
                    last0x05Timestamp = System.currentTimeMillis();
                    // read loop
                    while (!closed) {
                        int data = read();
                        if (data == -1) {
                            Thread.sleep(default05Time - (System.currentTimeMillis() - last0x05Timestamp));
                            break;
                        } else {
                            if (parseByte(data)) {
                                Thread.sleep(afterDataSent05Time - (System.currentTimeMillis() - last0x05Timestamp));
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    enum KW2EncodeStates {

        IDLE,
        START_RECEIVED,
        READ_ADDR_0,
        READ_ADDR_1,
        WRITE_ADDR_0,
        WRITE_ADDR_1,
        READ_LENGTH,
        WRITE_LENGTH,
        WRITE_DATA;
    }

    /**
     *
     * @param theData
     * @return true if packed parses and sent
     */
    boolean parseByte(int theData) throws IOException {
        switch (state) {
            case IDLE:
                if (theData == 0x01) {
                    setState(KW2EncodeStates.START_RECEIVED);
                }
                return false;
            case START_RECEIVED:
                if (theData == 0xF4) {
                    setState(KW2EncodeStates.WRITE_ADDR_0);
                } else if (theData == 0xF7) {
                    setState(KW2EncodeStates.READ_ADDR_0);
                } else {
                    setState(KW2EncodeStates.IDLE);
                }
                return false;
            case READ_ADDR_0:
                parsedAddress = (theData & 0x00FF) << 8;
                setState(KW2EncodeStates.READ_ADDR_1);
                return false;
            case READ_ADDR_1:
                parsedAddress |= theData & 0x00FF;
                setState(KW2EncodeStates.READ_LENGTH);
                return false;
            case READ_LENGTH: {
                final byte[] result = Arrays.copyOfRange(memMap, parsedAddress, parsedAddress + theData);
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Read at x0%04x %d Bytes [", parsedAddress, theData));
                boolean first = true;
                for (byte b : result) {
                    if (first) {
                        sb.append(String.format("%02x", b));
                    } else {
                        sb.append(String.format(" %02x", b));
                    }
                }
                sb.append("]");
                appendDataPointValues(sb, parsedAddress, theData);
                log.info(sb.toString());
                write(result);
                parsedAddress = 0;
                setState(KW2EncodeStates.IDLE);
            }
            return true;
            case WRITE_ADDR_0:
                parsedAddress = (theData & 0x00FF) << 8;
                setState(KW2EncodeStates.WRITE_ADDR_1);
                return false;
            case WRITE_ADDR_1:
                parsedAddress |= theData & 0x00FF;
                setState(KW2EncodeStates.WRITE_LENGTH);
                return false;
            case WRITE_LENGTH:
                writeData = new byte[theData];
                setState(KW2EncodeStates.WRITE_DATA);
                return false;
            case WRITE_DATA: {
                writeData[writeDataPos++] = (byte) (theData & 0x00FF);
                if (writeData.length == writeDataPos) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("Write at x0%04x %d Bytes [", parsedAddress, writeData.length));
                    int pos = parsedAddress;
                    for (byte b : writeData) {
                        if (pos == parsedAddress) {
                            sb.append(String.format("%02x", b));
                        } else {
                            sb.append(String.format(" %02x", b));
                        }
                        memMap[pos++] = b;
                    }
                    sb.append("]");
                    appendDataPointValues(sb, parsedAddress, writeData.length);
                    log.info(sb.toString());
                    write((byte) 0x00);
                    parsedAddress = 0;
                    setState(KW2EncodeStates.IDLE);
                    return true;
                }
            }
            return false;
            default:
                throw new RuntimeException("Unknown state" + state.name());

        }
    }

    private void setState(KW2EncodeStates newState) {
        log.debug(String.format("Change State from %s to %s", state, newState));
        state = newState;
    }

    public KW2Dummy() {
        this.default05Time = 2300;
        this.afterDataSent05Time = 3000;
        for (int i = 0; i < memMap.length; i++) {
            memMap[i] = (byte) 0xFF;
        }
    }

    public KW2Dummy(int default05Time, int afterDataSent05Time) {
        this.default05Time = default05Time;
        this.afterDataSent05Time = afterDataSent05Time;
        for (int i = 0; i < memMap.length; i++) {
            memMap[i] = (byte) 0xFF;
        }
    }

    public void openPort(String serialPortName) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        serialPort = ProtocolHandler.openPort(serialPortName);
        closed = false;
        dt = new DeviceTimings();
        t = new Thread(dt);
        t.start();
    }

    public void close() throws InterruptedException {
        closed = true;
        Thread.sleep(100); //TODO wait?
        t.interrupt();
        if (serialPort != null) {
            serialPort.close();
        }

    }
}

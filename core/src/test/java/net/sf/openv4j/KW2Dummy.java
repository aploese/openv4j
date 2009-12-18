/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import net.sf.openv4j.protocolhandlers.MemoryImage;
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

    class KW2Encoder {

        KW2EncodeStates state = KW2EncodeStates.IDLE;
        int parsedAddress;
        int parsedData;
        byte[] writeData;
        int writeDataPos;

        void parseByte(int theData) {
            switch (state) {
                case IDLE:
                    if (theData == 0x01) {
                        setState(KW2EncodeStates.START_RECEIVED);
                    }
                    break;
                case START_RECEIVED:
                    if (theData == 0xF4) {
                        setState(KW2EncodeStates.WRITE_ADDR_0);
                    } else if (theData == 0xF7) {
                        setState(KW2EncodeStates.READ_ADDR_0);
                    } else {
                        setState(KW2EncodeStates.IDLE);
                    }
                    break;
                case READ_ADDR_0:
                    parsedAddress = (theData & 0x00FF) << 8;
                    setState(KW2EncodeStates.READ_ADDR_1);
                    break;
                case READ_ADDR_1:
                    parsedAddress |= theData & 0x00FF;
                    setState(KW2EncodeStates.READ_LENGTH);
                    break;
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
                    for (DataPoint dp : DataPoint.values()) {
                        if ((dp.getAddr() >= parsedAddress) && (dp.getAddr() < parsedAddress + theData)) {
                            sb.append(String.format("\t@%04x %s:%s (%s)", dp.getAddr(), dp.getGroup().getLabel(), dp.getLabel(), dp.decode(KW2Dummy.this).toString()));
                        }
                    }
                    log.info(sb.toString());
                    is.setSendData(result);
                    parsedAddress = 0;
                    setState(KW2EncodeStates.IDLE);
                }
                break;
                case WRITE_ADDR_0:
                    parsedAddress = (theData & 0x00FF) << 8;
                    setState(KW2EncodeStates.WRITE_ADDR_1);
                    break;
                case WRITE_ADDR_1:
                    parsedAddress |= theData & 0x00FF;
                    setState(KW2EncodeStates.WRITE_LENGTH);
                    break;
                case WRITE_LENGTH:
                    writeData = new byte[theData];
                    setState(KW2EncodeStates.WRITE_DATA);
                    break;
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
                        log.info(sb.toString());
                        is.setSendData(new byte[]{0});
                        parsedAddress = 0;
                        setState(KW2EncodeStates.IDLE);
                    }
                }
                break;

            }
        }

        private void setState(KW2EncodeStates newState) {
            log.debug(String.format("Change State from %s to %s", state, newState));
            state = newState;
        }
    }
    private static Logger log = LoggerFactory.getLogger(KW2Dummy.class);
    private byte[] memMap = new byte[0x010000];
    private MockInputStream is = new MockInputStream();
    private MockOutputStream os = new MockOutputStream();
    private KW2Encoder enc = new KW2Encoder();
    private final int default05Time;
    private final int afterDataSent05Time;
    private int next05Time;

    class MockInputStream extends InputStream {

        byte[] dataToSend;
        int sendPos = 0x0100;

        @Override
        public int read() throws IOException {
            if (sendPos < 0x00FF) {
                final int result = 0x00FF & dataToSend[sendPos++];
                if (dataToSend.length == sendPos) {
                    sendPos = 0x100;
                    next05Time = afterDataSent05Time;
                }
                return result;
            }
            try {
                Thread.sleep(next05Time);
                next05Time = default05Time;
            } catch (InterruptedException ex) {
            }
            return 0x05;
        }

        private void setSendData(byte[] dataToSend) {
            this.dataToSend = dataToSend;
            sendPos = 0;
        }
    }

    class MockOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            if (log.isTraceEnabled()) {
                log.trace(String.format("TX: 0x%02x", b & 0x00FF));
            }
            enc.parseByte(b & 0x00FF);
        }
    }

    public InputStream getInputStream() {
        return is;
    }

    public OutputStream getOutputStream() {
        return os;
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

}

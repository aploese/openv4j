/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j.protocolhandlers;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.sf.openv4j.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aploese
 */
public class ProtocolHandler {

    private final static Logger log = LoggerFactory.getLogger(ProtocolHandler.class);
    private InputStream is;
    private OutputStream os;
    private boolean closed;
    private Thread t;
    private StreamListener streamListener = new StreamListener();

    public void setReadRequest(DataContainer container) {
        streamListener.setReadRequest(container);
    }

    public void setWriteRequest(DataContainer container) {
        streamListener.setWriteRequest(container);
    }

    enum State {

        KW_IDLE,
        KW_WAIT_FOR_READ_RDY,
        KW_WAIT_FOR_READ_RESP,
        KW_COLLECT_READ_DATA,
        KW_WAIT_FOR_WRITE_RDY,
        KW_WAIT_FOR_WRITE_RESP,
        _300_IDLE;
    }

    private class StreamListener implements Runnable {

        private DataContainer container;
        private State state = State.KW_IDLE;
        private int bytesLeft;
        private byte[] received;
        private long timeoutTimeStamp;
        private int currentIndex;
        private DataBlock currentDataBlock;
        private int failedCount; // TODO impl
        private int retries;

        @Override
        public void run() {
            System.out.println("THREAD START " + closed);

            try {
                int theData;
                try {
                    while (!closed) {
                        try {
                            theData = is.read();
                            switch (state) {
                                case KW_IDLE:
                                    if (theData == -1) {
                                        log.debug("Idle timeout received");
                                    } else {
                                        log.info(String.format("Idle char received: %02x", theData & 0x00ff));
                                    }
                                    break;
                                case KW_WAIT_FOR_READ_RDY:
                                    if (theData == 0x05) {
                                        sendReadKWDataPackage(currentDataBlock.getBaseAddress(), currentDataBlock.getLength());
                                    }
                                    break;
                                case KW_WAIT_FOR_READ_RESP:
                                    if (!checkConnBroken(theData, state.KW_WAIT_FOR_READ_RDY)) {
                                        setState(State.KW_COLLECT_READ_DATA);
                                        dataReaded(theData);
                                    }
                                    break;
                                case KW_COLLECT_READ_DATA:
                                    if (!checkConnBroken(theData, state.KW_WAIT_FOR_READ_RDY)) {
                                        dataReaded(theData);
                                    }
                                    break;
                                case KW_WAIT_FOR_WRITE_RDY:
                                    if (theData == 0x05) {
                                        sendWriteKWDataPackage(currentDataBlock.getBaseAddress(), currentDataBlock.getBytes());
                                    }
                                    break;
                                case KW_WAIT_FOR_WRITE_RESP:
                                    if (!checkConnBroken(theData, State.KW_WAIT_FOR_WRITE_RDY)) {
                                        dataWritten(theData);
                                    }
                                    break;

                            }
                        } catch (NullPointerException npe) {
                            if (!closed) {
                                throw new RuntimeException(npe);
                            }
                        }
                    }
                    log.info("closing down - finish waiting for new data");
                } catch (IOException e) {
                    log.error("run()", e);
                } catch (Exception e) {
                    log.info("finished waiting for packages", e);

                }
            } finally {
            }
        }

        private void sendWriteKWDataPackage(int address, byte[] theData) throws IOException {
            sendKWPackageHeader(address, 0xf4, theData.length);
            os.write(theData);
            timeoutTimeStamp = System.currentTimeMillis();
            received = new byte[1];
            bytesLeft = received.length;
            setState(State.KW_WAIT_FOR_WRITE_RESP);
        }

        private void sendKWPackageHeader(int address, int command, int length) throws IOException {
            os.write(0x01);
            os.write(command);
            os.write((address >> 8) & 0xff);
            os.write(address & 0xff);
            os.write(length);
        }

        public void sendReadKWDataPackage(int address, int length) throws IOException {
            sendKWPackageHeader(address, 0xf7, length);
            timeoutTimeStamp = System.currentTimeMillis();
            received = new byte[length];
            bytesLeft = received.length;
            setState(State.KW_WAIT_FOR_READ_RESP);
        }

        public void setReadRequest(DataContainer container) {
            this.container = container;
            retries = 3;
            setCurrentIndex(0);
            setState(State.KW_WAIT_FOR_READ_RDY);
        }

        public void setWriteRequest(DataContainer container) {
            this.container = container;
            retries = 3;
            setCurrentIndex(0);
            setState(State.KW_WAIT_FOR_WRITE_RDY);
        }

        private void checkClosed(int theData) throws InterruptedException {
            if ((theData == -1) && closed) {
                throw new InterruptedException("Port Closed");
            }
        }

        private void setState(State state) {
            this.state = state;
        }

        private void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
            received = null;
            currentDataBlock = container.getDataBlock(currentIndex);
        }

        private void dataReaded(int theData) {
            if (theData != -1) {
                received[received.length - bytesLeft--] = (byte) theData;
                if (bytesLeft == 0) {
                    currentDataBlock.setBytesAtPos(0, received);
                    if (currentIndex + 1 < container.getDataBlockCount()) {
                        setCurrentIndex(currentIndex + 1);
                        setState(State.KW_WAIT_FOR_READ_RDY);
                    } else {
                        setState(state.KW_IDLE);
                        final Object o = container;
                        if (o != null) {
                            synchronized (o) {
                                o.notifyAll();
                            }
                        }
                    }
                }
            }
        }

        private void dataWritten(int theData) {
            if (theData != -1) {
                if (theData == 0) {
                    received = null;
                    bytesLeft = 0;
                    if (currentIndex + 1 < container.getDataBlockCount()) {
                        setCurrentIndex(currentIndex + 1);
                        setState(State.KW_WAIT_FOR_WRITE_RDY);
                    } else {
                        setState(state.KW_IDLE);
                        final Object o = container;
                        if (o != null) {
                            synchronized (o) {
                                o.notifyAll();
                            }
                        }
                    }
                } else {
                    // Try again
                    failedCount++;
                    setState(State.KW_WAIT_FOR_WRITE_RDY);
                }
            }
        }

        private boolean checkConnBroken(int theData, State state) {
            if (theData == -1) {
                retries--;
                if (retries == 0) {
                    log.info("Timeout received set No retries left finishing");
                    setState(State.KW_IDLE);
                    final Object o = container;
                    if (o != null) {
                        synchronized (o) {
                            o.notifyAll();
                        }
                    }
                    return true;
                } else {
                    log.info(String.format("Timeout received set state to %s Retries left %d", state.name(), retries));
                    setState(state);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public void close() throws InterruptedException {
        closed = true;
        Thread.sleep(100); //TODO wait?
        t.interrupt();
    }

    public static SerialPort openPort(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        // Obtain a CommPortIdentifier object for the port you want to open.
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

        // Open the port represented by the CommPortIdentifier object. Give
        // the open call a relatively long timeout of 30 seconds to allow
        // a different application to reliquish the port if the user
        // wants to.
        log.info("open port " + portName);
        SerialPort sPort = (SerialPort) portId.open(DataPoint.class.getName(), 30000);
        log.info("port opend " + portName);
        sPort.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_EVEN);
        sPort.enableReceiveTimeout(1000);
        sPort.setInputBufferSize(512);
        sPort.setOutputBufferSize(512);
        sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        return sPort;
    }

    public void setStreams(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        closed = false;
        start();
    }

    private void start() {
        closed = false;
        t = new Thread(streamListener);
        t.setDaemon(true);
        t.start();
    }
}

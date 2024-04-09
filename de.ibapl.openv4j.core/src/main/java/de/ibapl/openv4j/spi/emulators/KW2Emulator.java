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
package de.ibapl.openv4j.spi.emulators;

import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F8_0x08;
import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F0_0x02;
import de.ibapl.openv4j.api.devices.v200kw2.BoilerTemperatures_0x0800_0x1c;
import de.ibapl.openv4j.api.devices.v200kw2.DifferentiaitionHouses_0x777f_0x12;
import de.ibapl.openv4j.api.devices.v200kw2.HeatingCircuitDhwScheme_0x7700_0x01;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.DataContainer;
import de.ibapl.openv4j.spi.protocolhandlers.OpenV4JAdapter;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.Speed;
import de.ibapl.spsw.api.StopBits;
import de.ibapl.spsw.api.TimeoutIOException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KW2Emulator extends DataContainer {

    private static final Logger LOG = Logger.getLogger("d.i.ov.td.KW2Dummy");
    private final static Level LOG_LEVEL = Level.FINEST;

    private final DeviceTimings dt;
    private final SerialPortSocket serialPort;
    private final Thread t;
    private boolean closed;
    private final static int DEFAULT_IO_REQ_TIME_CYCLE = 2300;
    private final static int DEFAULT_IO_REQ_TIME_LIMIT = 100;

    /**
     * Creates a new KW2Dummy object.
     */
    public KW2Emulator(SerialPortSocket serialPort) throws IOException {
        super();
        serialPort.setSpeed(Speed._4800_BPS);
        serialPort.setDataBits(DataBits.DB_8);
        serialPort.setParity(Parity.EVEN);
        serialPort.setStopBits(StopBits.SB_2);
        serialPort.setFlowControl(FlowControl.getFC_NONE());
        serialPort.setTimeouts(100, 1000, 1000);

        this.serialPort = serialPort;

        readFromStream(KW2Emulator.class.getResourceAsStream("/emulator-memory-maps/V200KW2-MemMap.txt"), this);
        dt = new DeviceTimings();
        t = new Thread(dt);
        t.start();
    }

    public void close() throws IOException, InterruptedException {
        closed = true;
        if (serialPort != null) {
            serialPort.close();
        }
        Thread.sleep(100); //TODO wait?
        t.interrupt();
    }

    public final static void addDataBlocksFor_V200KW2(DataContainer dc) {
        dc.addMemArea(new EcnsysDeviceIdent_0x00F0_0x02(dc));
        dc.addMemArea(new EcnsysDeviceIdent_0x00F8_0x08(dc));
        dc.addMemArea(new BoilerTemperatures_0x0800_0x1c(dc));
        dc.addMemArea(new MemArea(dc, 0x083a, (short) 0x07));
        dc.addMemArea(new MemArea(dc, 0x0842, (short) 0x0c));
        dc.addMemArea(new MemArea(dc, 0x087f, (short) 0x0b));
        dc.addMemArea(new MemArea(dc, 0x088a, (short) 0x25));

        dc.addMemArea(new MemArea(dc, 0x1010, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x1030, (short) 0x20));
        dc.addMemArea(new MemArea(dc, 0x1060, (short) 0x20));

        dc.addMemArea(new MemArea(dc, 0x2000, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x2028, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x2100, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x2128, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x2200, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x2228, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x2301, (short) 0x18));
        dc.addMemArea(new MemArea(dc, 0x2500, (short) 0x22));
//Mixer start?
        dc.addMemArea(new MemArea(dc, 0x2309, (short) 0x08));
        dc.addMemArea(new MemArea(dc, 0x2311, (short) 0x08));
//Mixer end
        dc.addMemArea(new MemArea(dc, 0x2535, (short) 0x15));
        dc.addMemArea(new MemArea(dc, 0x27a0, (short) 0x29));
        dc.addMemArea(new MemArea(dc, 0x27e1, (short) 0x12));
        dc.addMemArea(new MemArea(dc, 0x2900, (short) 0x07));

        dc.addMemArea(new MemArea(dc, 0x3000, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x3028, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x3100, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x3128, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x3200, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x3228, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x3301, (short) 0x18));
        dc.addMemArea(new MemArea(dc, 0x3309, (short) 0x08));
        dc.addMemArea(new MemArea(dc, 0x3311, (short) 0x08));
        dc.addMemArea(new MemArea(dc, 0x3500, (short) 0x22));
        dc.addMemArea(new MemArea(dc, 0x3535, (short) 0x15));
        dc.addMemArea(new MemArea(dc, 0x37a0, (short) 0x2a));
        dc.addMemArea(new MemArea(dc, 0x37e1, (short) 0x12));
        dc.addMemArea(new MemArea(dc, 0x3900, (short) 0x07));

        dc.addMemArea(new MemArea(dc, 0x4000, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x4028, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x4100, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x4128, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x4200, (short) 0x28));
        dc.addMemArea(new MemArea(dc, 0x4228, (short) 0x10));
        dc.addMemArea(new MemArea(dc, 0x4301, (short) 0x18));
        dc.addMemArea(new MemArea(dc, 0x4309, (short) 0x08));
        dc.addMemArea(new MemArea(dc, 0x4311, (short) 0x08));
        dc.addMemArea(new MemArea(dc, 0x4500, (short) 0x22));
        dc.addMemArea(new MemArea(dc, 0x4535, (short) 0x15));
        dc.addMemArea(new MemArea(dc, 0x47a0, (short) 0x2a));
        dc.addMemArea(new MemArea(dc, 0x47e1, (short) 0x12));
        dc.addMemArea(new MemArea(dc, 0x4900, (short) 0x07));

        dc.addMemArea(new MemArea(dc, 0x5500, (short) 0x29));
        dc.addMemArea(new MemArea(dc, 0x5702, (short) 0x26));
        dc.addMemArea(new MemArea(dc, 0x5702, (short) 0x29));
        dc.addMemArea(new MemArea(dc, 0x5732, (short) 0x02));

        dc.addMemArea(new MemArea(dc, 0x6300, (short) 0x01));
        dc.addMemArea(new MemArea(dc, 0x6500, (short) 0x0b));
        dc.addMemArea(new MemArea(dc, 0x6755, (short) 0x21));

        dc.addMemArea(new MemArea(dc, 0x7330, (short) 0x06));
        dc.addMemArea(new MemArea(dc, 0x7500, (short) 0x01));
        dc.addMemArea(new MemArea(dc, 0x7500, (short) 0x03));
        dc.addMemArea(new MemArea(dc, 0x7500, (short) 0x2b));
        dc.addMemArea(new MemArea(dc, 0x7507, (short) 0x24));
        dc.addMemArea(new MemArea(dc, 0x752b, (short) 0x24));
        dc.addMemArea(new MemArea(dc, 0x754f, (short) 0x29));
        dc.addMemArea(new MemArea(dc, 0x754f, (short) 0x2c));
        dc.addMemArea(new HeatingCircuitDhwScheme_0x7700_0x01(dc));
        dc.addMemArea(new MemArea(dc, 0x777c, (short) 0x01));
        dc.addMemArea(new DifferentiaitionHouses_0x777f_0x12(dc));
        dc.addMemArea(new MemArea(dc, 0x777f, (short) 0x17));

        dc.addMemArea(new MemArea(dc, 0xf000, (short) 0x10));
    }
    private final TreeMap<Integer, MemArea> memAreasRead = new TreeMap<>();
    private final TreeMap<Integer, MemArea> memAreasWrite = new TreeMap<>();

    private class DeviceTimings implements Runnable {

        @Override
        public void run() {
            final ByteBuffer buff = ByteBuffer.allocateDirect(512);
            long lastTimestampDataWasSent = System.currentTimeMillis();
            while (!closed) {
                try {
                    if (System.currentTimeMillis() - lastTimestampDataWasSent > DEFAULT_IO_REQ_TIME_LIMIT) {
                        Thread.sleep(DEFAULT_IO_REQ_TIME_CYCLE);
                    }
                    int count;
                    while ((count = serialPort.getInBufferBytesCount()) > 0) {
                        buff.position(0).limit(count);
                        serialPort.read(buff);
                        //discart read data
                    }
                    buff.position(0).put(OpenV4JAdapter.IO_TIME_SLOT).flip();
                    LOG.log(LOG_LEVEL, "send IO_TIME_SLOT");
                    serialPort.write(buff);
                    lastTimestampDataWasSent = System.currentTimeMillis();
                    buff.position(0).limit(1);
                    try {
                        if (serialPort.read(buff) == 1) {
                            //TODO measure time for timeslot?
                            buff.flip();
                            if (buff.get() == OpenV4JAdapter.KW2_ACK_IO_TIME_SLOT) {
                                LOG.log(LOG_LEVEL, "received KW2_ACK_IO_TIME_SLOT");
                                while (!closed) {
                                    buff.clear().limit(1);
                                    try {
                                        serialPort.read(buff);
                                        if (System.currentTimeMillis() - DEFAULT_IO_REQ_TIME_CYCLE > lastTimestampDataWasSent) {
                                            //some data received, but too late....
                                            LOG.log(LOG_LEVEL, "Time running out, waiting for command");
                                            // break the loop and start with the outer loop;
                                            break;
                                        }
                                    } catch (TimeoutIOException tioe) {
                                        //No data received
                                        LOG.log(LOG_LEVEL, "Timeout, waiting for command");
                                        // break the loop and start with the outer loop;
                                        break;
                                    }
                                    //ok command data was read
                                    buff.flip();
                                    final byte command = buff.get();
                                    switch (command) {

                                        case OpenV4JAdapter.KW2_VIRTUAL_READ_REQUEST -> {
                                            buff.position(0).limit(3);
                                            try {
                                                serialPort.read(buff);
                                                if (System.currentTimeMillis() - DEFAULT_IO_REQ_TIME_CYCLE > lastTimestampDataWasSent) {
                                                    LOG.log(LOG_LEVEL, "Time running out, waiting for command");
                                                    // break the loop and start with the outer loop;
                                                    break;
                                                }
                                            } catch (TimeoutIOException tioe) {
                                                LOG.log(LOG_LEVEL, "Timeout, waiting for command");
                                                // break the loop and start with the outer loop;
                                                break;
                                            }
                                            buff.flip();
                                            final int parsedAddress = buff.getShort() & 0xFFFF;
                                            final short dataLength = (short) (buff.get() & 0xff);

                                            final MemArea area = prepareReadArea(parsedAddress, dataLength);
                                            buff.clear();
                                            buff.put(buffer, parsedAddress, dataLength).flip();
                                            serialPort.write(buff);
                                            lastTimestampDataWasSent = System.currentTimeMillis();
                                            if (area.getClass() == MemArea.class) {
                                                LOG.log(LOG_LEVEL, String.format("KW2_VIRTUAL_READ_REQUEST addr: 0x%04x length: %d handled", parsedAddress, dataLength));
                                            } else {
                                                LOG.log(LOG_LEVEL, String.format("KW2_VIRTUAL_READ_REQUEST %s handled", area.getClass().getSimpleName()));
                                            }
                                        }
                                        case OpenV4JAdapter.KW2_VIRTUAL_WRITE_REQEST -> {
                                            buff.position(0).limit(3);
                                            try {
                                                serialPort.read(buff);
                                                if (System.currentTimeMillis() - DEFAULT_IO_REQ_TIME_CYCLE > lastTimestampDataWasSent) {
                                                    LOG.log(LOG_LEVEL, "Time running out, waiting for command");
                                                    // break the loop and start with the outer loop;
                                                    break;
                                                }
                                            } catch (TimeoutIOException tioe) {
                                                LOG.log(LOG_LEVEL, "Timeout, waiting for command");
                                                // break the loop and start with the outer loop;
                                                break;
                                            }
                                            buff.flip();
                                            final int parsedAddress = buff.getShort() & 0xFFFF;
                                            final short dataLength = (short) (buff.get() & 0xff);
                                            buff.clear().limit(dataLength);
                                            try {
                                                serialPort.read(buff);
                                            } catch (TimeoutIOException tioe) {
                                                // break the loop and start with the outer loop;
                                                break;
                                            }
                                            if (!buff.hasRemaining()) {
                                                buff.flip();
                                                final MemArea area = prepareWriteArea(parsedAddress, dataLength);
                                                buff.get(buffer, parsedAddress, dataLength);
                                                buff.clear().put(OpenV4JAdapter.RESPONSE_WRITE_OK).flip();
                                                serialPort.write(buff);
                                                lastTimestampDataWasSent = System.currentTimeMillis();
                                                if (area.getClass() == MemArea.class) {
                                                    LOG.log(LOG_LEVEL, String.format("KW2_VIRTUAL_WRITE_REQUEST addr: 0x%04x length: %d handled", parsedAddress, dataLength));
                                                } else {
                                                    LOG.log(LOG_LEVEL, String.format("KW2_VIRTUAL_WRITE_REQUEST %s handled", area.getClass().getSimpleName()));
                                                }
                                            } else {
                                                LOG.log(LOG_LEVEL, String.format("KW2_VIRTUAL_WRITE_REQUEST addr: 0x%04x length: %d Data missing -> No response", parsedAddress, dataLength));
                                            }
                                        }
                                        case OpenV4JAdapter.GWG_VIRTUAL_READ_REQUEST -> {
                                            buff.position(0).limit(2);
                                            try {
                                                serialPort.read(buff);
                                                if (System.currentTimeMillis() - DEFAULT_IO_REQ_TIME_CYCLE > lastTimestampDataWasSent) {
                                                    LOG.log(LOG_LEVEL, "Time running out, waiting for command");
                                                    // break the loop and start with the outer loop;
                                                    break;
                                                }
                                            } catch (TimeoutIOException tioe) {
                                                LOG.log(LOG_LEVEL, "Timeout, waiting for command");
                                                // break the loop and start with the outer loop;
                                                break;
                                            }
                                            buff.flip();
                                            final short parsedAddress = (short) (buff.get() & 0xFF);
                                            final short dataLength = (short) (buff.get() & 0xff);
                                            //? logVirtualReadBlock((parsedAddress << 8) | dataLength);
                                            buff.clear();
                                            final MemArea area = prepareReadArea(parsedAddress, dataLength);
                                            buff.put(buffer, parsedAddress, dataLength).flip();
                                            serialPort.write(buff);
                                            lastTimestampDataWasSent = System.currentTimeMillis();
                                            if (area.getClass() == MemArea.class) {
                                                LOG.log(LOG_LEVEL, String.format("GWG_VIRTUAL_READ_REQUEST addr: 0x%04x length: %d handled", parsedAddress, dataLength));
                                            } else {
                                                LOG.log(LOG_LEVEL, String.format("GWG_VIRTUAL_READ_REQUEST %s handled", area.getClass().getSimpleName()));
                                            }
                                        }
                                        case OpenV4JAdapter.GWG_SYNC -> {
                                            //Keep alive???
                                            lastTimestampDataWasSent = System.currentTimeMillis();
                                        }
                                        default ->
                                            LOG.log(Level.SEVERE, String.format("received Unknown command: 0x%02x", command));

                                    }
                                }
                            }
                        } else {
                            //no-op we dont get OpenV4JAdapter.KW2_ACK_IO_TIME_SLOT so just discard any read data
                        }
                    } catch (TimeoutIOException tioe) {
                        //no-op
                    }
                } catch (AsynchronousCloseException ace) {
                    //no-op
                } catch (Throwable t) {
                    System.err.println("Throwable: " + t);
                    t.printStackTrace(System.err);
                }
            }
        }

    }

    public Iterable<MemArea> readAreas() {
        return memAreasRead.values();
    }

    public Iterable<MemArea> writeAreas() {
        return memAreasWrite.values();
    }

    private MemArea createMemArea(int address, short size) {
        return switch (MemArea.computeKey(address, size)) {
            case 0x00f002 ->
                new EcnsysDeviceIdent_0x00F0_0x02(this);
            case 0x00f808 ->
                new EcnsysDeviceIdent_0x00F8_0x08(this);
            case 0x08001c ->
                new BoilerTemperatures_0x0800_0x1c(this);
            case 0x770001 ->
                new HeatingCircuitDhwScheme_0x7700_0x01(this);
            case 0x777f ->
                new DifferentiaitionHouses_0x777f_0x12(this);
            default ->
                new MemArea(this, address, size);
        };
    }

    private MemArea prepareReadArea(int address, short dataLength) {
        MemArea result = memAreasRead.get(MemArea.computeKey(address, dataLength));
        if (result == null) {
            result = getMemArea(MemArea.computeKey(address, dataLength));
            if (result == null) {
                result = createMemArea(address, dataLength);
                addMemArea(result);
            }
            memAreasRead.put(MemArea.computeKey(address, dataLength), result);
        }
        return result;
    }

    private MemArea prepareWriteArea(int address, short dataLength) {
        MemArea result = memAreasWrite.get(MemArea.computeKey(address, dataLength));
        if (result == null) {
            result = getMemArea(MemArea.computeKey(address, dataLength));
            if (result == null) {
                result = createMemArea(address, dataLength);
                addMemArea(result);
            }
            memAreasWrite.put(MemArea.computeKey(address, dataLength), result);
        }
        return result;
    }
}

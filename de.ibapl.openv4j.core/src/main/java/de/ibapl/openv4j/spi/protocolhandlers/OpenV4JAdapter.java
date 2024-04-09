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
package de.ibapl.openv4j.spi.protocolhandlers;

import de.ibapl.openv4j.api.Devices;
import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F0_0x02;
import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F8_0x08;
import de.ibapl.spsw.api.DataBits;
import de.ibapl.spsw.api.FlowControl;
import de.ibapl.spsw.api.Parity;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.Speed;
import de.ibapl.spsw.api.StopBits;
import de.ibapl.spsw.api.TimeoutIOException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenV4JAdapter implements AutoCloseable {

    private static final Logger log = Logger.getLogger("d.i.ov.s.OpenV4JAdapter");
    private final ByteChannel channel;
    public final static byte IO_TIME_SLOT = 0x05;
    public final static byte KW2_ACK_IO_TIME_SLOT = 0x01;
    public final static byte GWG_SYNC = 0x04;
    public final static byte GWG_VIRTUAL_READ_REQUEST = (byte) 0xc7;
    public final static byte KW2_VIRTUAL_READ_REQUEST = (byte) 0xf7;
    public final static byte KW2_VIRTUAL_WRITE_REQEST = (byte) 0xf4;
    public final static byte KW2_GFA_READ_REQUEST = 0x6b;
    public final static byte KW2_GFA_WRITE_REQEST = 0x68;
    public final static byte KW2_PROCESS_READ_REQEST = 0x7b;
    public final static byte KW2_PROCESSL_WRITE_REQEST = 0x78;
    public final static byte RESPONSE_WRITE_OK = 0x00;
    public final static byte[] INIT_SEQUENCE = new byte[]{(byte) 0x16, 0x00, 0x00};

    private final ByteBuffer buffer = ByteBuffer.allocateDirect(512);

    public static String toHexASCII(byte[] theData) {
        StringBuilder sb = new StringBuilder(theData.length * 3);

        for (int i : theData) {
            sb.append(String.format("%02x ", i & 0xff));
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public void sendKwVirtualWriteRequest(MemArea area)
            throws IOException {
        buffer.clear();
        buffer.put(KW2_VIRTUAL_WRITE_REQEST);
        buffer.putShort((short) area.getBaseAddress());
        buffer.put((byte) (area.getSize() & 0xff)).flip();
        area.copyBytesTo(buffer);
        buffer.flip();
        channel.write(buffer);
        if (buffer.hasRemaining()) {
            throw new RuntimeException(String.format("actual read size (%i) mismatch expected %i", area.getSize(), buffer.position()));
        }
        buffer.clear().limit(1);
        channel.read(buffer);
        final byte b = buffer.flip().get();
        if (b != RESPONSE_WRITE_OK) {
            throw new RuntimeException(String.format("Write result 0x00 expected, but got 0x%02x", b));
        }
    }

    public void sendKwVirtualReadRequest(MemArea area) throws IOException {
        buffer.clear();
        buffer.put(KW2_VIRTUAL_READ_REQUEST);
        buffer.putShort((short) area.getBaseAddress());
        buffer.put((byte) (area.getSize() & 0xff)).flip();
        channel.write(buffer);
        if (buffer.hasRemaining()) {
            throw new RuntimeException("Cant write KW2_VIRTUAL_READ_REQUEST command");
        }
        buffer.clear().limit(area.getSize());
        channel.read(buffer);
        if (buffer.hasRemaining()) {
            throw new RuntimeException(String.format("actual read size (%i) mismatch expected %i", area.getSize(), buffer.position() - area.getBaseAddress()));
        }
        buffer.flip();
        area.copyBytesFrom(buffer);
    }

    public void sendKwVirtualWriteRequest(MemoryImage.UpdateEntry entry)
            throws IOException {
        buffer.clear();
        buffer.put(KW2_VIRTUAL_WRITE_REQEST);
        buffer.putShort((short) entry.getAddress());
        buffer.put((byte) (entry.getSize() & 0xff)).flip();
        entry.copyBytesTo(buffer);
        buffer.flip();
        channel.write(buffer);
        if (buffer.hasRemaining()) {
            throw new RuntimeException(String.format("actual read size (%i) mismatch expected %i", entry.getSize(), buffer.position()));
        }
        buffer.clear().limit(1);
        channel.read(buffer);
        final byte b = buffer.flip().get();
        if (b != RESPONSE_WRITE_OK) {
            throw new RuntimeException(String.format("Write result 0x00 expected, but got 0x%02x", b));
        }
    }

    public void sendPendingUpdates(MemoryImage mi) throws IOException {
        waitForTimeSlotAndSendACK();
        while (!mi.pendingUpdateEntries.isEmpty()) {
            sendKwVirtualWriteRequest(mi.pendingUpdateEntries.getFirst());
            mi.pendingUpdateEntries.removeFirst();
        }
    }

    /**
     * Creates a new ProtocolHandler and initializes the serial port to
     * 4800,8,e,2.
     *
     * @param serialPortSocket
     */
    public OpenV4JAdapter(SerialPortSocket serialPortSocket) throws IOException {
        super();
        if (!serialPortSocket.isOpen()) {
            throw new IllegalStateException("serial port " + serialPortSocket.getPortName() + " is not open");
        }
        log.log(Level.INFO, "configure port {0}", serialPortSocket);

        serialPortSocket.setSpeed(Speed._4800_BPS);
        serialPortSocket.setDataBits(DataBits.DB_8);
        serialPortSocket.setParity(Parity.EVEN);
        serialPortSocket.setStopBits(StopBits.SB_2);
        serialPortSocket.setFlowControl(FlowControl.getFC_NONE());
        serialPortSocket.setTimeouts(100, 1000, 1000);

        this.channel = serialPortSocket;
    }

    public void waitForTimeSlotAndSendACK() throws IOException {
        waitForTimeSlot();
        buffer.clear().put(KW2_ACK_IO_TIME_SLOT).flip();
        //aknownledge that we want to communicate
        channel.write(buffer);
    }

    protected void waitForTimeSlot() throws IOException {
        //clear out in buffer
        do {
            buffer.clear();
            try {
                channel.read(buffer);
            } catch (TimeoutIOException tioe) {
                break;
            }
            buffer.flip();
        } while (buffer.limit() == buffer.capacity());
        // we wait for the IO_TIME_SLOT
        buffer.position(0).limit(1);
        while (true) {
            try {
                channel.read(buffer);
                break;
            } catch (TimeoutIOException tioe) {

            }
        }

        final byte b = buffer.flip().get();
        if (b != IO_TIME_SLOT) {
            throw new RuntimeException(String.format("IO_TIME_SLOT (0x05) expected, but got 0x%02x", b));
        }
    }

    public void sendInit() throws IOException {
        buffer.clear();
        buffer.put(GWG_SYNC);
        buffer.flip();
        channel.write(buffer);
        for (int i = 0; i < 3; i++) {
            waitForTimeSlot();
            buffer.clear();
            buffer.put(INIT_SEQUENCE);
            buffer.flip();
            channel.write(buffer);
        }
        waitForTimeSlotAndSendACK();
        buffer.clear();
        buffer.put(GWG_VIRTUAL_READ_REQUEST);
        buffer.put((byte) 0xf8);
        buffer.put((byte) 0x04);
        buffer.put(GWG_SYNC);
        buffer.flip();
        channel.write(buffer);
        buffer.clear().limit(4);
        channel.read(buffer);
        buffer.flip();
        final short systemID = buffer.getShort();
        final byte hwIndex = buffer.get();
        final byte swIndex = buffer.get();

        //Für V200KW2
        //Lesen der Anlagenstruktur (und Aktualwerte?)
        //vread 00f0 02 -> 00ff
        //vread 00f8 08 -> 20980002ffffffff Deviceid mit allen index
        //vread f000 10 -> ?not fetched yet
        //Boiler
        //vread 0842 0c -> 000001000000000001000100
        //vread 0800 1c -> 6900a402a901c800c800c800d7016700a502a901c800c800c800d501
        //vread 088a 25 -> 609200002009120304164317c800c800c800030303c8ffab060a1e0000d48ee1000a1e0000
        //A1M1
        //vread 2301 18 -> 040000000e14031419700101040000001970010104000000
        //vread 2500 22 -> 050000000000460000020001c800c800004600ffffffffffffffffffffffffff0005
        //vread 2535 15 -> 0200000000000a000000002004ffffffffffffffff
        //vread 2900 07 -> 00000000000000
        //M2
        //vread 3301 18 -> 0300000b09150f1519700101040000001970010104000000
        //vread 3500 22 -> 0202519fb900c70101030101d200000000c701ffffffffffffffffffffffffff0202
        //vread 3535 15 -> 0200000000000a000096002001ffffffffffffffff
        //vread 3900 07 -> de01c800000201
        //
        //vread 5500 29 -> 8e021702f901350271025e01a4015e01a4018810000000000000000001010000ff00004f0063005500
        //vread 5702 29 -> 000101084effffffffffffffffff14141406000f060c2c0106ffffffff00ff00000c01ff3c00000000
        //
        //vread 6300 01 -> 32
        //vread 6500 0b -> f401bc0200009501fc0100
        //
        //vread 7507 24 -> 0f2006091202084240c62006010403150848d1200508070714ffffd12005080707141320
        //vread 752b 24 -> d12005080707140032d12005080606202640d12005ffffffffffff002005080606193112
        //vread 754f 2c -> 0020050806061931120020050806061931ff0000000000000000000000559f2643c6543e007a7c0c01000000
        //vread 7700 01 -> 04
        //vread 777f 17 -> 0101010305070a0507ff01af00110204ff400700000000
        //
        //vread 00f8 04 -> 20980002
        //vread 083a 07 -> 00000002020200
        //vread 087f 0b -> 00000101000100d48ee100
        //
        //vread 1010 10 -> 37343530363933303030303030303030
        //vread 1030 20 -> e100232a232a232a3700000000000000ffffffffffffffffb8f8f8f8f8f8f800
        //vread 1060 20 -> e100232a232a232a3700000000000000ffffffffffffffffb8f8f8f8f8f8f800
        //A1M1
        //vread 2000 28 -> 30b0ffffffffffff30b0ffffffffffff30b0ffffffffffff30b0ffffffffffff30b0ffffffffffff
        //vread 2028 10 -> 30b0ffffffffffff30b0ffffffffffff
        //vread 2100 28 -> 30a0ffffffffffff30a0ffffffffffff30a0ffffffffffff30a0ffffffffffff30a0ffffffffffff
        //vread 2128 10 -> 30a0ffffffffffff30a0ffffffffffff
        //vread 2200 28 -> 30389098ffffffff30389098ffffffff30389098ffffffff30389098ffffffff30389098ffffffff
        //vread 2228 10 -> 30389098ffffffff30389098ffffffff
        //vread 27a0 29 -> 00ff020200052400ff00ffffffffffff00000800ff0000000a00ffffffffffff000000ff01144b001f
        //vread 27e1 12 -> 0132ffff0064140014ffffffffffff000000
        //M2
        //vread 3000 28 -> 28a8ffffffffffff28a8ffffffffffff28a8ffffffffffff28a8ffffffffffff28a8ffffffffffff
        //vread 3028 10 -> 28a8ffffffffffff28a8ffffffffffff
        //vread 3100 28 -> 2bb0ffffffffffff2bb0ffffffffffff2bb0ffffffffffff2bb0ffffffffffff2bb0ffffffffffff
        //vread 3128 10 -> 2bb0ffffffffffff2bb0ffffffffffff
        //vread 3200 28 -> 2bb0ffffffffffff2bb0ffffffffffff2bb0ffffffffffff2bb0ffffffffffff2bb0ffffffffffff
        //vread 3228 10 -> 2bb0ffffffffffff2bb0ffffffffffff
        //vread 37a0 2a -> 00ff0a0400011400ff00ffffffffffff00000800ff0000000a00ffffffffffff000000ff01194b001f00
        //vread 37e1 12 -> 0132ffff0050140014ffffffffffff000000
        //
        //vread 5732 02 -> 4600
        //
        //vread 6755 21 -> 0100ff000affffffffffff14000a0002ff04ff0807ffffffffffff000000020500
        //
        //vread 7330 06 -> 070000000000
        //vread 7500 03 -> 001800
        //vread 777c 01 -> ff
        //
        //
        //
        //
        //
        //Update: Aktuelle Werte alle 10s
        //vread 777f 12 -> 0101010305070a0507ff01af00110204ff40
        //vread 7700 01 -> 04
        //vread 754f 29 -> 0020050806061931120020050806061931ff0000000000000000000000559f2643c6543e007a7c0c01
        //vread 752b 24 -> d12005080707140032d12005080606202640d12005ffffffffffff002005080606193112
        //vread 7507 24 -> 0f2006091202084240c62006010403150848d1200508070714ffffd12005080707141320
        //vread 6500 0b -> f401bc0200009501fc0100
        //vread 6300 01 -> 32
        //vread 5702 26 -> 000101084effffffffffffffffff14141406000f060c2c0106ffffffff00ff00000c01ff3c00
        //vread 5500 29 -> 8e021702f901350271025e01a4015e01a4018810000000000000000001010000ff00004f0063005500
        //vread 3900 07 -> de01c800000201
        //vread 3535 15 -> 0200000000000a000096002001ffffffffffffffff
        //vread 3500 22 -> 0202519fb900c70101030101d200000000c701ffffffffffffffffffffffffff0202
        //vread 3301 18 -> 0300000b09150f1519700101040000001970010104000000
        //vread 2900 07 -> 00000000000000
        //vread 2535 15 -> 0200000000000a000000002004ffffffffffffffff
        //vread 2500 22 -> 050000000000460000020001c800c800004600ffffffffffffffffffffffffff0005
        //vread 2301 18 -> 040000000e14031419700101040000001970010104000000
        //vread 088a 25 -> 609200002009120304164317c800c800c800030303c8ffab060a1e0000d48ee1000a1e0000
        //vread 0842 0c -> 000001000000000001000100
        //vread 0800 1c -> 6900a402a901c800c800c800d7016700a502a901c800c800c800d501
        //
        //Update Anlagenstruktur???
        //vread 5502 25 -> 1702f901350271025e01a4015e01a4018810000000000000000001010000ff00004f006300
        //vread 2906 01 -> 00
        //vread 2535 01 -> 02
        //vread 2500 16 -> 050000000000460000020001c800c800004600ffffff
        //vread 2302 17 -> 0000000e14031419700101040000001970010104000000
        //vread 088a 25 -> 609200002009120304164317c800c800c800030303c8ffab060a1e0000d48ee1000a1e0000
        //vread 0842 08 -> 0000010000000000
        //vread 0810 08 -> a502a901c800c800
        //vread 6500 0b -> f401bc0200009501fc0100
        //vread 6300 01 -> 32
        //vread 3900 07 -> de01c800000201
        //vread 3535 01 -> 02
        //vread 3500 16 -> 0202519fb900c70101030101d200000000c701ffffff
        //vread 3302 17 -> 00000b09150f1519700101040000001970010104000000
    }

    public OpenV4JAdapter(ByteChannel channel) throws IOException {
        super();
        if (!channel.isOpen()) {
            throw new IllegalStateException("The channel " + channel.toString() + " is not open");
        }
        this.channel = channel;
    }

    @Override
    public synchronized void close() throws IOException {
        channel.close();
    }

    public Devices detectConnectedDevice(MemoryImage cache) throws IOException {
        EcnsysDeviceIdent_0x00F8_0x08 ecnsysDeviceIdent_0x00F8 = new EcnsysDeviceIdent_0x00F8_0x08(cache);
        EcnsysDeviceIdent_0x00F0_0x02 ecnsysDeviceIdent_0x00F0 = new EcnsysDeviceIdent_0x00F0_0x02(cache);
        waitForTimeSlotAndSendACK();
        sendKwVirtualReadRequest(ecnsysDeviceIdent_0x00F8);
        sendKwVirtualReadRequest(ecnsysDeviceIdent_0x00F0);
        return Devices.decode(ecnsysDeviceIdent_0x00F8, ecnsysDeviceIdent_0x00F0);
    }

}

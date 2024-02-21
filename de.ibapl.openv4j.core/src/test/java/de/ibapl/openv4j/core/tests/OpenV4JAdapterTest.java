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
package de.ibapl.openv4j.core.tests;

import de.ibapl.openv4j.api.Devices;
import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F0_0x02;
import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F8_0x08;
import de.ibapl.openv4j.spi.emulators.KW2Emulator;
import de.ibapl.openv4j.spi.protocolhandlers.DataContainer;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;
import de.ibapl.openv4j.spi.protocolhandlers.OpenV4JAdapter;
import de.ibapl.spsw.api.SerialPortSocket;
import de.ibapl.spsw.api.SerialPortSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ServiceLoader;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class OpenV4JAdapterTest {

    private SerialPortSocket spsAdapter;
    private SerialPortSocket spsEmulator;
    private KW2Emulator emulator;
    private OpenV4JAdapter openV4JAdapter;

    @BeforeEach
    public void setUp() throws Exception {
        ServiceLoader<SerialPortSocketFactory> spsFactory = ServiceLoader.load(SerialPortSocketFactory.class);
        for (SerialPortSocketFactory spsf : spsFactory) {
            try {
                spsEmulator = spsf.open(SerialPortSocketFactory.URI_AUTORITY_MEMORY + "://KW2Emulator:B");
                spsAdapter = spsf.open(SerialPortSocketFactory.URI_AUTORITY_MEMORY + "://KW2Emulator:A");
                if (spsEmulator == null) {
                    throw new NullPointerException("spsEmulator is Null");
                }
                if (spsAdapter == null) {
                    throw new NullPointerException("spsAdapter is Null");
                }
                emulator = new KW2Emulator(spsEmulator);
                openV4JAdapter = new OpenV4JAdapter(spsAdapter);
                break;
            } catch (Exception e) {
                fail(e);
            }
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        openV4JAdapter.close();
        openV4JAdapter = null;
        emulator.close();
        emulator = null;
        spsAdapter = null;
        spsEmulator = null;
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

    /**
     * Test of sendReadRequest method, of class OpenV4JAdapter.
     *
     */
    @Test
    public void testDetectConnectedDevice() throws Exception {
        System.out.println("testDetectDevice");
        MemoryImage.readFromStream(KW2Emulator.class.getResourceAsStream("/emulator-memory-maps/V200KW2-MemMap.txt"), emulator);

        DataContainer cache = new DataContainer();

        Devices dev = openV4JAdapter.detectConnectedDevice(cache);

        assertEquals(Devices.V200KW2, dev);
    }

    /**
     * Test of sendReadRequest method, of class OpenV4JAdapter.
     *
     */
    @Test
    public void testSendReadRequest() throws Exception {
        System.out.println("testSendReadRequest");
        MemoryImage.readFromStream(KW2Emulator.class.getResourceAsStream("/emulator-memory-maps/V200KW2-MemMap.txt"), emulator);

        MemoryImage cache = new MemoryImage();

        openV4JAdapter.waitForTimeSlotAndSendACK();
        EcnsysDeviceIdent_0x00F8_0x08 expected = new EcnsysDeviceIdent_0x00F8_0x08(emulator);
        EcnsysDeviceIdent_0x00F8_0x08 actual = new EcnsysDeviceIdent_0x00F8_0x08(cache);

        openV4JAdapter.sendKwVirtualReadRequest(actual);

        assertEquals(expected.getDeviceIdentification(), actual.getDeviceIdentification());
    }

    @Test
    @Disabled
    public void testSendWriteRequest() throws Exception {
        /*
        System.out.println("testSendWriteRequest");

        DataContainer container = new SimpleDataContainer();

        container.addToDataContainer(DataPoint.M2_CONFIG_SHIFT);
        container.setByte(DataPoint.M2_CONFIG_SHIFT, (byte) 22);
        openV4JAdapter.sendKwVirtualWriteRequest(container);

        assertEquals((byte) 22, emulator.getByte(DataPoint.M2_CONFIG_SHIFT));
         */
    }
}

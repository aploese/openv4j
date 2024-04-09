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
package de.ibapl.openv4j.api.devices.generic;

import de.ibapl.openv4j.api.tag.Name;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;

/**
 *
 * @author aploese
 */
public class EcnsysDeviceIdent_0x00F8_0x08 extends MemArea {

    public EcnsysDeviceIdent_0x00F8_0x08(MemoryImage mem) {
        super(mem, 0x00f8, (short) 0x08);
    }

    @Name("GRUPPENIDENTIFIKATION")
    public byte getGroupIdentification() {
        return mem.getByte(baseAddress);
    }

    @Name("REGLERIDENTIFIKATION")
    public byte getControllerIdentification() {
        return mem.getByte(baseAddress + 1);
    }

    public short getDeviceIdentification() {
        return mem.getShortHex(baseAddress);
    }

    @Name("HARDWAREINDEX")
    public byte getHardwareIndex() {
        return mem.getByte(baseAddress + 2);
    }

    @Name("SOFTWAREINDEX")
    public byte getSoftwareIndex() {
        return mem.getByte(baseAddress + 3);
    }

    @Name("VERSION_LDA")
    public byte getVersionLDA() {
        return mem.getByte(baseAddress + 4);
    }

    @Name("VERSION_RDA")
    public byte getVersionRDA() {
        return mem.getByte(baseAddress + 5);
    }

    @Name("VERSION_SW1")
    public byte getVersionSW1() {
        return mem.getByte(baseAddress + 6);
    }

    @Name("VERSION_SW2")
    public byte getVersionSW2() {
        return mem.getByte(baseAddress + 7);
    }
}

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
package de.ibapl.openv4j.api;

import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F0_0x02;
import de.ibapl.openv4j.api.devices.generic.EcnsysDeviceIdent_0x00F8_0x08;

/**
 *
 */
public enum Devices {
    GWG_BT2(0x2054, Protocol.GWG),
    GWG_VBEM(0x2053, Protocol.GWG),
    GWG_VBT(0x2053, Protocol.GWG),
    GWG_VWMS(0x2053, Protocol.GWG),
    HV_GWG(0x2000, Protocol.GWG),
    HV_V050(0x2000, Protocol.GWG),
    HV_V300KW3(0x2000, Protocol.GWG),
    V050HK1M(0x20b4, Protocol.KW),
    V050HK1S(0x20ac, Protocol.KW),
    V050HK1W(0x20aa, Protocol.KW),
    V050HK3S(0x20ad, Protocol.KW),
    V050HK3W(0x20ab, Protocol.KW),
    V100GC1(0x20a0, Protocol.KW),
    V100KC2(0x2091, Protocol.KW),
    V150KB1(0x2092, Protocol.KW),
    V200GW1(0x20a4, Protocol.KW),
    V200KW1(0x2094, Protocol.KW),
    V200KW2(0x2098, 0x01, 0x01, 0x01, 0x03, Protocol.KW),
    V200KW2_4(0x2098, 0x01, 0x01, 0x04, 0x04, Protocol.KW),
    V200KW2_5(0x2098, 0x01, 0x01, 0x05, 0x05, Protocol.KW),
    V200KW2_6(0x2098, 0x01, 0x01, 0x06, 0x0f, Protocol.KW),
    V300GW2(0x20a5, Protocol.KW),
    V300KW3(0x209c, Protocol.KW),
    V333MW1(0x20b8, Protocol._300, Protocol.KW),
    V333MW1S(0x20b9, Protocol._300, Protocol.KW),
    V333MW2(0x20ba, Protocol._300, Protocol.KW),
    VDensHC1(0x20c0, Protocol._300, Protocol.KW),
    VDensHC2(0x20c1, Protocol._300, Protocol.KW),
    VDensHO1(0x20c2, Protocol._300, Protocol.KW),
    Vitocom300(0x208a, Protocol._300, Protocol.KW),
    VPendHC1(0x20c3, Protocol._300, Protocol.KW),
    VPendHC2(0x20c4, Protocol._300, Protocol.KW),
    VPendHO1(0x20c5, Protocol._300, Protocol.KW),
    VPlusHC1(0x20c6, Protocol._300, Protocol.KW),
    VPlusHC2(0x20c7, Protocol._300, Protocol.KW),
    VPlusHO1(0x20c8, Protocol._300, Protocol.KW),
    VScotHC1(0x20c9, Protocol._300, Protocol.KW),
    VScotHC2(0x20ca, Protocol._300, Protocol.KW),
    VScotHO1(0x20cb, Protocol._300, Protocol.KW);

    public static Devices decode(EcnsysDeviceIdent_0x00F8_0x08 ecnsysDeviceIdent_0x00F8, EcnsysDeviceIdent_0x00F0_0x02 ecnsysDeviceIdent_0x00F0) {
        return switch (ecnsysDeviceIdent_0x00F8.getDeviceIdentification()) {
            case 0x2098 ->
                V200KW2;
            default ->
                throw new IllegalArgumentException(String.format("Cant find Device for 0x%04x!", ecnsysDeviceIdent_0x00F8.getDeviceIdentification()));
        };
    }

    final short id;
    final byte hwVersion;
    final byte hwVersionTill;
    final byte swVersion;
    final byte swVersionTill;

    final Protocol[] protocols;

    private Devices(int id, Protocol... protocols) {
        this.id = (short) id;
        this.hwVersion = 0x01;
        this.hwVersionTill = 0x01;
        this.swVersion = 0x01;
        this.swVersionTill = 0x01;
        this.protocols = protocols;
    }

    private Devices(int id, int hwVersion, int hwVersionTill, int swVersion, int swVersionTill, Protocol... protocols) {
        this.id = (short) id;
        this.hwVersion = (byte) hwVersion;
        this.hwVersionTill = (byte) hwVersionTill;
        this.swVersion = (byte) swVersion;
        this.swVersionTill = (byte) swVersionTill;
        this.protocols = protocols;
    }

    public short getId() {
        return id;
    }

    public String getName() {
        return name();
    }

    public String getLabel() {
        return name();
    }

    public static Devices getDeviceById(short devId, byte hwVersion, byte swVersion) {
        for (Devices dev : Devices.values()) {
            if (dev.id == devId
                    && dev.hwVersion >= hwVersion
                    && dev.hwVersionTill <= hwVersion
                    && dev.swVersion >= swVersion
                    && dev.swVersionTill <= swVersion) {
                return dev;
            }
        }
        return null;
    }

    public Protocol[] getProtocols() {
        return new Protocol[]{Protocol.KW};
    }
}

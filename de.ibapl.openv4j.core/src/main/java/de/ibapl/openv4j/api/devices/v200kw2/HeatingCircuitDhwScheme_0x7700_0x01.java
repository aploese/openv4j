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
package de.ibapl.openv4j.api.devices.v200kw2;

import de.ibapl.openv4j.api.tag.Name;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;

/**
 *
 * @author aploese
 */
public class HeatingCircuitDhwScheme_0x7700_0x01 extends MemArea {

    /**
     * BC is boiler circuit - DE: KK Kesselkreis DHW is Domestic Hot Water - DE:
     * WW Warm Wasser
     *
     *
     *
     * A1 is System circuit 1 (heatig circuit 1) M1 is Mixer circuit 1 (heatig
     * circuit 1) M2 is Mixer circuit 2 (heatig circuit 2) M3 is Mixer circuit 3
     * (heatig circuit 3) DHW is Domestic Hot Water
     */
    public enum HeatingCircuitDhwScheme {
        @Name("1 KK")
        _1_BC,
        @Name("2 KK + WW")
        _2_BC_PLUS_DHW,
        @Name("3 M2")
        _3_M2,
        @Name("4 M2 + WW")
        _4_M2_PLUS_DHW2,
        @Name("5 KK + M2")
        _5_BC_PLUS_M2,
        @Name("6 KK + M2 + WW")
        _6_BC_PLUS_M2_PLUS_DHW,
        @Name("7 M2 + M3")
        _7_M2_PLUS_M3,
        @Name("8 M2 + M3 + WW")
        _8_M2_PLUS_M3_PLUS_DHW,
        @Name("9 KK + M2 + M3")
        _9_BC_PLUS_M2_PLUS_M2,
        @Name("10 KK + M2 + M3 + WW")
        _10_BC_PLUS_M2_PLUS_M3_PLUS_DHW;

        private static HeatingCircuitDhwScheme decode(byte b) {
            return switch (b) {
                case 0x01 ->
                    _1_BC;
                case 0x02 ->
                    _2_BC_PLUS_DHW;
                case 0x03 ->
                    _3_M2;
                case 0x04 ->
                    _4_M2_PLUS_DHW2;
                case 0x05 ->
                    _5_BC_PLUS_M2;
                case 0x06 ->
                    _6_BC_PLUS_M2_PLUS_DHW;
                case 0x07 ->
                    _7_M2_PLUS_M3;
                case 0x08 ->
                    _8_M2_PLUS_M3_PLUS_DHW;
                case 0x09 ->
                    _9_BC_PLUS_M2_PLUS_M2;
                case 0x0a ->
                    _10_BC_PLUS_M2_PLUS_M3_PLUS_DHW;
                default ->
                    throw new IllegalArgumentException("Unknown scheme: " + b);
            };
        }

        private static byte encode(HeatingCircuitDhwScheme value) {
            return switch (value) {
                case _1_BC ->
                    0x01;
                case _2_BC_PLUS_DHW ->
                    0x02;
                case _3_M2 ->
                    0x03;
                case _4_M2_PLUS_DHW2 ->
                    0x04;
                case _5_BC_PLUS_M2 ->
                    0x05;
                case _6_BC_PLUS_M2_PLUS_DHW ->
                    0x06;
                case _7_M2_PLUS_M3 ->
                    0x07;
                case _8_M2_PLUS_M3_PLUS_DHW ->
                    0x08;
                case _9_BC_PLUS_M2_PLUS_M2 ->
                    0x09;
                case _10_BC_PLUS_M2_PLUS_M3_PLUS_DHW ->
                    0x0a;
                default ->
                    throw new IllegalArgumentException("Cant encode scheme: " + value);
            };
        }
    }

    public HeatingCircuitDhwScheme_0x7700_0x01(MemoryImage mem) {
        super(mem, 0x7700, (short) 0x01);
    }

    @Name("K00_KonfiAnlagenschemaV200_NR1~0x7700")
    public HeatingCircuitDhwScheme getHeatingCircuitDhwSchemeV200() {
        return HeatingCircuitDhwScheme.decode(mem.getByte(baseAddress));
    }

    @Name("K00_KonfiAnlagenschemaV200_NR1~0x7700")
    public void setHeatingCircuitDhwSchemeV200(HeatingCircuitDhwScheme value) {
        mem.setByte(baseAddress, HeatingCircuitDhwScheme.encode(value));
    }

}

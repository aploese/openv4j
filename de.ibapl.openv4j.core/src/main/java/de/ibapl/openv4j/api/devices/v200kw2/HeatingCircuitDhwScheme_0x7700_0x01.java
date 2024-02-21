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
package de.ibapl.openv4j.api.devices.v200kw2;

import de.ibapl.openv4j.api.tag.Name;
import de.ibapl.openv4j.spi.protocolhandlers.MemArea;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;

/**
 *
 * @author aploese
 */
public class HeatingCircuitDhwScheme_0x7700_0x01 extends MemArea {

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
        _6_BC_PLUS_M2_PLUS_DHW;

        HeatingCircuitDhwScheme decode(byte b) {
            return switch (b) {
                case 0x00 ->
                    null;
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
                default ->
                    throw new IllegalArgumentException("Unknown scheme: " + b);
            };
        }
    }

    public HeatingCircuitDhwScheme_0x7700_0x01(MemoryImage mem) {
        super(mem, 0x7700, (short) 0x01);
    }

    @Name("K00_KonfiAnlagenschemaV200_NR1~0x7700")
    public byte getHeatingCircuitDhwSchemeV200() {
        return mem.getByte(baseAddress);
    }

}

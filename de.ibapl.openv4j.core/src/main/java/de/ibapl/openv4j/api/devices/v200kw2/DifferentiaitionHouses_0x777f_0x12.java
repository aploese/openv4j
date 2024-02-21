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
 * Also fetched ... 0x777f_0x17
 *
 * @author aploese
 */
public class DifferentiaitionHouses_0x777f_0x12 extends MemArea {

    public enum HouseType {
        @Name("0 Mehrparteienhaus")
        _0_APARTMENT_BUILDING,
        @Name("1 Einfamilienhaus")
        _1_DETACHED_HOUSE;

        HouseType decode(byte b) {
            return switch (b) {
                case 0x00 ->
                    _0_APARTMENT_BUILDING;
                case 0x01 ->
                    _1_DETACHED_HOUSE;
                default ->
                    throw new IllegalArgumentException("Unknown scheme: " + b);
            };
        }
    }

    public DifferentiaitionHouses_0x777f_0x12(MemoryImage mem) {
        super(mem, 0x7700, (short) 0x12);
    }

    @Name("K7F_KonfiEinfamilienhaus~0x777F")
    public byte getHouseType() {
        return mem.getByte(baseAddress);
    }

}

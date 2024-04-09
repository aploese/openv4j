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

import java.nio.ByteBuffer;

public class MemArea {

    // only unsignd short allowed....
    protected final int baseAddress;
    // only unsignd byte allowed....
    protected final short size;
    protected final MemoryImage mem;

    public MemArea(MemoryImage mem, int baseAddress, short size) {
        super();
        MemoryImage.checkAddressAndSize(baseAddress, size);
        this.mem = mem;
        this.baseAddress = baseAddress;
        this.size = size;
    }

    public final int getBaseAddress() {
        return baseAddress;
    }

    public final short getSize() {
        return size;
    }

    public final boolean matchAddressAndSize(int address, short size) {
        return this.baseAddress == address && this.size == size;
    }

    @Override
    public final int hashCode() {
        return getKey();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MemArea other = (MemArea) obj;
        if (this.baseAddress != other.baseAddress) {
            return false;
        }
        return this.size == other.size;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "baseaddress: " + baseAddress + ", size: " + size;
    }

    /**
     * Can be used as The key in a TreeMap.
     *
     * @return
     */
    public final int getKey() {
        return computeKey(baseAddress, size);
    }

    final boolean containsAddress(int address) {
        return (baseAddress <= address) && (baseAddress + size > address);
    }

    public static int computeKey(int address, short size) {
        return (address << 8) | size;
    }

    void copyBytesTo(ByteBuffer buffer) {
        buffer.put(mem.buffer, baseAddress, size);
    }

    void copyBytesFrom(ByteBuffer buffer) {
        buffer.get(mem.buffer, baseAddress, size);
    }

}

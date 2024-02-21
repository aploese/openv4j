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

import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;
import java.nio.ByteBuffer;

/**
 *
 * @author aploese
 */
public class TestMemoryImage extends MemoryImage {

    public final static byte PATTERN_01010101 = 0x55;
    public final static byte PATTERN_10101010 = (byte) 0xAA;

    public TestMemoryImage(byte pattern) {
        super();
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = pattern;
        }
    }
}

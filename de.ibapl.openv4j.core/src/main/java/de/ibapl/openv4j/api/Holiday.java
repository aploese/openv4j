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

import java.time.LocalDateTime;

public class Holiday {

    private LocalDateTime end;
    private LocalDateTime start;
    private byte endFlag;
    private byte startFlag;

    public LocalDateTime getEnd() {
        return end;
    }

    public byte getEndFlag() {
        return endFlag;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public byte getStartFlag() {
        return startFlag;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setEndFlag(byte endFlag) {
        this.endFlag = endFlag;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setStartFlag(byte startFlag) {
        this.startFlag = startFlag;
    }

    @Override
    public String toString() {
        return String.format("%s : 0x%02x | %s : 0x%02x", start, startFlag, end, endFlag);
    }
}

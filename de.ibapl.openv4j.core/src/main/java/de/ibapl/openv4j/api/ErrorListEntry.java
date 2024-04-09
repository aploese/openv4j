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
package de.ibapl.openv4j.api;

import java.time.LocalDateTime;

public class ErrorListEntry {

    private LocalDateTime tinestamp;
    private int errorCode;

    public ErrorListEntry(int errorCode, LocalDateTime timeStamp) {
        this.errorCode = errorCode;
        this.tinestamp = timeStamp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTinestamp() {
        return tinestamp;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setTinestamp(LocalDateTime tinestamp) {
        this.tinestamp = tinestamp;
    }

    @Override
    public String toString() {
        return String.format("%02x  %s", errorCode & 0xFF, tinestamp.toString());
    }

}

/*
 * Copyright 2009, openv4j.sf.net, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
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
 *
 * $Id: $
 *
 * @author arnep
 */
package net.sf.openv4j;

import java.util.Date;

/**
 *
 * @author aploese
 */
public class Holiday {
    private Date start;
    private Date end;
    private byte startFlag;
    private byte endFlag;

    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return the startFlag
     */
    public byte getStartFlag() {
        return startFlag;
    }

    /**
     * @return the endFlag
     */
    public byte getEndFlag() {
        return endFlag;
    }

    @Override
    public String toString() {
        return String.format("%s : 0x%02x | %s : 0x%02x", start, startFlag, end, endFlag);
    }

    /**
     * @param startFlag the startFlag to set
     */
    public void setStartFlag(byte startFlag) {
        this.startFlag = startFlag;
    }

    /**
     * @param endFlag the endFlag to set
     */
    public void setEndFlag(byte endFlag) {
        this.endFlag = endFlag;
    }

}

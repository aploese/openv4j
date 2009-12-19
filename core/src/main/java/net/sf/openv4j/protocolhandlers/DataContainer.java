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
package net.sf.openv4j.protocolhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.openv4j.DataPoint;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public abstract class DataContainer extends MemoryImage {
    private static Logger log = LoggerFactory.getLogger(DataContainer.class);

    /**
     * DOCUMENT ME!
     *
     * @param startAddress DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public abstract void addToDataContainer(int startAddress, int length);

    /**
     * DOCUMENT ME!
     *
     * @param startAddress DOCUMENT ME!
     * @param data DOCUMENT ME!
     */

    //TODO What happend if 1 param is given?
    public abstract void addToDataContainer(int startAddress, int[] data);

    /**
     * DOCUMENT ME!
     *
     * @param dataPoint DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addToDataContainer(DataPoint dataPoint, int[] data) {
        if (dataPoint.getLength() != data.length) {
            throw new IllegalArgumentException(String.format("Lenght of datapoint %s (%d) must match lenght of data (%d)", dataPoint.getName(), dataPoint.getLength(), data.length));
        }

        addToDataContainer(dataPoint.getAddr(), data);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract DataBlock getDataBlock(int i);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getDataBlockCount();

    /**
     * DOCUMENT ME!
     *
     * @param dataPoint DOCUMENT ME!
     */
    public void addToDataContainer(DataPoint dataPoint) {
        addToDataContainer(dataPoint.getAddress(), dataPoint.getLength());
    }
}

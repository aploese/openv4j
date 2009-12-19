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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.openv4j.protocolhandlers.DataContainer;
import net.sf.openv4j.protocolhandlers.SegmentedDataContainer;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class Device {
    /**
     * DOCUMENT ME!
     */
    protected DataContainer container;

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void myTearDown() throws Exception {
        container = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void mySetUp() throws Exception {
        container = new SegmentedDataContainer();
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    protected void readFromStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) {
            String[] splitted = line.split(" ");
            int address = -1;

            for (int i = 0; i < splitted.length; i++) {
                if (i == 0) {
                    address = Integer.parseInt(splitted[0], 16);
                    container.addToDataContainer(address, 16);
                } else {
                    if ((splitted[i].length() != 0) && (!"|".equals(splitted[i]))) {
                        container.setByte(address, (byte) Integer.parseInt(splitted[i], 16));
                        address++;
                    }
                }
            }
        }
    }
}

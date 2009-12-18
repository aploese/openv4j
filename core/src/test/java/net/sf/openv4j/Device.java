/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j;

import net.sf.openv4j.protocolhandlers.DataContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.sf.openv4j.protocolhandlers.SegmentedDataContainer;

/**
 *
 * @author aploese
 */
public class Device {
    protected DataContainer container;

    protected void mySetUp() throws Exception {
        container = new SegmentedDataContainer();

    }

    public void myTearDown() throws Exception {
        container = null;
    }

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
                         container.setByte(address, (byte)Integer.parseInt(splitted[i], 16));
                         address++;
                    }
                }
            }
        }
    }



}

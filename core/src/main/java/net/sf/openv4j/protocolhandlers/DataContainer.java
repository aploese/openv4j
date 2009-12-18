package net.sf.openv4j.protocolhandlers;

import net.sf.openv4j.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aploese
 */
public abstract class DataContainer extends MemoryImage {

    private static Logger log = LoggerFactory.getLogger(DataContainer.class);

    public abstract void addToDataContainer(int startAddress, int length);

    //TODO What happend if 1 param is given?
    public abstract void addToDataContainer(int startAddress, int[] data);

    public void addToDataContainer(DataPoint dataPoint, int[] data) {
        if (dataPoint.getLength() != data.length) {
            throw new IllegalArgumentException(String.format("Lenght of datapoint %s (%d) must match lenght of data (%d)", dataPoint.getName(), dataPoint.getLength(), data.length));
        }
        addToDataContainer(dataPoint.getAddr(), data);
    }

    public abstract DataBlock getDataBlock(int i);

    public abstract int getDataBlockCount();

    public void addToDataContainer(DataPoint dataPoint) {
        addToDataContainer(dataPoint.getAddress(), dataPoint.getLength());
    }

}

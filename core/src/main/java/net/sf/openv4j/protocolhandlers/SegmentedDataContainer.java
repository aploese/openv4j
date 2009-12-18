/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j.protocolhandlers;

/**
 *
 * @author aploese
 */
public class SegmentedDataContainer extends DataContainer {

    /**
     * At least the V200KW2 is capable to fetch all data with 32 byte segmentation
     */
    /**
     * At least the V200KW2 is capable to fetch all data with 32 byte segmentation
     */
    public static final int DEFAULT_SEGMENT_SIZE = 32;
    private int segmentSize;
    private DataBlock[] dataSegments;

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
        dataSegments = new DataBlock[0x010000 / segmentSize];
    }

    @Override
    public void addToDataContainer(int startAddress, int[] data) {
        throw new UnsupportedOperationException("Write of segmented data is not supported.");
    }

    @Override
    public void addToDataContainer(int startAddress, int length) {
        int baseInxed = startAddress / segmentSize;
        if (dataSegments[baseInxed] == null) {
            dataSegments[baseInxed] = new DataBlock(baseInxed * segmentSize, segmentSize);
        }
        for (int i = 1; i < length / segmentSize; i++) {
            baseInxed++;
            if (dataSegments[baseInxed] == null) {
                dataSegments[baseInxed] = new DataBlock(baseInxed * segmentSize, segmentSize);
            }
        }
    }

    @Override
    protected int getByte(int address) {
        final int baseIndex = address / segmentSize;
        if (dataSegments[baseIndex] == null) {
            return 0xff;
        } else {
            return dataSegments[baseIndex].getByteAtPos(address % segmentSize);
        }
    }

    public int getNextSegmentAddr(int currentSendSegmentAddr) {
        if (currentSendSegmentAddr == -1) {
            for (int i = 0; i < dataSegments.length; i++) {
                if (dataSegments[i] != null) {
                    return i * segmentSize;
                }
            }
        } else {
            for (int i = currentSendSegmentAddr / segmentSize + 1; i < dataSegments.length; i++) {
                if (dataSegments[i] != null) {
                    return i * segmentSize;
                }
            }
        }
        return -1;
    }

    @Override
    public void setByte(int addr, byte theData) {
        dataSegments[addr / segmentSize].setByteAtPos(addr % segmentSize, theData);
    }

    @Override
    public void setBytes(int addr, byte[] theData) {
        if ((addr % segmentSize == 0) && (theData.length == segmentSize)) {
            dataSegments[addr / segmentSize].setBytesAtPos(addr % segmentSize, theData);
        } else {
            super.setBytes(addr, theData);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataSegments.length; i++) {
            if (dataSegments[i] != null) {
                dataSegments[i].writeTo(sb, i == 0);
            }
        }
        return sb.toString();
    }

    public SegmentedDataContainer() {
        super();
        setSegmentSize(DEFAULT_SEGMENT_SIZE);
    }

    public SegmentedDataContainer(int segmentSize) {
        super();
        setSegmentSize(segmentSize);
    }

    @Override
    public DataBlock getDataBlock(int index) {
        int pos = 0;
        for (int i = 0; i < dataSegments.length; i++) {
            if (dataSegments[i] != null) {
                if (pos == index) {
                    return dataSegments[i];
                }
                pos++;
            }
        }
        throw new IndexOutOfBoundsException("Cant find index " + index);
    }

    @Override
    public int getDataBlockCount() {
        int result = 0;
        for (int i = 0; i < dataSegments.length; i++) {
            if (dataSegments[i] != null) {
                result++;
            }
        }
        return result;
    }
}

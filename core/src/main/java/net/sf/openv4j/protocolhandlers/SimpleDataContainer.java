package net.sf.openv4j.protocolhandlers;

import java.util.ArrayList;
import java.util.List;
import net.sf.openv4j.protocolhandlers.DataBlock;
import net.sf.openv4j.protocolhandlers.DataContainer;
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
public class SimpleDataContainer extends DataContainer{

    private static Logger log = LoggerFactory.getLogger(SimpleDataContainer.class);

    @Override
    public void addToDataContainer(int startAddress, int length) {
        data.add(new DataBlock(startAddress, length));
    }

    @Override
    public void addToDataContainer(int startAddress, int[] bytes) {
        data.add(new DataBlock(startAddress, bytes));
    }

    @Override
    public int getByte(int address) {
        for (DataBlock db : data) {
            if (hasAddress(db, address)) {
                return db.getByteAtPos(address - db.getBaseAddress());
            }
        }
        log.error(String.format("No such Address %04x", address));
        return (byte) 0xFF;
    }

    private List<DataBlock> data = new ArrayList<DataBlock>();

    public int getLength() {
        return data.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (DataBlock db : data) {
            sb.append(String.format("0x04x :", db.getBaseAddress()));
            for (int i = 0; i < db.getLength(); i++) {
                sb.append(String.format(" %02x", db.getByteAtPos(i)));
            }
        }
        return sb.toString();
    }

    @Override
    public void setByte(int address, byte theData) {
        for (DataBlock db : data) {
            if (hasAddress(db, address)) {
                db.setByteAtPos(address - db.getBaseAddress(), theData);
            }
        }
        log.error(String.format("No such Address %04x", address));
    }

    
    @Override
    public DataBlock getDataBlock(int i) {
        return data.get(i);
    }

    @Override
    public int getDataBlockCount() {
        return data.size();
    }

    private boolean hasAddress(DataBlock db, int address) {
        return db.getBaseAddress() <= address && db.getBaseAddress() + db.getLength() > address;
    }
}

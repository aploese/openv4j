/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j;

import java.io.Serializable;

/**
 * UINT LSB first
 * HEX MSB first
 * @author aploese
 */
public enum DataType implements Serializable {
    BOOL(1),
    UINT_1(1),
    UINT_2(2),
    UINT_4(4),
    HEX_2(2),
    TIME_STAMP_8(8),
    CYCLE_TIMES(8),
    ERROR_LIST_ENTRY(9);

    final int length;

    private DataType (int length) {
        this.length = length;
    }

    int getLength() {
        return length;
    }
}

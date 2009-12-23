/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j;

import java.util.Comparator;

/**
 *
 * @author aploese
 */
public class GroupAndNameComparator implements Comparator<DataPoint> {

    public GroupAndNameComparator() {
    }

    @Override
    public int compare(DataPoint o1, DataPoint o2) {
        int result = o1.getGroup().compareTo(o2.getGroup());
        if (result != 0) {
            return result;
        }
        result = o1.getValueKind().compareTo(o2.getValueKind());
        if (result != 0) {
            return result;
        }
        result = o1.getValue().compareTo(o2.getValue());
        return result;
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j;

/**
 *
 * @author aploese
 */
public class CycleTimes {

    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;

    public void setStart(int h, int min) {
        startHour = h;
        startMin = min;
    }
    
    public void setEnd(int h, int min) {
        endHour = h;
        endMin = min;
    }

    /**
     * @return the startHour
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * @return the startMin
     */
    public int getStartMin() {
        return startMin;
    }

    /**
     * @return the endHour
     */
    public int getEndHour() {
        return endHour;
    }

    /**
     * @return the endMin
     */
    public int getEndMin() {
        return endMin;
    }


}

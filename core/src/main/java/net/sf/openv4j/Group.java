/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j;

import java.io.Serializable;

/**
 *
 * @author aploese
 */
public enum Group implements Serializable {
    COMMON("Common"),
    BOILER("Boiler"),
    DHW("DHW"),
    M1("M1"),
    M2("M2"),
    M3("M3"),
    SOLAR("Solar");

    final private String label;

    private Group(String label) {
        this.label = label;
    }


    public String getName() {
        return name();
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

}

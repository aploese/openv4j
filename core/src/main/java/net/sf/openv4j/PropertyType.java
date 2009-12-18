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
public enum PropertyType implements Serializable {

    STATE("state"),
    ERROR_STATE("error"),
    TEMP_ACTUAL("temp (a)"),
    TEMP_NOMINAL("temp (n)"),
    TEMP_LOW_PASS("temp (lp)"),
    TEMP_DAMPED("temp (d)"),
    TEMP_MAX("temp (max)"),
    TEMP_REDUCED("temp reduced"),
    TEMP_PARTY("temp party"),
    POWER_IN_PERCENT("power [%]"),
    CONSUPTION("consuption [l|m³]"),
    OPERATING_TIME_S("time [s]"),
    OPERATING_TIME_H("time [h]"),
    ENERGY_KWH("energy [kWh]"),
    CYCLES("cycles"),
    CYCLES_HEATING("heating cycles"),
    CYCLES_RECIRC("recirc cycles"),
    TIME_STAMP("time"),
    POSITION_IN_PERCENT("position [%]"),
    CONFIG("config "),
    ERROR("error");

    final String label;

    private PropertyType(String label) {
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

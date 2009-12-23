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

import java.io.Serializable;

import java.util.Arrays;
import static net.sf.openv4j.DataType.*;
import static net.sf.openv4j.Group.*;
import static net.sf.openv4j.Property.*;
import static net.sf.openv4j.PropertyType.*;
import net.sf.openv4j.protocolhandlers.DataContainer;
import net.sf.openv4j.protocolhandlers.MemoryImage;

/**
 * 
 */
public enum DataPoint implements Serializable { //0x0000 to 0x0fff
    COMMON_DEVICE_TYPE_ID(0x00F8, COMMON, CONFIG, DEVICE_TYPE_ID, HEX_2, AccessType.RO),
    COMMON_TEMP_OUTSIDE(0x0800, COMMON, TEMP_ACTUAL, OUTSIDE, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_BOILER_ACTUAL(0x0802, Group.BOILER, TEMP_ACTUAL, Property.BOILER, UINT_2, AccessType.RO, 0.1),
    DHW_TEMP_DHW_ACTUAL(0x0804, Group.DHW, TEMP_ACTUAL, DHW_CYLINDER, UINT_2, AccessType.RO, 0.1),
    DHW_TEMP_DHW_2_ACTUAL(0x0806, Group.DHW, TEMP_ACTUAL, DHW_CYLINDER_2, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_EXHAUST(0x0808, Group.BOILER, TEMP_ACTUAL, EXHAUST, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_HEATING_RETURN(0x080a, Group.BOILER, TEMP_ACTUAL, HEATING_RETURN, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_HEATING_SUPPLY(0x080c, Group.BOILER, TEMP_ACTUAL, HEATING_SUPPLY, UINT_2, AccessType.RO, 0.1, new AlsoAt(0x0950)),
    COMMON_TEMP_OUTSIDE_LOW_PASS(0x080e, Group.COMMON, TEMP_LOW_PASS, OUTSIDE, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_MAX_EXHAUST(0x089f, Group.BOILER, TEMP_MAX, EXHAUST, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_BOILER_LOW_PASS(0x0810, Group.BOILER, TEMP_LOW_PASS, Property.BOILER, UINT_2, AccessType.RO, 0.1),
    DHW_TEMP_DHW_LOW_PASS(0x0812, Group.DHW, TEMP_LOW_PASS, DHW_CYLINDER, UINT_2, AccessType.RO, 0.1),
    DHW_TEMP_DHW_2_LOW_PASS(0x0814, Group.DHW, TEMP_LOW_PASS, DHW_CYLINDER_2, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_EXHAUST_LOW_PASS(0x0816, Group.BOILER, TEMP_LOW_PASS, EXHAUST, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_HEATING_RETURN_LOW_PASS(0x0818, Group.BOILER, TEMP_LOW_PASS, HEATING_RETURN, UINT_2, AccessType.RO, 0.1),
    BOILER_STATE_BURNER_STAGE_1(0x0842, Group.BOILER, STATE, BURNER_STAGE_1, BOOL, AccessType.RO),DHW_STATE_DHW_PUMP(0x0845, Group.DHW, STATE, DHW_PUMP, BOOL, AccessType.RO),
    DHW_STATE_DHW_RECIRC_PUMP(0x0846, Group.DHW, STATE, DHW_RECIRC_PUMP, BOOL, AccessType.RO),
    BOILER_STATE_BURNER_STAGE_2(0x0849, Group.BOILER, STATE, BURNER_STAGE_2, BOOL, AccessType.RO),
    BOILER_TEMP_HEATING_SUPPLY_LOW_PASS(0x081a, Group.BOILER, TEMP_LOW_PASS, HEATING_SUPPLY, UINT_2, AccessType.RO, 0.1),BOILER_ERROR_STATE_BURNER_STAGE_1(0x0883, Group.BOILER, ERROR_STATE, BURNER_STAGE_1, BOOL, AccessType.RO),
    BOILER_OPERATING_TIME_BURNER_STAGE_1(0x0886, Group.BOILER, OPERATING_TIME_S, BURNER_STAGE_1, UINT_4, AccessType.RO),
    BOILER_CYCLES_BURNER_STAGE_1(0x088a, Group.BOILER, CYCLES, BURNER_STAGE_1, UINT_4, AccessType.RO, new AlsoAt(0x08a7)),
    COMMON_SYSTEM_TIME(0x088e, COMMON, TIME_STAMP, DEVICE, TIME_STAMP_8, AccessType.RW),BOILER_TEMP_UNKNOWN_0X0896(0x0896, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_UNKNOWN_0X0898(0x0898, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_UNKNOWN_0X089A(0x089a, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    BOILER_OPERATING_TIME_BURNER_STAGE_2(0x08a3, Group.BOILER, OPERATING_TIME_S, BURNER_STAGE_2, UINT_4, AccessType.RO, new AlsoAt(0x08ab)),
    BOILER_CYCLES_BURNER_STAGE_2(0x0887, Group.BOILER, CYCLES, BURNER_STAGE_2, UINT_4, AccessType.RO),DHW_A1M1_CYCLE_MON_TIMES(0x2100, Group.DHW_A1M1, CYCLES_HEATING, MONDAY, CYCLE_TIMES, AccessType.RW),
    //M1
    DHW_A1M1_CYCLE_TUE_TIMES(0x2108, Group.DHW_A1M1, CYCLES_HEATING, TUESDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_WED_TIMES(0x2110, Group.DHW_A1M1, CYCLES_HEATING, WEDNSDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_THD_TIMES(0x2118, Group.DHW_A1M1, CYCLES_HEATING, THURSDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_FR_TIMES(0x2120, Group.DHW_A1M1, CYCLES_HEATING, FRIDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_SA_TIMES(0x2128, Group.DHW_A1M1, CYCLES_HEATING, SATURDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_SU_TIMES(0x2130, Group.DHW_A1M1, CYCLES_HEATING, SUNDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_MON_TIMES(0x2200, Group.DHW_A1M1, CYCLES_RECIRC, MONDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_TUE_TIMES(0x2208, Group.DHW_A1M1, CYCLES_RECIRC, TUESDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_WED_TIMES(0x2210, Group.DHW_A1M1, CYCLES_RECIRC, WEDNSDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_THD_TIMES(0x2218, Group.DHW_A1M1, CYCLES_RECIRC, THURSDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_FR_TIMES(0x2220, Group.DHW_A1M1, CYCLES_RECIRC, FRIDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_SA_TIMES(0x2228, Group.DHW_A1M1, CYCLES_RECIRC, SATURDAY, CYCLE_TIMES, AccessType.RW),
    DHW_A1M1_CYCLE_RECIRC_SU_TIMES(0x2230, Group.DHW_A1M1, CYCLES_RECIRC, SUNDAY, CYCLE_TIMES, AccessType.RW),
    A1M1_TIMER_HEATUP(0x2502, Group.A1M1, CONFIG, HEATUP_TIME, UINT_4, AccessType.RO, 0.1),A1M1_STATE_FROST(0x2506, Group.A1M1, STATE, FROST, BOOL, AccessType.RO),
    A1M1_STATE_HOT_WATER_HEATUP(0x2508, Group.A1M1, STATE, DHW_HEATUP, BOOL, AccessType.RO),
    //M2
    M2_CYCLE_MON_TIMES(0x3000, Group.M2, CYCLES_HEATING, MONDAY, CYCLE_TIMES, AccessType.RW),M2_CYCLE_TUE_TIMES(0x3008, Group.M2, CYCLES_HEATING, TUESDAY, CYCLE_TIMES, AccessType.RW),
    M2_CYCLE_WED_TIMES(0x3010, Group.M2, CYCLES_HEATING, WEDNSDAY, CYCLE_TIMES, AccessType.RW),
    M2_CYCLE_THD_TIMES(0x3018, Group.M2, CYCLES_HEATING, THURSDAY, CYCLE_TIMES, AccessType.RW),
    M2_CYCLE_FR_TIMES(0x3020, Group.M2, CYCLES_HEATING, FRIDAY, CYCLE_TIMES, AccessType.RW),
    M2_CYCLE_SA_TIMES(0x3028, Group.M2, CYCLES_HEATING, SATURDAY, CYCLE_TIMES, AccessType.RW),
    M2_CYCLE_SU_TIMES(0x3030, Group.M2, CYCLES_HEATING, SUNDAY, CYCLE_TIMES, AccessType.RW),
    M2_OPERATING_TYPE(0x3301, Group.M2, CONFIG, OPERATING_TYPE, UINT_1, AccessType.RW, new AlsoAt(0x3501)),
    M2_STATE_SAVE(0x3302, Group.M2, STATE, SAVE_MODE, BOOL, AccessType.RO),
    M2_STATE_PARTY(0x3303, Group.M2, STATE, PARTY_MODE, BOOL, AccessType.RO),
    M2_CONFIG_SLOPE(0x3304, Group.M2, CONFIG, SLOPE, UINT_1, AccessType.RW),
    M2_CONFIG_SHIFT(0x3305, Group.M2, CONFIG, SHIFT, UINT_1, AccessType.RW, 0.1),
    M2_TEMP_ROOM_NORMAL(0x3306, Group.M2, TEMP_NORMAL, ROOM, UINT_1, AccessType.RW),
    M2_TEMP_ROMM_REDUCED(0x3307, Group.M2, TEMP_REDUCED, ROOM, UINT_1, AccessType.RW),
    M2_TEMP_ROOM_PARTY(0x3308, Group.M2, TEMP_PARTY, ROOM, UINT_1, AccessType.RW),
    M2_STATE_UNKNOWN_0X3500(0x3500, Group.M2, STATE, UNKNOWN, UINT_1, AccessType.RO),
    M2_STATE_UNKNOWN_0X3501(0x3501, Group.M2, STATE, UNKNOWN, UINT_1, AccessType.RO),
    M2_TIMER_HEATUP(0x3502, Group.M2, CONFIG, HEATUP_TIME, UINT_4, AccessType.RO, 0.1),
    M2_STATE_FROST(0x3506, Group.M2, STATE, FROST, BOOL, AccessType.RO),
    M2_STATE_UNKNOWN_0X3507(0x3507, Group.M2, STATE, UNKNOWN, UINT_1, AccessType.RO),
    M2_STATE_HOT_WATER_HEATUP(0x3508, Group.M2, STATE, DHW_HEATUP, BOOL, AccessType.RO),
    M2_STATE_UNKNOWN_0X3509(0x3509, Group.M2, STATE, UNKNOWN, UINT_1, AccessType.RO),
    M2_TEMP_UNKNOWN_0X3511(0x3511, Group.M2, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    // NAchtumschaltung?? oder WW??
    M2_STATE_UNKNOWN_0X3535(0x3535, Group.M2, STATE, UNKNOWN, UINT_1, AccessType.RO),M2_STATE_UNKNOWN_0X3541(0x3541, Group.M2, STATE, UNKNOWN, UINT_1, AccessType.RO),
    M2_STATE_PUMP(0x350a, Group.M2, STATE, PUMP, BOOL, AccessType.RO, new AlsoAt(0x3906)),
    //     STATE_M2_FROST(0x350b, Group.M2, STATE, FROST, BOOL),
    //     STATE_M2_FROST(0x350c, Group.M2, STATE, FROST, BOOL),
    //     STATE_M2_FROST(0x3510, Group.M2, STATE, FROST, BOOL),
    // TODO ???
    M2_TEMP_ROOM(0x350c, Group.M2, TEMP_NOMINAL, Property.ROOM, UINT_2, AccessType.RO, 0.1),M2_TEMP_0X350E(0x350e, Group.M2, TEMP_ACTUAL, Property.UNKNOWN, UINT_2, AccessType.RO, 0.1),
    //TODO ???     TEMP_M2_HEATING_SUPPLY(0x3544, Group.M2, TEMP_NOMINAL, HEATING_SUPPLY, UINT_2, 0.1),
    M2_MIXER_POS(0x354c, Group.M2, POSITION_IN_PERCENT, MIXER, UINT_1, AccessType.RO, 0.5),M2_CONFIG_C2_A0(0x37a0, Group.M2, CONFIG, C2_A0, UINT_1, AccessType.RW),
    M2_CONFIG_C2_A2(0x37a2, Group.M2, CONFIG, C2_A2, UINT_1, AccessType.RW),
    M2_CONFIG_C2_A3(0x37a3, Group.M2, CONFIG, C2_A3, UINT_1, AccessType.RW),
    M2_CONFIG_C2_A4(0x37a4, Group.M2, CONFIG, C2_A4, UINT_1, AccessType.RW),
    M2_CONFIG_C2_A5(0x37a5, Group.M2, CONFIG, C2_A5, UINT_1, AccessType.RW),
    M2_CONFIG_C2_A6(0x37a6, Group.M2, CONFIG, C2_A6, UINT_1, AccessType.RW),
    M2_CONFIG_C2_A9(0x37a9, Group.M2, CONFIG, C2_A9, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B0(0x37b0, Group.M2, CONFIG, C2_B0, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B1(0x37b1, Group.M2, CONFIG, C2_B1, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B2(0x37b2, Group.M2, CONFIG, C2_B2, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B3(0x37b3, Group.M2, CONFIG, C2_B3, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B5(0x37b5, Group.M2, CONFIG, C2_B5, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B7(0x37b7, Group.M2, CONFIG, C2_B7, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B8(0x37b8, Group.M2, CONFIG, C2_B8, UINT_1, AccessType.RW),
    M2_CONFIG_C2_B9(0x37b9, Group.M2, CONFIG, C2_B9, UINT_1, AccessType.RW),
    M2_CONFIG_C2_C0(0x37c0, Group.M2, CONFIG, C2_C0, UINT_1, AccessType.RW),
    M2_CONFIG_C2_C1(0x37c1, Group.M2, CONFIG, C2_C1, UINT_1, AccessType.RW),
    M2_CONFIG_C2_C2(0x37c2, Group.M2, CONFIG, C2_C2, UINT_1, AccessType.RW),
    M2_CONFIG_C2_C5(0x37c5, Group.M2, CONFIG, C2_C5, UINT_1, AccessType.RW),
    M2_CONFIG_C2_C8(0x37c8, Group.M2, CONFIG, C2_C8, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E1(0x37e1, Group.M2, CONFIG, C2_E1, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E2(0x37e2, Group.M2, CONFIG, C2_E2, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E5(0x37e5, Group.M2, CONFIG, C2_E5, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E6(0x37e6, Group.M2, CONFIG, C2_E6, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E7(0x37e7, Group.M2, CONFIG, C2_E7, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E8(0x37e8, Group.M2, CONFIG, C2_E8, UINT_1, AccessType.RW),
    M2_CONFIG_C2_E9(0x37e9, Group.M2, CONFIG, C2_E9, UINT_1, AccessType.RW),
    M2_CONFIG_C2_F0(0x37f0, Group.M2, CONFIG, C2_F0, UINT_1, AccessType.RW),
    M2_CONFIG_C2_F2(0x37f2, Group.M2, CONFIG, C2_F2, UINT_1, AccessType.RW),
    M2_TEMP_HEATING_SUPPLY(0x3900, Group.M2, TEMP_NOMINAL, HEATING_SUPPLY, UINT_2, AccessType.RO, 0.1),
    M2_TEMP_HEATING_RETURN(0x3902, Group.M2, TEMP_NOMINAL, HEATING_RETURN, UINT_2, AccessType.RO, 0.1),
    //M3
    BOILER_TEMP_UNKNOWN_0X5500(0x5500, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),BOILER_TEMP_NOMINAL_BOILER(0x5502, Group.BOILER, TEMP_NOMINAL, Property.BOILER, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_UNKNOWN_0X5504(0x5504, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_UNKNOWN_0X5506(0x5506, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    BOILER_TEMP_UNKNOWN_0X5508(0x5508, Group.BOILER, TEMP_ACTUAL, UNKNOWN, UINT_2, AccessType.RO, 0.1),
    BOILER_POSITION_DROSSELKLAPPE(0x5555, Group.BOILER, POSITION_IN_PERCENT, Property.THROTTLE_VALVE, UINT_2, AccessType.RO, 0.5),
    BOILER_STATE_BURNER_STAGES_ACTIVE(0x551e, Group.BOILER, STATE, Property.BURNER_STAGES, UINT_1, AccessType.RO),
    BOILER_STATE_UNKNOWN_0X5508(0x551f, Group.BOILER, STATE, UNKNOWN, UINT_1, AccessType.RO),
    BOILER_POWER_IN_PERCENT_BURNER(0x55e3, Group.BOILER, POWER_IN_PERCENT, Property.BURNER, UINT_2, AccessType.RO, 0.5),
    //??     TEMP_BOILER_NOMINAL(0x555a, Group.BOILER, TEMP_NOMINAL, ProtocolValue.BOILER, UINT_2, 0.1),

    //??     BOILER_TEMP_HEATING_SUPPLY_NOMINAL(0x5600, Group.BOILER, TEMP_NOMINAL, HEATING_SUPPLY, UINT_2, 0.1),
    //??     BOILER_TEMP_HEATING_RETURN_NOMINAL(0x56a0, Group.BOILER, TEMP_NOMINAL, HEATING_RETURN, UINT_2, 0.1);
    BOILER_CONFIG_BURNER_TYPE(0x5702, Group.BOILER, CONFIG, Property.C2_02, UINT_1, AccessType.RW),BOILER_CONFIG_C2_03(0x5703, Group.M2, CONFIG, C2_03, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_04(0x5704, Group.M2, CONFIG, C2_04, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_05(0x5705, Group.M2, CONFIG, C2_05, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_06(0x5706, Group.M2, CONFIG, C2_06, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_13(0x5713, Group.M2, CONFIG, C2_13, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_1F(0x571f, Group.M2, CONFIG, C2_1F, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_21(0x5721, Group.M2, CONFIG, C2_21, UINT_2, AccessType.RW),
    BOILER_CONFIG_C2_23(0x5723, Group.M2, CONFIG, C2_23, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_24(0x5724, Group.M2, CONFIG, C2_24, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_26(0x5726, Group.M2, CONFIG, C2_26, UINT_2, AccessType.RW),
    BOILER_CONFIG_C2_28(0x5728, Group.M2, CONFIG, C2_28, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_29(0x5729, Group.M2, CONFIG, C2_29, UINT_2, AccessType.RW),
    BOILER_CONFIG_C2_32(0x5732, Group.M2, CONFIG, C2_32, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_33(0x5733, Group.M2, CONFIG, C2_33, UINT_1, AccessType.RW),
    //BURNER 2 stages
    BOILER_CONFIG_C2_10(0x5710, Group.M2, CONFIG, C2_10, UINT_1, AccessType.RW),BOILER_CONFIG_C2_11(0x5711, Group.M2, CONFIG, C2_11, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_12(0x5712, Group.M2, CONFIG, C2_12, UINT_1, AccessType.RW),
    //BURNER Modulated
    BOILER_CONFIG_C2_15(0x5715, Group.M2, CONFIG, C2_15, UINT_1, AccessType.RW),BOILER_CONFIG_C2_16(0x5716, Group.M2, CONFIG, C2_16, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_17(0x5717, Group.M2, CONFIG, C2_17, UINT_1, AccessType.RW),
    BOILER_CONFIG_C2_18(0x5718, Group.M2, CONFIG, C2_18, UINT_2, AccessType.RW),
    BOILER_CONFIG_C2_1A(0x571a, Group.M2, CONFIG, C2_1A, UINT_1, AccessType.RW),
    //TODO ????
    DHW_TEMP_NOMINAL_DHW(0x6300, Group.DHW, TEMP_NOMINAL, DHW_CYLINDER, UINT_1, AccessType.RW),SOLAR_NACHLADEUNTERDRUECKUNG(0x6551, Group.SOLAR, STATE, NACHLADEUNTERDRUECKUNG, BOOL, AccessType.RO),
    SOLAR_STATE_PUMP(0x6552, Group.SOLAR, STATE, PUMP, BOOL, AccessType.RO, 0.1),
    SOLAR_ENERGY_KWH(0x6560, Group.SOLAR, ENERGY_KWH, COLLECTOR, UINT_4, AccessType.RO),
    SOLAR_TEMP_ACTUAL_COLLECTOR(0x6564, Group.SOLAR, TEMP_ACTUAL, COLLECTOR, UINT_2, AccessType.RO, 0.1),
    SOLAR_TEMP_ACTUAL_RESERVOIR(0x6566, Group.SOLAR, TEMP_ACTUAL, DHW_CYLINDER, UINT_2, AccessType.RO, 0.1),
    SOLAR_OPERATING_TIME(0x6568, Group.SOLAR, OPERATING_TIME_H, COLLECTOR, UINT_2, AccessType.RO),
    DHW_CONFIG_C2_55(0x6755, Group.DHW, CONFIG, C2_55, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_56(0x6756, Group.DHW, CONFIG, C2_56, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_58(0x6758, Group.DHW, CONFIG, C2_58, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_59(0x6759, Group.DHW, CONFIG, C2_59, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_60(0x6760, Group.DHW, CONFIG, C2_60, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_61(0x6761, Group.DHW, CONFIG, C2_61, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_62(0x6762, Group.DHW, CONFIG, C2_62, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_64(0x6764, Group.DHW, CONFIG, C2_64, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_66(0x6766, Group.DHW, CONFIG, C2_66, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_70(0x6770, Group.DHW, CONFIG, C2_70, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_71(0x6771, Group.DHW, CONFIG, C2_71, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_72(0x6772, Group.DHW, CONFIG, C2_72, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_73(0x6773, Group.DHW, CONFIG, C2_73, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_74(0x6774, Group.DHW, CONFIG, C2_74, UINT_1, AccessType.RW),
    DHW_CONFIG_C2_75(0x6775, Group.DHW, CONFIG, C2_75, UINT_1, AccessType.RW),
    COMMON_ERROR_ERROR_1(0x7507, Group.COMMON, ERROR, Property.ERROR_1, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_2(0x7510, Group.COMMON, ERROR, Property.ERROR_2, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_3(0x7519, Group.COMMON, ERROR, Property.ERROR_3, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_4(0x7522, Group.COMMON, ERROR, Property.ERROR_4, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_5(0x752b, Group.COMMON, ERROR, Property.ERROR_5, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_6(0x7534, Group.COMMON, ERROR, Property.ERROR_6, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_7(0x753d, Group.COMMON, ERROR, Property.ERROR_7, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_8(0x7546, Group.COMMON, ERROR, Property.ERROR_8, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_9(0x754f, Group.COMMON, ERROR, Property.ERROR_9, ERROR_LIST_ENTRY, AccessType.RO),
    COMMON_ERROR_ERROR_10(0x7558, Group.COMMON, ERROR, Property.ERROR_10, ERROR_LIST_ENTRY, AccessType.RO),
    BOILER_CONSUPTION(0x7574, Group.BOILER, CONSUPTION, Property.BURNER, UINT_4, AccessType.RO, 0.0001),
    COMMON_ERROR_STATE_ERROR(0x7579, Group.COMMON, ERROR, Property.STATUS_ERROR, BOOL, AccessType.RO),
    BOILER_POWER_IN_PERCENT_INTERNAL_PUMP(0x7660, Group.BOILER, POWER_IN_PERCENT, INTERNAL_PUMP, UINT_2, AccessType.RO),
    COMMON_CONFIG_C2_7F(0x777f, Group.COMMON, CONFIG, C2_7F, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_80(0x7780, Group.COMMON, CONFIG, C2_80, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_81(0x7781, Group.COMMON, CONFIG, C2_81, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_82(0x7782, Group.COMMON, CONFIG, C2_82, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_83(0x7783, Group.COMMON, CONFIG, C2_83, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_84(0x7784, Group.COMMON, CONFIG, C2_84, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_85(0x7785, Group.COMMON, CONFIG, C2_85, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_86(0x7786, Group.COMMON, CONFIG, C2_86, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_87(0x7787, Group.COMMON, CONFIG, C2_87, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_89(0x7789, Group.COMMON, CONFIG, C2_89, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_8A(0x778a, Group.COMMON, CONFIG, C2_8A, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_8E(0x778e, Group.COMMON, CONFIG, C2_8E, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_90(0x7790, Group.COMMON, CONFIG, C2_90, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_93(0x7793, Group.COMMON, CONFIG, C2_93, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_94(0x7794, Group.COMMON, CONFIG, C2_94, UINT_1, AccessType.RW),
    COMMON_CONFIG_C2_95(0x7795, Group.COMMON, CONFIG, C2_95, UINT_1, AccessType.RW);

    /*
       Aus Haustechnik....
       Vito_Data_V333F_BP 'VScotHO1 addresses for V333F_BP
       Temp_Aussen = &H5525 '1 OK
       Temp_Aussen_Gedämpft = &H5527 '2 ok
       Temp_WW = &H812 '3 ok
       Temp_WW_Soll = &H6300 '4 ??
       Temp_Kessel_Soll = &H555A '5 ok A226
       Temp_Kessel = &H802 '6 ok
       Temp_VL_Soll = &H2544 '7 A1M1 ok
       Temp_VL = &H2900 '8 A1M1 ok
       Temp_Tag = &H2306 '9 ??
       Temp_Nacht = &H2307 '10 ??
       Temp_Raum_Soll = &HA406 '11 ?? H250C
       Brenner_Stunden = &H8A7 '12 ?? H886
       Brenner_Starts = &H88A '13 OK
       Pump = &H7660 '14 ??
       Leistung = &HA38F '15 ??
       Betriebsart = &H2301 '16 A1M1 ok
       Party = &H2303 '17 A1M1 ok
       Spar = &H2302 '18 A1M1 ok
       Extern_Sperr = &H8A1 '19 ?? HA81 si
       Extern_Anfordern = &HA80 '20 ?? HA80
       Sammelstoerung = &H883 '21 ??´0A83? H847? FehlerHA82?
       ZirkuPump = &H6515 '22 H846 Speicherladepumpe
       Temp_RL = &H808 '23 80A geä.
       SysTime = &H88E '24 Uhrzeit
       Temp_WW_Ausl = &H814 '25 ?? Klären
       Mischer = &H254C '26
       SpeicherLP = &H6513 '27 &H845
       Temp_Abgas = &H816 '28
       Errorlist = &H7507 '29 klären
       Brenner_Ein = &H55D3 '30 Brenner ???
       Umschaltventil = &HA10 '31
       Drosselklappe = &H5555 '32
     */

    //    ab 7700 Codierung 2 ??
    //    Codierung 82 - 87 liegt auf 2 bis 7
    //0x2000 to 0x2fff
    //0x3000 to 0x3fff
    //0x4000 to 0x4fff
    //0x5000 to 0x5fff
    //0x6000 to 0x6fff
    //0x7000 to 0x7fff
    //0x8000 to 0x8fff
    //0x9000 to 0x9fff
    //0xa000 to 0xafff
    //0xb000 to 0xbfff
    //0xc000 to 0xcfff
    //0xd000 to 0xdfff
    //0xe000 to 0xefff
    //0xf000 to 0xffff
    public static final int[] BLOCKS_16 =
        { // GWG

           0x0000, 0x0010, 0x0020, 0x0030, 0x0040, 0x0050, 0x0060, 0x0070, 0x0080, 0x0090, 0x00a0, 0x00b0, 0x00c0, 0x00d0, 0x00e0, 0x00f0, 0x0200, 0x0210, 0x0220, // KW Boiler| DHW
        0x0800, 0x0810, 0x0820, 0x0830, 0x0840, 0x0850, 0x0860, 0x0870, 0x0880, 0x0890, 0x08a0, 0x08b0, 0x08e0, 0x08f0, // Unknown
        0x0900, 0x0910, // Unknown
        0x1000, 0x1010, 0x1020, 0x1030, 0x1040, 0x1050, 0x1060, 0x1070, 0x1080, 0x1090, // A1M1
        0x2000, 0x2010, 0x2020, 0x2030, 0x2100, 0x2110, 0x2120, 0x2130, // A1M1
        0x2200, 0x2210, 0x2220, 0x2230, // A1M1
        0x2300, 0x2310, // A1M1
        0x2500, 0x2510, 0x2520, 0x2530, 0x2540, // A1M1
        0x27a0, 0x27b0, 0x27c0, 0x27e0, 0x27f0, // A1M1
        0x2900, // M2
        0x3000, 0x3010, 0x3020, 0x3030, 0x3100, 0x3110, 0x3120, 0x3130, 0x3200, 0x3210, 0x3220, 0x3230, 0x3300, 0x3310, 0x3500, 0x3510, 0x3520, 0x3530, 0x3540, 0x37a0, 0x37b0, 0x37c0, 0x37e0, 0x37f0, 0x3900, 0x4000, 0x4010, 0x4020, 0x4030, 0x4100, 0x4110, 0x4120, 0x4130, 0x4200, 0x4210, 0x4220, 0x4230, 0x4300, 0x4310, 0x4500, 0x4510, 0x4520, 0x4530, 0x4540, 0x47a0, 0x47b0, 0x47c0, 0x47e0, 0x47f0, 0x4900, 0x5300, 0x5500, 0x5510, 0x5520, 0x5530, 0x5700, 0x5710, 0x5720, 0x5730, 0x57a0, 0x57d0, 0x57f0, 0x6300, 0x6500, 0x6510, //Solar
        0x6550, 0x6560, //??
        0x6750, 0x6760, 0x6770, 0x6900, 0x7300, 0x7310, 0x7320, 0x7330, 0x7340, 0x7500, 0x7510, 0x7520, 0x7530, 0x7540, 0x7550, 0x7560, 0x7570, 0x7700, 0x7770, 0x7780, 0x7790, 0x7900, // V333WM1 ??? Boiler 1 to 4
        0xa200, 0xa210, 0xa220, 0xa230, 0xa240, 0xa250, 0xa260, 0xa270, 0xa280, 0xa290, 0xa2a0, 0xa2b0, 0xa2c0, 0xa2d0, 0xa2e0, 0xa2f0, // V333WM1 Power nominal
        0xa3f0
        };

    /**
     * DOCUMENT ME!
     *
     * @param sb DOCUMENT ME!
     * @param dc DOCUMENT ME!
     */
    public static void printAll(StringBuilder sb, DataContainer dc) {
        for (Group g : Group.values()) {
            sb.append(g.toString()).append("\n");

            for (DataPoint p : getSortedPoints()) {
                if (g.equals(p.getGroup())) {
                    sb.append("\t");
                    p.toString(dc, sb);
                    sb.append("\n");
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataPoint DOCUMENT ME!
     * @param mem DOCUMENT ME!
     * @param sb DOCUMENT ME!
     */
    public static void printMatchingAddesses(DataPoint dataPoint, MemoryImage mem, StringBuilder sb) {
        boolean headerPrinted = false;

        for (int i = 0; i < (0x010000 - dataPoint.getLength()); i++) {
            if (isInKnownBlocks16(i, dataPoint.getLength())) {
                if (compareBytes(mem, dataPoint.getAddr(), i, dataPoint.getLength())) {
                    if (!headerPrinted) {
                        dataPoint.toString(mem, sb);
                        headerPrinted = true;
                    }

                    sb.append(String.format("\t\tAlso @0x%04x", i));

                    DataPoint p = findByAddr(i);

                    if (p != null) {
                        p.toString(mem, sb);
                    } else {
                        sb.append("\n");
                    }
                }
            }
        }
    }

    private static boolean compareBytes(MemoryImage mem, int addr0, int addr1, int length) {
        // same or already found
        if ((addr0 == addr1) || (addr0 > addr1)) {
            return false;
        }

        boolean isOnlyFF = true;

        for (int i = 0; i <= length; i++) {
            if (mem.getUInt1(addr0 + i) != mem.getUInt1(addr1 + i)) {
                return false;
            }

            isOnlyFF &= (mem.getUInt1(addr0 + i) == 0x00ff);
        }

        return !isOnlyFF;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DataPoint findByAddr(int addr) {
        for (DataPoint p : DataPoint.values()) {
            if (addr == p.getAddr()) {
                return p;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isInKnownBlocks16(int address, int length) {
        for (int i = 0; i < length; i++) {
            if (!isInKnownBlock16(address + i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isInKnownBlock16(int address) {
        for (int base : BLOCKS_16) {
            if ((base == ((address / 16) * 16))) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DataPoint[] getSortedPoints() {
        DataPoint[] result = Arrays.copyOf(DataPoint.values(), DataPoint.values().length);
        Arrays.sort(result, new GroupAndNameComparator());

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param addr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean addressBelongsToMe(int addr) {
        return (getAddr() <= addr) && ((getAddr() + getLength()) > addr);
    }

    private static boolean isUnknownRange(int address, int length) {
        for (DataPoint dp : DataPoint.values()) {
            for (int i = 0; i < length; i++) {
                if (dp.addressBelongsToMe(address + i)) {
                    return false;
                }
            }
        }

        return true;
    }

    private final int addr;
    private final Group group;
    private final PropertyType valueKind;
    private final Property value;
    private final DataType type;
    private final AccessType access;
    private final double factor;
    private final Alternative alternative;

    private DataPoint(int addr, Group group, PropertyType valueKind, Property value, DataType type, AccessType access) {
        this.addr = addr;
        this.group = group;
        this.valueKind = valueKind;
        this.value = value;
        this.type = type;
        this.access = access;
        this.factor = Double.NaN;
        this.alternative = null;
    }

    private DataPoint(int addr, Group group, PropertyType valueKind, Property value, DataType type, AccessType access, Alternative alternative) {
        this.addr = addr;
        this.group = group;
        this.valueKind = valueKind;
        this.value = value;
        this.type = type;
        this.factor = Double.NaN;
        this.access = access;
        this.alternative = alternative;
    }

    private DataPoint(int addr, Group group, PropertyType valueKind, Property value, DataType type, AccessType access, double factor) {
        this.addr = addr;
        this.group = group;
        this.valueKind = valueKind;
        this.value = value;
        this.type = type;
        this.factor = factor;
        this.access = access;
        this.alternative = null;
    }

    private DataPoint(int addr, Group group, PropertyType valueKind, Property value, DataType type, AccessType access, double factor, Alternative alternative) {
        this.addr = addr;
        this.group = group;
        this.valueKind = valueKind;
        this.value = value;
        this.type = type;
        this.factor = factor;
        this.access = access;
        this.alternative = alternative;
    }

    private String getFormatString() {
        switch (getType()) {
            case BOOL:
                return "%-28s: %d";

            case HEX_2:
                return "%-28s: %04x";

            case UINT_1:
            case UINT_2:
            case UINT_4:

                switch (getValueKind()) {
                    case OPERATING_TIME_S:
                        return "%-28s: %d s (%d:%02d:%02d)";

                    default:

                        if (Double.isNaN(getFactor())) {
                            return "%-28s: %d";
                        } else {
                            return "%-28s: %.1f";
                        }
                }

            case TIME_STAMP_8:

                switch (getValueKind()) {
                    case TIME_STAMP:
                        return "%-28s: %s";
                }

            default:
                return "%-28s: UNKNOWN ValueKind:\"" + getValueKind().name() + "\"";
        }
    }

    private String getAlternativeStr() {
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param mem DOCUMENT ME!
     * @param sb DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public void toString(MemoryImage mem, StringBuilder sb) {
        switch (getType()) {
            case BOOL:
                sb.append(String.format(getFormatString(), getLabel(), mem.getBool(getAddr()) ? 1 : 0));

                break;

            case HEX_2:
                sb.append(String.format(getFormatString(), getLabel(), mem.getHex2(getAddr())));

                break;

            case UINT_1:

                if (Double.isNaN(getFactor())) {
                    sb.append(String.format(getFormatString(), getLabel(), mem.getUInt1(getAddr())));
                } else {
                    sb.append(String.format(getFormatString(), getLabel(), mem.getUInt1(getAddr()) * getFactor()));
                }

                break;

            case UINT_2:

                if (Double.isNaN(getFactor())) {
                    sb.append(String.format(getFormatString(), getLabel(), mem.getUInt2(getAddr())));
                } else {
                    sb.append(String.format(getFormatString(), getLabel(), mem.getUInt2(getAddr()) * getFactor()));
                }

                break;

            case UINT_4:

                if (Double.isNaN(getFactor())) {
                    if (OPERATING_TIME_S.equals(getValueKind())) {
                        int data = mem.getUInt4(getAddr());
                        int h = data / 3600;
                        int min = (data / 60) % 60;
                        int sec = data % 60;

                        sb.append(String.format(getFormatString(), getLabel(), data, h, min, sec));
                    } else {
                        sb.append(String.format(getFormatString(), getLabel(), mem.getUInt4(getAddr())));
                    }
                } else {
                    sb.append(String.format(getFormatString(), getLabel(), mem.getUInt4(getAddr()) * getFactor()));
                }

                break;

            case TIME_STAMP_8:
                sb.append(String.format(getFormatString(), getLabel(), mem.getTimeStamp_8(getAddr()).toString()));

                break;

            case CYCLE_TIMES:
                sb.append(String.format("%-28s: ", getLabel()));
                sb.append(mem.getCycleTimes(addr, 8));

                break;

            case ERROR_LIST_ENTRY:
                sb.append(String.format("%-28s: %s", getLabel(), mem.getErrorListEntry(addr)));

                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return getType().getLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param dc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public int getInt(DataContainer dc) {
        switch (getType()) {
            case BOOL:
                return dc.getUInt1(getAddr());

            case UINT_1:
                return dc.getUInt1(getAddr());

            case UINT_2:
                return dc.getUInt2(getAddr());

            case UINT_4:
                return dc.getUInt4(getAddr());

            case HEX_2:
                return dc.getHex2(getAddr());

            default:
                throw new UnsupportedOperationException(String.format("Cant handle %s to int", getType().toString()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return the addr
     */
    public int getAddr() {
        return addr;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the valueKind
     */
    public PropertyType getValueKind() {
        return valueKind;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the value
     */
    public Property getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the type
     */
    public DataType getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the access
     */
    public AccessType getAccess() {
        return access;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the factor
     */
    public double getFactor() {
        return factor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the alternative
     */
    public Alternative getAlternative() {
        return alternative;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabel() {
        if (Property.UNKNOWN.equals(value)) {
            return String.format("0x%04x %s%s", getAddr(), getValueKind().toString(), getAlternativeStr());
        } else {
            return String.format("%s %s%s", getValue().toString(), getValueKind().toString(), getAlternativeStr());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object decode(MemoryImage dc) {
        switch (type) {
            case BOOL:
                return dc.getBool(addr);

            case CYCLE_TIMES:
                return dc.getCycleTimes(addr, 8);

            case ERROR_LIST_ENTRY:
                return dc.getErrorListEntry(addr);

            case TIME_STAMP_8:
                return dc.getTimeStamp_8(addr);

            case HEX_2:
                return String.format("0x%04x", dc.getHex2(addr));

            case UINT_1:

                if (Double.isNaN(factor)) {
                    return (double) dc.getUInt1(addr);
                } else {
                    return dc.getUInt1(addr) * factor;
                }

            case UINT_2:

                if (Double.isNaN(factor)) {
                    return (double) dc.getUInt2(addr);
                } else {
                    return dc.getUInt2(addr) * factor;
                }

            case UINT_4:

                if (Double.isNaN(factor)) {
                    return (double) dc.getUInt4(addr);
                } else {
                    return dc.getUInt4(addr) * factor;
                }

            default:
                return "UNKNOWN DATATYPE ";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dc DOCUMENT ME!
     * @param d DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void encode(MemoryImage dc, double d) {
        switch (type) {
            case BOOL:
                dc.setBool(addr, Double.compare(d, 0) == 0);

                break;

            case HEX_2:
                dc.setHex2(addr, (int) d);

            case UINT_1:

                if (Double.isNaN(factor)) {
                    dc.setUInt1(addr, (int) d);
                } else {
                    dc.setUInt1(addr, (int) Math.round(d / factor));
                }

                break;

            case UINT_2:

                if (Double.isNaN(factor)) {
                    dc.setUInt2(addr, (int) d);
                } else {
                    dc.setUInt2(addr, (int) Math.round(d / factor));
                }

                break;

            case UINT_4:

                if (Double.isNaN(factor)) {
                    dc.setUInt4(addr, (int) d);
                } else {
                    dc.setUInt4(addr, (int) Math.round(d / factor));
                }

                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sb DOCUMENT ME!
     * @param memImage DOCUMENT ME!
     */
    public static void printAddresses(StringBuilder sb, MemoryImage memImage) {
        for (int i = 0; i < 0x010000;) {
            DataPoint p = DataPoint.findByAddr(i);

            if (p != null) {
                sb.append(String.format("\t@0x%04x %-8s=> ", p.getAddr(), p.getGroup().getLabel()));
                p.toString(memImage, sb);
                sb.append("\n");
                i += p.getLength();
            } else {
                if (DataPoint.isInKnownBlocks16(i, 1)) {
                    sb.append(String.format("\t\t@0x%04x 0x%02x byte: %4d", i, memImage.getUInt1(i) & 0xff, memImage.getUInt1(i)));

                    if (isInKnownBlocks16(i, 2) && isUnknownRange(i, 2)) {
                        sb.append(String.format(" short: %6d", memImage.getUInt2(i)));
                    }

                    if (isInKnownBlocks16(i, 4) && isUnknownRange(i, 4)) {
                        sb.append(String.format(" int: %11d", memImage.getUInt4(i)));
                    }

                    sb.append("\n");
                }

                i++;
            }
        }

        System.err.print(sb.toString());
    }
}

/*
 * OpenV4J - Drivers for the Viessmann optolink protocol https://github.com/openv/openv/wiki
 * Copyright (C) 2009-2024, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
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
 */
package de.ibapl.openv4j.api.devices;

import de.ibapl.openv4j.api.OperatingMode;
import de.ibapl.openv4j.api.tag.Name;
import de.ibapl.openv4j.api.tag.Trending;
import de.ibapl.openv4j.spi.protocolhandlers.MemoryImage;

/**
 *
 * @author aploese
 */
public class GenericBoilerDhwHc {

    /**
     *
     * Name formatString "SOMENAME%s~%dXXX" replace %s with A1M1 or A1 or M1 or
     * M2 or M3.</br>
     * %d with </br>
     * - 2 for A1M1 or A1 Or M1 </br>
     * - 3 for M2 </br>
     * - 4 for M3 </br>
     */
    public abstract class HeatingCircuit {

        private final int baseAddresss;
        private final static int OFFSET_CURRENT_OPERATING_MODE = 0x500;

        public HeatingCircuit(final int baseAddresss) {
            this.baseAddresss = baseAddresss;
        }

        @Trending
        @Name("HK_AktuelleBetriebsart%s~0x%d500 (Byte)")
        public OperatingMode getCurrentOperatingMode() {
            return OperatingMode.decode(memImage.getByte(baseAddresss + OFFSET_CURRENT_OPERATING_MODE));
        }

        @Trending
        public abstract double getFlowTemperature();

        @Trending
        public abstract double getReturnTemperature();

        /*
    - Heizkreispumpe A1M1 (729) [HK_PumpenzustandA1M1~0x2906 (Byte)]
    - Betriebsart A1M1 (55) [BedienBetriebsartA1M1~0x2301 (Byte)]
    - Raumtemperatur Soll Normalbetrieb A1M1 (82) [BedienRTSolltemperaturA1M1~0x2306 (Byte)]
    - Raumtemperatur Soll Reduzierter Betrieb A1M1 (85) [BedienRTSolltemperaturReduziertA1M1~0x2307 (Byte)]
    - Vorlaufsolltemperatur A1M1 (771) [HK_VT_Solltemperatur_A1M1~0x2500 (SInt)]
    - Raumtemperatur Soll A1M1 (732) [HK_RaumsolltemperaturaktuellA1M1~0x2500 (Byte)]
         */
    }

    public class HeatingCircuitMixed extends HeatingCircuit {

        public HeatingCircuitMixed(int baseAddresss) {
            super(baseAddresss);
        }

        @Override
        public double getFlowTemperature() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public double getReturnTemperature() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        /*
            X = 2 >A1M1; 3 > M2 ; 4 > M3
    - Vorlauftemperatur M2 (6053) [VorlauftemperaturM2~0x3900 (Int)]
    - Rücklauftemperatur (4680) [RuecklauftemperaturM2~0x3902 (Int)] HIDDEN:("Status Sensor 17"="Unterbrechung" OR "Status Sensor 17"="nicht vorhanden")
    - Raumtemperatur M2 (5376) [TiefpassTemperaturwert_RTS_M2~0x0898 (Int)] HIDDEN:(RaumTempSensHK2="Nicht vorhanden" OR A0_KennFBM2="nicht vorhanden" OR A0_KennFBM2="0 ohne" OR FBM2="nicht vorhanden")
        <ID>TiefpassTemperaturwert_RTS_M3~0x089A</ID>
    - Heizkreispumpe M2 (730) [HK_PumpenzustandM2~0x3906 (Byte)]
    - Betriebsart M2 (56) [BedienBetriebsartM2~0x3301 (Byte)]
    - Raumtemperatur Soll Normalbetrieb M2 (83) [BedienRTSolltemperaturM2~0x3306 (Byte)]
    - Raumtemperatur Soll Reduzierter Betrieb M2 (86) [BedienRTSolltemperaturReduziertM2~0x3307 (Byte)]
    - Vorlaufsolltemperatur M2 (772) [HK_VT_Solltemperatur_M2~0x3500 (SInt)]
    - Raumtemperatur Soll M2 (733) [HK_RaumsolltemperaturaktuellM2~0x3500 (Byte)]
         */
    }

    public class HeatingCircuitDirect extends HeatingCircuit {

        public HeatingCircuitDirect(int baseAddresss) {
            super(baseAddresss);
        }

        @Override
        public double getFlowTemperature() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public double getReturnTemperature() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        /*
    - Vorlauftemperatur A1M1 (5375) [TiefpassTemperaturwert_KTS_A1~0x0810 (Int)]
    - Raumtemperatur A1M1 (5367) [TiefpassTemperatur_RTS_A1M1~0x0896 (Int)] HIDDEN:(FBA1M1="nicht vorhanden" OR A0_KennFBA1M1="0 ohne" OR A0_KennFBA1M1="nicht vorhanden" OR RaumTempSensHK1="Nicht vorhanden")
    - Heizkreispumpe A1M1 (729) [HK_PumpenzustandA1M1~0x2906 (Byte)]
    - Betriebsart A1M1 (55) [BedienBetriebsartA1M1~0x2301 (Byte)]
    - Raumtemperatur Soll Normalbetrieb A1M1 (82) [BedienRTSolltemperaturA1M1~0x2306 (Byte)]
    - Raumtemperatur Soll Reduzierter Betrieb A1M1 (85) [BedienRTSolltemperaturReduziertA1M1~0x2307 (Byte)]
    - Vorlaufsolltemperatur A1M1 (771) [HK_VT_Solltemperatur_A1M1~0x2500 (SInt)]
    - Raumtemperatur Soll A1M1 (732) [HK_RaumsolltemperaturaktuellA1M1~0x2500 (Byte)]
         */
    }

    public class DhwCylinder {
        /*
    - Warmwassertemperatur (STS1) (5378) [TiefpassTemperaturwert_STS1~0x0812 (Int)]
    - Speicherladepumpe (251) [DigitalAusgang_Speicherladepumpe~0x0845 (Byte)]
    - Zirkulationspumpe (252) [DigitalAusgang_Zirkulationspumpe~0x0846 (Byte)]
    - Warmwasser-Solltemperatur (51) [Bedien_WW_Solltemperatur~0x6300 (Byte)]
    - Warmwassertemperatur Soll (effektiv) (7177) [WW_SolltemperaturAktuell~0x6500 (Int)]
         */
    }

    public class Boiler {
        /*
    - Aussentemperatur (5373) [TiefpassTemperaturwert_ATS~0x5525 (SInt)]
    - Kesseltemperatur (5374) [TiefpassTemperaturwert_KTS~0x0810 (Int)]
    - Abgastemperatur (5372) [TiefpassTemperaturwert_AGTS~0x0816 (Int)] HIDDEN:("Status Sensor 15"="Unterbrechung" OR "Status Sensor 15"="nicht vorhanden")
    - Kesselsolltemperatur (2955) [KesselSolltemperaturwert~0x5502 (Int)]
    - Kesseltemperatur (2948) [Kesselisttemperatur_NR1~0x5500 (Int)]
         */
    }

    protected MemoryImage memImage;

    public HeatingCircuit getA1M1() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public HeatingCircuitDirect getA1() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public HeatingCircuitMixed getM1() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public HeatingCircuitMixed getM2() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public HeatingCircuitMixed getM3() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public DhwCylinder getDhwCylinder() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    protected final int numOfHeatingCirquits;

    public GenericBoilerDhwHc(int numOfHeatingCirquits) {
        this.numOfHeatingCirquits = numOfHeatingCirquits;
    }

}

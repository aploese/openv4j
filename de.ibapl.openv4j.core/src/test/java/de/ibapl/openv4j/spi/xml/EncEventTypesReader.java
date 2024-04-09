/*
 * OpenV4J - Drivers for the Viessmann optolink protocol https://github.com/openv/openv/wiki
 * Copyright (C) 2024, Arne Plöse and individual contributors as indicated
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
package de.ibapl.openv4j.spi.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author aploese
 */
public class EncEventTypesReader {

    static enum Conversion {
        NoConversion,
        DatenpunktADDR,
        DateMBus,
        DateTimeMBus,
        DateTimeVitocom,
        Div2,
        Div10,
        Div100,
        Div1000,
        DateBCD,
        DateTimeBCD,
        Estrich,
        FixedStringTerminalZeroes,
        HexByte2AsciiByte,
        HexByte2DecimalByte,
        HexByte2Version,
        HexToFloat,
        HourDiffSec2Hour,
        IPAddress,
        Kesselfolge,
        LastBurnerCheck,
        LastCheckInterval,
        Mult2,
        Mult5,
        Mult10,
        Mult100,
        MultOffset,
        MultOffsetBCD,
        MultOffsetFloat,
        Phone2BCD,
        VitocomNV,
        RotateBytes,
        Sec2Minute,
        Sec2Hour,
        Time53,
        UTCDiff2Month,
        Vitocom300SGEinrichtenKanalLON,
        Vitocom300SGEinrichtenKanalMBUS,
        Vitocom300SGEinrichtenKanalWILO,
        Vitocom3NV,
        VitocomEingang;
    }

    static enum Unit {
        AMPERE,
        AMPERE_HOUR,
        BAR,
        MILLI_BAR,
        BAR_ABSOLUTE,
        CUBIC_METER,
        CUBIC_METER_PER_HOUR,
        DAYS,
        DEGREE_CELSIUS,
        DEGREE_CELSIUS_PER_MINUTE,
        DEZI_BEL_M,
        GIGA_JOULE,
        GRAM_PER_SECOND,
        HOUR,
        HERZ,
        KELVIN,
        _K_Sec,
        _K_Min,
        K_PER_H,
        KILO_JOULE,
        KILO_WATT,
        KILO_WATT_10,
        KILO_WATT_HOUR,
        KILO_WATT_HOUR_PER_CUBIC_METER,
        KILO_GRAMM,
        KILO_GRAMM_PER_HOUR,
        LITER,
        LITER_PER_HOUR,
        LITER_PER_MINUTE,
        MEGA_WATT_HOUR,
        MEGA_JOULE,
        MINUS,
        MINUTE,
        MONTH,
        OHM,
        PERCENT,
        PERCENT_PER_KELVIN,
        PERCENT_PER_KILO_WATT,
        PKT,
        PPM,
        PTS,
        RPM,
        RPS,
        RPS_PRO_S,
        SECOND,
        SECH,
        TONS,
        VOLT,
        VA,
        VA_R,
        MILLI_VOLT,
        WATT,
        WATT_HOUR;

        static Unit parse(String value) {
            if (value == null) {
                return null;
            }
            return switch (value) {
                case "ecnUnit.A" ->
                    AMPERE;
                case "ecnUnit.Ah" ->
                    AMPERE_HOUR;
                case "ecnUnit.Bar" ->
                    BAR;
                case "ecnUnit.Bar (absolut)" ->
                    BAR_ABSOLUTE;
                case "ecnUnit.mBar" ->
                    BAR;
                case "ecnUnit.m3" ->
                    CUBIC_METER;
                case "ecnUnit.m3 pro h", "ecnUnit.cbm pro h" ->
                    CUBIC_METER_PER_HOUR;
                case "ecnUnit.Tage" ->
                    DAYS;
                case "ecnUnit.Grad C" ->
                    DEGREE_CELSIUS;
                case "ecnUnit.Grad C pro Min" ->
                    DEGREE_CELSIUS_PER_MINUTE;
                case "ecnUnit.dBm" ->
                    DEZI_BEL_M;
                case "ecnUnit.g_pro_sec" ->
                    GRAM_PER_SECOND;
                case "ecnUnit.GJ" ->
                    GIGA_JOULE;
                case "ecnUnit.Hz", "Hz" ->
                    HERZ;
                case "ecnUnit.Stunden", "ecnUnit.h" ->
                    HOUR;
                case "ecnUnit.kg" ->
                    KILO_GRAMM;
                case "ecnUnit.kg_pro_h" ->
                    KILO_GRAMM_PER_HOUR;
                case "ecnUnit.K" ->
                    KELVIN;
                case "ecnUnit.kJ" ->
                    KILO_JOULE;
                case "ecnUnit.K Sec" ->
                    _K_Sec;
                case "ecnUnit.K Min" ->
                    _K_Min;
                case "ecnUnit.K pro h" ->
                    K_PER_H;
                case "ecnUnit.kW", "ecnUnit.KW" ->
                    KILO_WATT;
                case "ecnUnit.kWh" ->
                    KILO_WATT_HOUR;
                case "ecnUnit.kWh pro m3" ->
                    KILO_WATT_HOUR_PER_CUBIC_METER;
                case "ecnUnit.kW_10" ->
                    KILO_WATT_10;
                case "Liter" ->
                    LITER;
                case "ecnUnit.Liter" ->
                    LITER;
                case "ecnUnit.l pro h" ->
                    LITER_PER_HOUR;
                case "ecnUnit.l pro min" ->
                    LITER_PER_MINUTE;
                case "ecnUnit.MWh" ->
                    MEGA_WATT_HOUR;
                case "ecnUnit.MJ" ->
                    MEGA_JOULE;
                case "ecnUnit.Minus" ->
                    MINUS;
                case "Minuten" ->
                    MINUTE;
                case "ecnUnit.Minuten" ->
                    MINUTE;
                case "ecnUnit.Monate" ->
                    MONTH;
                case "ecnUnit.Ohm" ->
                    OHM;
                case "ecnUnit.Prozent" ->
                    PERCENT;
                case "ecnUnit.Prozent pro K" ->
                    PERCENT_PER_KELVIN;
                case "ecnUnit.ct_pro_kwh" ->
                    PERCENT_PER_KILO_WATT;
                case "Pkt" ->
                    PKT;
                case "ecnUnit.ppm" ->
                    PPM;
                case "Pts" ->
                    PTS;
                case "ecnUnit.U pro min" ->
                    RPM;
                case "ecnUnit.rps" ->
                    RPS;
                case "ecnUnit.rps pro s" ->
                    RPS_PRO_S;
                case "ecnUnit.sech" ->
                    SECH;
                case "ecnUnit.Sekunden", "ecnUnit.Sek." ->
                    SECOND;
                case "ecnUnit.Tonnen" ->
                    TONS;
                case "ecnUnit.V" ->
                    VOLT;
                case "ecnUnit.mV" ->
                    VOLT;
                case "ecnUnit.VA" ->
                    VA;
                case "ecnUnit.VAr" ->
                    VA_R;
                case "ecnUnit.W" ->
                    WATT;
                case "ecnUnit.Wh" ->
                    WATT_HOUR;
                default ->
                    throw new IllegalArgumentException("NO value: " + value);
            };
        }
    }

    static enum SDKDataType {
        Binary,
        Byte,
        ByteArray,
        DateTime,
        Double,
        Int,
        String;
    }

    static enum AccessMode {
        Read,
        ReadWrite,
        Write;
    }

    static enum FCRead {
        undefined,
        BE_READ,
        Physical_READ,
        EEPROM_READ,
        GFA_READ,
        KBUS_EEPROM_LT_READ,
        XRAM_READ,
        KBUS_VIRTUAL_READ,
        KBUS_DIRECT_READ,
        KBUS_INDIRECT_READ,
        KBUS_MEMBERLIST_READ,
        KBUS_TRANSPARENT_READ,
        KBUS_DATAELEMENT_READ,
        KMBUS_EEPROM_READ,
        OT_Physical_Read,
        OT_Virtual_Read,
        PROZESS_READ,
        Remote_Procedure_Call,
        Port_READ,
        Virtual_MarktManager_READ,
        Virtual_MBUS,
        Virtual_WILO_READ,
        Virtual_READ;
    }

    static enum FCWrite {
        undefined,
        BE_WRITE,
        EEPROM_WRITE,
        KBUS_VIRTUAL_WRITE,
        KBUS_EEPROM_LT_WRITE,
        KBUS_GATEWAY_WRITE,
        KBUS_TRANSPARENT_WRITE,
        KBUS_DIRECT_WRITE,
        KBUS_MEMBERLIST_WRITE,
        KBUS_INDIRECT_WRITE,
        OT_Physical_Write,
        OT_Virtual_Write,
        PROZESS_WRITE,
        Remote_Procedure_Call,
        Virtual_MarktManager_WRITE,
        Virtual_MBUS,
        Virtual_READ,
        Virtual_WILO_WRITE,
        Virtual_WRITE;
    }

    static class AddressComparator implements Comparator<Entry> {

        @Override
        public int compare(Entry o1, Entry o2) {
            int result;
            if (o1.address == null) {
                result = o2.address == null ? 0 : 1;
            } else {
                result = o2.address == null ? -1 : o1.address.compareTo(o2.address);
            }
            if (result != 0) {
                return result;
            }
            return o1.id.compareTo(o2.id);
        }

    }

    static class Entry {

        String id;
        String address;
        SDKDataType sDKDataType;
        FCRead fCRead;
        FCWrite fCWrite;
        Integer byteLength;
        Integer bytePosition;
        Integer bitLength;
        Integer bitPosition;
        AccessMode accessMode;
        Integer blockLength;
        String conversion;
        Unit unit;
    }

    public static void main(String[] args) {
        try {
            parse();
        } catch (XMLStreamException x) {

        } catch (FileNotFoundException ex) {
        }
    }

    /*
    <EventTypes>
  <EventType>
    <ID>ecnStatusEventType</ID>
    <DataType>Dropdown</DataType>
    <AccessMode>FCRead</AccessMode>
    <Address></Address>
    <VitocomChannelID></VitocomChannelID>
    <FCRead></FCRead>
    <PrefixRead></PrefixRead>
    <FCWrite></FCWrite>
    <PrefixWrite></PrefixWrite>
    <Parameter></Parameter>
    <BlockLength>0</BlockLength>
    <BytePosition>0</BytePosition>
    <ByteLength>0</ByteLength>
    <BitPosition>0</BitPosition>
    <BitLength>0</BitLength>
    <ValueList>0=ecnStatusEventType~Undefined</ValueList>
  </EventType>
     */
    public static void parse() throws XMLStreamException, FileNotFoundException {

        HashMap<String, String> idAddressMap = new HashMap<>();

        ArrayList<Entry> list = new ArrayList<>();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(EncEventTypesReader.class.getResourceAsStream("/viessmann-xml/0.0.26.4683/ecnEventType.xml"));

        Entry currentEntry = null;
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "EventTypes" ->
                        nextEvent = reader.nextEvent();
                    case "EventType" ->
                        nextEvent = reader.nextEvent();

                    case "AccessMode" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.accessMode = AccessMode.valueOf(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "BlockLength" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.blockLength = Integer.parseInt(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "Conversion" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.conversion = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "SDKDataType" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.sDKDataType = SDKDataType.valueOf(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "ByteLength" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.byteLength = Integer.parseInt(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "BytePosition" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.bytePosition = Integer.parseInt(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "BitLength" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.bitLength = Integer.parseInt(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "BitPosition" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.bitPosition = Integer.parseInt(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "FCRead" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.fCRead = FCRead.valueOf(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "FCWrite" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.fCWrite = FCWrite.valueOf(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "ID" -> {
                        nextEvent = reader.nextEvent();
                        currentEntry = new Entry();
                        currentEntry.id = nextEvent.asCharacters().getData();
                        nextEvent = reader.nextEvent();
                    }
                    case "Address" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.address = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "Unit" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.unit = Unit.parse(nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                    default -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            //        System.err.println("Startelement: " + startElement.getName() + ", " + nextEvent.asCharacters().getData());
                            nextEvent = reader.nextEvent();
                        }
                    }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("EventType")) {
                    if (currentEntry != null) {
                        String oldAddress = idAddressMap.put(currentEntry.id.split("~")[0], currentEntry.address);
                        if (oldAddress != null) {
                            System.err.println("Found Id " + currentEntry.id + " at: " + oldAddress + " and: " + currentEntry.address);
                        }
                        list.add(currentEntry);
                    }
                } else if (endElement.getName().getLocalPart().equals("EventTypes")) {
                    list.sort(new AddressComparator());
                    PrintStream ps = new PrintStream(new File("./ecnEventType.csv")); // System.out;
                    //PrintStream ps = System.out;
                    ps.append("Address;ID;SdkDataType;ByteLength;BytePosition;BitLength;BitPosition;FC_Read,FC_Write;Conversion;Unit");
                    for (Entry e : list) {
                        if (FCRead.Virtual_READ == e.fCRead || FCWrite.Virtual_WRITE == e.fCWrite) {
                            ps.append(e.address).append(';').append(e.id).append(';').append(Objects.toString(e.sDKDataType)).append(';');
                            ps.append(Objects.toString(e.byteLength)).append(';').append(Objects.toString(e.bytePosition)).append(';').append(Objects.toString(e.bitLength)).append(';').append(Objects.toString(e.bitPosition)).append(';');
                            ps.append(Objects.toString(e.fCRead)).append(';').append(Objects.toString(e.fCWrite)).append(';').append(e.conversion).append(';').append(Objects.toString(e.unit));
                            ps.println();
                        }
                    }
                    ps.close();
                }
            }
        }
    }

}

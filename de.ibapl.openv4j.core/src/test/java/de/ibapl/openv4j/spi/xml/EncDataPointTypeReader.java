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
package de.ibapl.openv4j.spi.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author aploese
 */
public class EncDataPointTypeReader {

    static class Entry {

        String Identification;
        String ID;
        String Options;
        String IdentificationExtension;
        String IdentificationExtensionTill;
        String Description;
        String EventOptimisation;
        String ControllerType;
        String ErrorType;
        String EventOptimisationExceptionList;
        String F0Till;
        String F0;
        ArrayList<String> EventTypeList;
    }

    public static void main(String[] args) {
        try {
            parse();
        } catch (XMLStreamException x) {
        } catch (FileNotFoundException x) {

        }
    }

    public static void parse() throws XMLStreamException, FileNotFoundException {

        HashMap<String, String> idAddressMap = new HashMap<>();

        ArrayList<Entry> list = new ArrayList<>();
        Set<String> names = new HashSet<>();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(EncDataPointTypeReader.class.getResourceAsStream("/viessmann-xml/0.0.26.4683/ecnDataPointType.xml"));

        Entry currentEntry = null;
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "DataPointTypes" -> {

                    }
                    case "DataPointType" -> {
                        currentEntry = new Entry();
                    }
                    case "EventTypeList" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            String[] splitted = nextEvent.asCharacters().getData().split(";");
                            currentEntry.EventTypeList = new ArrayList<>(Arrays.asList(splitted));
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "ID" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.ID = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "Identification" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.Identification = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "IdentificationExtension" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.IdentificationExtension = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "IdentificationExtensionTill" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.IdentificationExtensionTill = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "Description" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.Description = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "Options" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.Options = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "EventOptimisation" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.EventOptimisation = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "ControllerType" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.ControllerType = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "ErrorType" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.ErrorType = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "EventOptimisationExceptionList" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.EventOptimisationExceptionList = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "F0" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.F0 = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    case "F0Till" -> {
                        nextEvent = reader.nextEvent();
                        if (nextEvent instanceof Characters) {
                            currentEntry.F0Till = nextEvent.asCharacters().getData();
                            nextEvent = reader.nextEvent();
                        }
                    }
                    default -> {
                        throw new RuntimeException("Unhandled : " + startElement.getName().getLocalPart());
                    }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("DataPointType")) {
                    if (currentEntry.ID.startsWith("MBus")
                            || currentEntry.ID.startsWith("ecnStatusDataPoint")
                            || currentEntry.ID.startsWith("GWG_")
                            || currentEntry.ID.startsWith("VPlusH")
                            || currentEntry.ID.startsWith("VPlusH")
                            || currentEntry.ID.startsWith("VDensH")
                            || currentEntry.ID.startsWith("VPendH")
                            || currentEntry.ID.startsWith("VScotH")
                            || currentEntry.ID.startsWith("VCOM")
                            || currentEntry.ID.startsWith("Vitocom")
                            || currentEntry.ID.startsWith("Vitogate")
                            || currentEntry.ID.startsWith("_VITODATA")
                            || currentEntry.ID.startsWith("WILO")
                            || currentEntry.ID.startsWith("DEKATEL_")
                            || currentEntry.ID.startsWith("Solartrol_M")
                            || currentEntry.ID.startsWith("HV_")
                            || currentEntry.ID.startsWith("Dekamatik_")) {
                        //} else if (currentEntry.ID.startsWith("V050")
                        //|| currentEntry.ID.startsWith("V100")
                        //|| currentEntry.ID.startsWith("V150")) {
                        //Altes Zeug
                        //list.add(currentEntry);
                        //System.err.append("Added: ").append(currentEntry.ID).append(" Descr: ").println(currentEntry.Description);
                    } else if (currentEntry.ID.startsWith("V200GW")
                            || currentEntry.ID.startsWith("V300GW")
                            || currentEntry.ID.startsWith("V333GW")) {
                        //GW
                        //list.add(currentEntry);
                        //System.err.append("Added: ").append(currentEntry.ID).append(" Descr: ").println(currentEntry.Description);
                    } else if (currentEntry.ID.equals("V050HK1M")
                            || currentEntry.ID.equals("V050HK1W")
                            || currentEntry.ID.equals("V050HK3W")
                            || currentEntry.ID.equals("V050HK3S")
                            || currentEntry.ID.equals("V050HK1S")
                            || currentEntry.ID.equals("V100GC1")
                            || currentEntry.ID.equals("V100KC1")
                            || currentEntry.ID.equals("V100KC2")
                            || currentEntry.ID.equals("V150KB1")
                            || currentEntry.ID.equals("V200KW2")
                            || currentEntry.ID.equals("V200KW1")
                            || currentEntry.ID.equals("V300KW3")
                            || currentEntry.ID.equals("V333MW1")
                            || currentEntry.ID.equals("V333MW1S")
                            || currentEntry.ID.equals("V333MW2")
                            || currentEntry.ID.equals("VBC550P")
                            || currentEntry.ID.equals("VBC550S")
                            || currentEntry.ID.equals("VBC700_AW")
                            || currentEntry.ID.equals("VBC700_BW_WW")) {
                        //KW
                        list.add(currentEntry);
                    } else if (currentEntry.ID.startsWith("VBC550")) {
                        //Holz Scheit und Pellet
                        //list.add(currentEntry);
                        //System.err.append("Added: ").append(currentEntry.ID).append(" Descr: ").println(currentEntry.Description);
                    } else if (currentEntry.ID.startsWith("VBC700")
                            || currentEntry.ID.startsWith("VBC701")) {
                        //Wärmepumpen
                        //list.add(currentEntry);
                        //System.err.append("Added: ").append(currentEntry.ID).append(" Descr: ").println(currentEntry.Description);
                    } else {
                        //list.add(currentEntry);
                        System.err.append("UNKNOWN: ").append(currentEntry.ID).append(" Descr: ").println(currentEntry.Description);
                    }
                } else if (endElement.getName().getLocalPart().equals("DataPointTypes")) {
                    list.sort((e1, e2) -> {
                        return e1.ID.compareTo(e2.ID);
                    });
                    for (Entry e : list) {
                        if (e.EventTypeList == null) {
                            System.err.println("NO TYPES IN: " + e.ID);
                        } else {
                            names.addAll(e.EventTypeList);
                        }
                    }
                    List<String> namesAsList = new ArrayList<>(names);
                    //namesAsList.sort((s1, s2) -> s1.compareTo(s2));
                    namesAsList.sort((s1, s2) -> {
                        String[] split1 = s1.split("~");
                        String[] split2 = s2.split("~");
                        if (split1.length == 1) {
                            if (split2.length == 1) {
                                return 0;
                            } else {
                                return 1;
                            }
                        } else if (split2.length == 1) {
                            return -1;
                        } else {
                            return split1[1].compareTo(split2[1]);
                        }
                    });
                    PrintStream ps = new PrintStream(new File("./EncDataPoint.csv")); // System.out;

                    ps.append("DataPointType");
                    for (Entry e : list) {
                        ps.append("; " + e.ID);
                    }
                    ps.println();
                    ps.append("id");
                    for (Entry e : list) {
                        ps.append("; 0x" + e.Identification);
                    }
                    ps.println();
                    ps.append("extention");
                    for (Entry e : list) {
                        ps.append("; 0x" + e.IdentificationExtension);
                    }
                    ps.println();
                    ps.append("extentionTill");
                    for (Entry e : list) {
                        ps.append("; 0x" + e.IdentificationExtensionTill);
                    }
                    ps.println();
                    for (String name : namesAsList) {
                        ps.append(name);
                        for (Entry e : list) {
                            if (e.EventTypeList.contains(name)) {
                                ps.append("; X");
                            } else {
                                ps.append("; ");

                            }

                        }
                        ps.println();
                    }
                    ps.close();
                }
            }
        }
    }

}

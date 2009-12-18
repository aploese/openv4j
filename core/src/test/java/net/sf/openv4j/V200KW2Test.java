/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.openv4j;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author aploese
 */
public class V200KW2Test extends Device {

    public V200KW2Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        InitLog.LogInit.initLog(InitLog.LogInit.INFO);
//                log = LoggerFactory.getLogger(PrintMemoryMap.class);

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        super.mySetUp();
    }

    @After
    public void tearDown() throws Exception {
        super.myTearDown();
    }

    /**
     * Test of setStreams method, of class PrintMemoryMap.
     */
    @Test
    public void testToString() throws Exception {
        System.out.println("All");
        readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        System.err.println(container.toString());
        System.err.println(DataPoint.printAll(container));

    }

    /**
     * Test of setStreams method, of class PrintMemoryMap.
     */
    @Test
    public void testSearchAddresses() throws Exception {
        System.out.println("SearchAddresses");
        readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        StringBuilder sb = new StringBuilder();

        for (DataPoint p : DataPoint.values()) {
            switch (p.getValueKind()) {
                case TEMP_ACTUAL:
                case TEMP_DAMPED:
                case TEMP_LOW_PASS:
                case TEMP_NOMINAL:
                case TEMP_PARTY:
                case TEMP_REDUCED:
                    DataPoint.printMatchingAddesses(p, container, sb);
                    break;
                default:
                    DataPoint.printMatchingAddesses(p, container, sb);
                    break;
            }
        }
//        System.err.print(sb.toString());
    }

    /**
     * Test of setStreams method, of class PrintMemoryMap.
     */
    @Test
    public void testPrintAddresses() throws Exception {
        System.out.println("PrintAddresses");
        readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 0x010000;) {
            DataPoint p = DataPoint.findByAddr(i);
            if (p != null) {
                p.toString(container, sb, String.format("\t@0x%04x %s: ", p.getAddr(), p.getGroup().getLabel()));
                i += p.getLength();
            } else {
                if (container.getUInt1(i) != 0x00ff) {
                    sb.append(String.format("\t\t@0x%04x 0x%02x byte: %d\tshort: %d\tint: %d %n", i, container.getUInt1(i), container.getUInt1(i), (0x00fffe - i) < 0 ? 0x00ff : container.getUInt2(i), (0x00fffc - i) < 0 ? 0x00ff : container.getUInt4(i)));
                }
                i++;
            }
        }
        System.err.print(sb.toString());
    }
}

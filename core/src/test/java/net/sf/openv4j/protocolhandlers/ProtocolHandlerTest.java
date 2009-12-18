/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j.protocolhandlers;

import net.sf.openv4j.DataPoint;
import net.sf.openv4j.KW2Dummy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aploese
 */
public class ProtocolHandlerTest {

    public ProtocolHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
         protocolHandler = new ProtocolHandler();
        dummy = new KW2Dummy(1, 1);
        protocolHandler.setStreams(dummy.getInputStream(), dummy.getOutputStream());
   }

    @After
    public void tearDown() throws Exception {
         protocolHandler.close();
        protocolHandler = null;
        dummy = null;
   }

    private ProtocolHandler protocolHandler;
    private KW2Dummy dummy;

    /**
     * Test of setReadRequest method, of class ProtocolHandler.
     */
    @Test
    public void testSetReadRequest() throws Exception {
        dummy.readFromStream(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt"));
        DataContainer container = new SegmentedDataContainer(16);

        for (int i : DataPoint.BLOCKS) {
            container.addToDataContainer(i, 16);
        }
        protocolHandler.setReadRequest(container);
        synchronized (container) {
            container.wait(container.getDataBlockCount()  * 60000);
        }
        String[]  expected = dummy.streamToString(KW2Dummy.class.getResourceAsStream("V200KW2-MemMap.txt")).split("\n");
        String[] result = container.toString().split("\n");
        assertEquals("Length mismatch", expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Index " + i, expected[i], result[i]);
        }
   }

    /**
     * Test of setWriteRequest method, of class ProtocolHandler.
     */
    @Test
    public void testSetWriteRequest() throws Exception {
        System.out.println("setWriteRequest");
        DataContainer container = new SimpleDataContainer();

        container.addToDataContainer(DataPoint.M2_CONFIG_SLOPE);
        DataPoint.M2_CONFIG_SLOPE.encode(container, 22);
        protocolHandler.setWriteRequest(container);
        synchronized (container) {
            container.wait(container.getDataBlockCount()  * 60000);
        }
        assertEquals(DataPoint.M2_CONFIG_SLOPE.decode(dummy),(double)22);
    }

}
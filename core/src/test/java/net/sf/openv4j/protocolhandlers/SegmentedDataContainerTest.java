/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openv4j.protocolhandlers;

import net.sf.openv4j.DataPoint;
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
public class SegmentedDataContainerTest {

    public SegmentedDataContainerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private SegmentedDataContainer instance;

    @Before
    public void setUp() {
        instance = new SegmentedDataContainer();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of addToReadOut method, of class SegmentedDataContainer.
     */
    @Test
    public void testaddToDataContainer() {
        System.out.println("addToReadOut");
        instance.addToDataContainer(0, 1);
        assertEquals(1, instance.getDataBlockCount());
        instance.addToDataContainer(0x1000, 0x1000);
        assertEquals(129, instance.getDataBlockCount());
        instance.addToDataContainer(0x4001, 0x0fff);
        assertEquals(256, instance.getDataBlockCount());
    }

    /**
     * Test of addToReadOut method, of class SegmentedDataContainer.
     */
    @Test
    public void testAddToDataContainer_16() {
        System.out.println("addToReadOut");
        instance.setSegmentSize(16);
        for (int i : DataPoint.BLOCKS) {
            instance.addToDataContainer(i, 16);
        }
        assertEquals(DataPoint.BLOCKS.length, instance.getDataBlockCount());
        int i = 0;
        for (int addr : DataPoint.BLOCKS) {
            assertEquals("Index: " + i, addr, instance.getDataBlock(i).getBaseAddress());
            i++;
        }
    }

    /**
     * Test of addToReadOut method, of class SegmentedDataContainer.
     */
    @Test
    public void testAddToDataContainer_2() {
        System.out.println("addToReadOut");
        instance.addToDataContainer(0, 32);
        assertEquals(1, instance.getDataBlockCount());
    }
}
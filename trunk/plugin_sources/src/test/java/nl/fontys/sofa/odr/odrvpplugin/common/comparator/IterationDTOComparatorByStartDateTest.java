/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common.comparator;

import nl.rug.search.odr.ws.dto.IterationDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

/**
 *
 * @author Michael
 */
public class IterationDTOComparatorByStartDateTest {

    private IterationDTO o1;
    private IterationDTO o2;
    private long date;
    private IterationDTOComparatorByStartDate instance;

    public IterationDTOComparatorByStartDateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        date = new Date().getTime();
        o1 = new IterationDTO();
        o2 = new IterationDTO();
        instance = new IterationDTOComparatorByStartDate();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compare method, of class IterationDTOComparatorByStartDate.
     */
    @Test
    public void testCompare() {
        o1.setStartDate(new Date(date));
        o2.setStartDate(new Date(date + 1000));
        int result = instance.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareEquals() {
        o1.setStartDate(new Date(date));
        o2.setStartDate(new Date(date));
        int result = instance.compare(o1, o2);
        assertEquals(result, 0);
    }

    @Test
    public void testCompareNeq() {
        o1.setStartDate(new Date(date + 500));
        o2.setStartDate(new Date(date));
        int result = instance.compare(o1, o2);
        assertFalse(result < 0);
    }
}

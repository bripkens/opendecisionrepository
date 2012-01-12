/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common.comparator;

import java.util.Date;
import nl.fontys.sofa.odr.odrvpplugin.controller.Views.Helper.DecisionHistory;
import nl.rug.search.odr.ws.dto.HistoryDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael
 */
public class DecisionHistoryComparatorTest {

    private long date;
    private HistoryDTO h1;
    private HistoryDTO h2;
    private DecisionHistory o1;
    private DecisionHistory o2;
    private DecisionHistoryComparator instance;

    public DecisionHistoryComparatorTest() {
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

        h1 = new HistoryDTO();
        h2 = new HistoryDTO();

        o1 = new DecisionHistory(null, h1);
        o2 = new DecisionHistory(null, h2);

        instance = new DecisionHistoryComparator();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compare method, of class DecisionHistoryComparator.
     */
    @Test
    public void testCompare() {

        h1.setDecidedWhen(new Date(date));
        h2.setDecidedWhen(new Date(date + 1000));
        int result = instance.compare(o1, o2);
        assertTrue(0 > result);
    }

    @Test
    public void testCompareEqual() {
        h1.setDecidedWhen(new Date(date));
        h2.setDecidedWhen(new Date(date));
        int result = instance.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareNeg() {
        h1.setDecidedWhen(new Date(date + 500));
        h2.setDecidedWhen(new Date(date));
        int result = instance.compare(o1, o2);
        assertFalse(0 > result);
    }
}

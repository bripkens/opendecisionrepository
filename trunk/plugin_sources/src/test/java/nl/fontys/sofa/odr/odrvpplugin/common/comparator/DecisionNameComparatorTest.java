/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common.comparator;

import nl.rug.search.odr.ws.dto.DecisionDTO;
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
public class DecisionNameComparatorTest {

    private DecisionDTO o1;
    private DecisionDTO o2;
    private DecisionNameComparator instance;

    public DecisionNameComparatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        o1 = new DecisionDTO();
        o2 = new DecisionDTO();
        instance = new DecisionNameComparator();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compare method, of class DecisionNameComparator.
     */
    @Test
    public void testCompare() {
        o1.setName("Ant");
        o2.setName("Maven");
        int result = instance.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareEqual() {
        o1.setName("Glassfish");
        o2.setName("Glassfish");
        int result = instance.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareNeq() {
        o1.setName("JavaScript");
        o2.setName("AJAX");
        int result = instance.compare(o1, o2);
        assertFalse(result < 0);
    }
}

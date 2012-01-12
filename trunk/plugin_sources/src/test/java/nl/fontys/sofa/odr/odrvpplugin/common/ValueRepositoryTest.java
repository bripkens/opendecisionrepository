/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

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
public class ValueRepositoryTest {

    private static class TestClass {

        String s;

        public TestClass(String s) {
            this.s = s;
        }

        public String getS() {
            return s;
        }
    }

    public ValueRepositoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class ValueRepository.
     */
    @Test
    public void testGetInstance() {
        ValueRepository vR1 = ValueRepository.getInstance();
        ValueRepository vR2 = ValueRepository.getInstance();
        assertNotNull(vR1);
        assertNotNull(vR2);
        assertTrue(vR1.hashCode() == vR2.hashCode());
    }

    /**
     * Test of setValue method, of class ValueRepository.
     */
    @Test
    public void testGetAndSetValue() {
        TestClass s = new TestClass("SS");
        ValueRepository vR1 = ValueRepository.getInstance();

        TestClass tS = (TestClass) vR1.getValue(VPStrings.TESTSTRING);
        assertNull(tS);
        vR1.setValue(VPStrings.TESTSTRING, s);
        tS = (TestClass) vR1.getValue(VPStrings.TESTSTRING);
        assertNotNull(tS);
        assertEquals(s, tS);
    }

    /**
     * Test of removeValue method, of class ValueRepository.
     */
    @Test
    public void testRemoveValue() {
        TestClass s = new TestClass("SS");
        ValueRepository vR1 = ValueRepository.getInstance();
        vR1.setValue(VPStrings.TESTSTRING, s);
        TestClass tS = (TestClass) vR1.getValue(VPStrings.TESTSTRING);
        assertNotNull(tS);
        assertEquals(s, tS);
        vR1.removeValue(VPStrings.TESTSTRING);
        tS = (TestClass) vR1.getValue(VPStrings.TESTSTRING);
        assertNull(tS);

    }
}
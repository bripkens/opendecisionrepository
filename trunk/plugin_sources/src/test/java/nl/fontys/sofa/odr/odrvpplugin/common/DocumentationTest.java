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
 * @author eigo
 */
public class DocumentationTest {
    
    public DocumentationTest() {
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
     * Test of encode method, of class Documentation.
     */
    @Test
    public void testEncode() {
        String testString = "asjkdhasoödhaöskdhasödjhasjdhasd\nasdasdasdas\nasldaäsjd";
        String encoded = Documentation.encode(testString);
        String docString = Documentation.getDOCUMENTATION();
        assertTrue(encoded.startsWith(docString));
        assertTrue(encoded.equals(docString+testString+docString));
        assertTrue(encoded.contains(testString));
    }

    /**
     * Test of decode method, of class Documentation.
     */
    @Test
    public void testDecode() {
        String testString = "asjkdhasoödhaöskdhasödjhasjdhasd\nasdasdasdas\nasldaäsjd";
        String encoded = Documentation.encode(testString);
        String docString = Documentation.getDOCUMENTATION();
        String decoded = Documentation.decode(encoded);
        assertTrue(decoded.equals(testString));
    }
}

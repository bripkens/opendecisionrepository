package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class TemplateComponentTest {

    private TemplateComponent c;

    @Before
    public void setUp() {
        c = new TemplateComponent();
    }

    @Test
    public void testInitialization() {
        assertNull(c.getId());
        assertNull(c.getLabel());
        assertNull(c.getLocalizationReference());
        assertEquals(0, c.getOrder());
    }

    @Test
    public void testId() {
        c.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) c.getId());
    }

    @Test(expected=BusinessException.class)
    public void testBadId() {
        c.setId(null);
    }


    @Test
    public void testLocalizationReference() {
        String reference = "#{dsadas}";
        c.setLocalizationReference(reference);
        assertEquals(reference, c.getLocalizationReference());
        c.setLocalizationReference(null);
        assertNull(c.getLocalizationReference());
    }

    @Test
    public void testSetLabel() {
        String name = "ODR";

        c.setLabel(name);

        assertEquals(name, c.getLabel());
    }

    @Test(expected = BusinessException.class)
    public void testInvalidLabelNull() {
        c.setLabel(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidLabelEmpty() {
        c.setLabel("  ");
    }

    @Test(expected=BusinessException.class)
    public void testLabelTooShort() {
        c.setLabel("aa ");
    }

    @Test(expected=BusinessException.class)
    public void testLabelTooLong() {
        c.setLabel("aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna");
    }

    @Test
    public void testSetOrder() {
        c.setOrder(1);
        assertEquals(1, c.getOrder());
        c.setOrder(-31);
        assertEquals(-31, c.getOrder());
    }
}

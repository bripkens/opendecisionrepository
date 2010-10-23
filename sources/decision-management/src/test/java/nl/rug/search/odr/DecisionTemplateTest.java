package nl.rug.search.odr;

import nl.rug.search.odr.entities.DecisionTemplate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionTemplateTest extends AbstractEjbTest {

    private DecisionTemplateLocal dtl;




    @Before
    public void setUp() {
        dtl = lookUp(DecisionTemplateBean.class, DecisionTemplateLocal.class);
    }




    @Test
    public void testIsNameUsed() {
        String name = "Empty template";

        assertFalse(dtl.isNameUsed(name));

        DecisionTemplate template = new DecisionTemplate();
        template.setName(name);
        dtl.persist(template);

        assertTrue(dtl.isNameUsed(name));
    }



    @Test
    public void testGetByName() {
        DecisionTemplate template = new DecisionTemplate();
        template.setName("Foobar");
        dtl.persist(template);

        DecisionTemplate templateFromDatabase = dtl.getByName(template.getName());
        assertEquals(template.getName(), templateFromDatabase.getName());
    }
}

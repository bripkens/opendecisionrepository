package nl.rug.search.odr;

import nl.rug.search.odr.entities.DecisionTemplate;
import nl.rug.search.odr.entities.TemplateComponent;
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



    @Test
    public void testGetSmallestTemplate() {
        DecisionTemplate small = new DecisionTemplate();
        small.setName("Foobar");
        dtl.persist(small);

        DecisionTemplate big = new DecisionTemplate();
        big.setName("Foobar");
        big.addComponent(getSomeComponent());
        dtl.persist(big);

        assertEquals(small, dtl.getSmallestTemplate());

        small.addComponent(getSomeComponent());
        small.addComponent(getSomeComponent());
        dtl.merge(small);

        assertEquals(big, dtl.getSmallestTemplate());
    }

    private TemplateComponent getSomeComponent() {
        TemplateComponent tc = new TemplateComponent();
        tc.setLabel("ABCD");
        return tc;
    }
}

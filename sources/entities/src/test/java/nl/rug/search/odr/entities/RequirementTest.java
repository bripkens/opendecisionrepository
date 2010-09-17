package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class RequirementTest {

    private Requirement r;

    @Before
    public void setUp() {
        r = new Requirement();
    }

    @Test
    public void testInitialization() {
        assertNull(r.getRequirementId());
        assertNull(r.getDescription());
    }

    @Test
    public void testId() {
        long id = 1;
        r.setRequirementId(id);
        assertEquals(1, id);
    }

    @Test(expected = NullPointerException.class)
    public void testNullId() {
        r.setRequirementId(null);
    }

    @Test
    public void testSetName() {
        String description = "xxx";
        r.setDescription(description);

        assertEquals(description, r.getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void testNullDescription() {
        r.setDescription(null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyDescription() {
        r.setDescription("     ");
    }
}


package nl.rug.search.odr.entities;

import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectTest {

    private Project p;

    @Before
    public void setUp() {
        p = new Project();
    }

    @Test
    public void testInit() {
        assertNull(p.getId());
        assertNull(p.getDescription());
        assertNull(p.getName());
        assertNotNull(p.getMembers());

        assertTrue(p.getMembers().isEmpty());
    }

    @Test
    public void testSetName() {
        String name = "ODR";

        p.setName(name);

        assertEquals(name, p.getName());
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameNull() {
        p.setName(null);
    }

    @Test(expected = BusinessException.class)
    public void testInvalidNameEmpty() {
        p.setName("  ");
    }

    @Test(expected=BusinessException.class)
    public void testNameTooShort() {
        p.setName("aa ");
    }

    @Test(expected=BusinessException.class)
    public void testNameTooLong() {
        p.setName("aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + "aoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjknaaoiugghkpiugizfgvhbjkjoiugzfgvhjbkjoihugbjkna"
                + " ");
    }

    @Test(expected = BusinessException.class)
    public void testChangeName() {
        p.setName("peter");
        p.setName("gabi");
    }



    @Test
    public void testSetDescription() {
        p.setDescription("abcdef");

        assertEquals("abcdef", p.getDescription());

        p.setDescription(null);

        assertEquals(null, p.getDescription());
    }


    @Test
    public void testAddMembers() {
        Person person = new Person();
        person.setName("Gerd");

        ProjectMember pm = new ProjectMember();
        pm.setPerson(person);

        p.addMember(pm);

        assertSame(person, p.getMembers().iterator().next().getPerson());
        assertSame(pm, p.getMembers().iterator().next());

        assertSame(p, pm.getProject());
        assertSame(person, pm.getPerson());

        assertSame(p, person.getMemberships().iterator().next().getProject());
        assertSame(pm, person.getMemberships().iterator().next());
    }
}

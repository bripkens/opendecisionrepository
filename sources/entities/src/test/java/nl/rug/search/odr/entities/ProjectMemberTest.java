
package nl.rug.search.odr.entities;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectMemberTest {

    private ProjectMember m;

    @Before
    public void setUp() {
        m = new ProjectMember();
    }

    @Test
    public void testInit() {
        assertNull(m.getProjectMemberId());
        assertNull(m.getPerson());
        assertNull(m.getProject());
        assertNull(m.getRole());
    }

    @Test
    public void testAddMembers() {
        Project p = new Project();

        Person person = new Person();
        person.setName("Gerd");

        ProjectMember pm = new ProjectMember();
        pm.setPerson(person);
        pm.setProject(p);

        assertSame(person, p.getMembers().iterator().next().getPerson());
        assertSame(pm, p.getMembers().iterator().next());

        assertSame(p, pm.getProject());
        assertSame(person, pm.getPerson());

        assertSame(p, person.getMemberships().iterator().next().getProject());
        assertSame(pm, person.getMemberships().iterator().next());
    }
}

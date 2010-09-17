
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
}

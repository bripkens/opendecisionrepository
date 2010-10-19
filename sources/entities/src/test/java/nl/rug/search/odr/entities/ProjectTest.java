package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
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
    public void testSetId() {
        p.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) p.getId());
    }


    @Test(expected=BusinessException.class)
    public void testSetIdNull() {
        p.setId(null);
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




    @Test(expected = BusinessException.class)
    public void testNameTooShort() {
        p.setName("aa ");
    }




    @Test(expected = BusinessException.class)
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




    @Test
    public void setIterations() {
        Collection<Iteration> iterations = new ArrayList<Iteration>();
        Iteration it = new Iteration();
        iterations.add(it);

        p.setIterations(iterations);

        assertNotSame(iterations, p.getIterations());
        assertSame(it, p.getIterations().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setIterationsNull() {
        p.setIterations(null);
    }




    @Test
    public void addIteration() {
        Iteration it = new Iteration();
        p.addIteration(it);

        assertFalse(p.getIterations().isEmpty());

        assertSame(it, p.getIterations().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addIterationNull() {
        p.addIteration(null);
    }




    @Test
    public void removeIteration() {
        Iteration it = new Iteration();
        p.addIteration(it);

        assertFalse(p.getIterations().isEmpty());

        p.removeIteration(it);

        assertTrue(p.getIterations().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeIterationNull() {
        p.removeIteration(null);
    }




    @Test
    public void removeAllIteration() {
        Iteration it = new Iteration();
        p.addIteration(it);

        p.removeAllIterations();

        assertTrue(p.getIterations().isEmpty());
    }




    @Test
    public void setRoles() {
        Collection<StakeholderRole> roles = new ArrayList<StakeholderRole>();
        StakeholderRole role = new StakeholderRole();
        roles.add(role);

        p.setRoles(roles);

        assertNotSame(roles, p.getRoles());
        assertSame(role, p.getRoles().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setRolesNull() {
        p.setRoles(null);
    }




    @Test
    public void addRole() {
        StakeholderRole role = new StakeholderRole();
        p.addRole(role);

        assertFalse(p.getRoles().isEmpty());

        assertSame(role, p.getRoles().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addRoleNull() {
        p.addRole(null);
    }




    @Test
    public void removeRole() {
        StakeholderRole role = new StakeholderRole();
        p.addRole(role);

        assertFalse(p.getRoles().isEmpty());

        p.removeRole(role);

        assertTrue(p.getRoles().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeRoleNull() {
        p.removeRole(null);
    }




    @Test
    public void removeAllRoles() {
        StakeholderRole role = new StakeholderRole();
        p.addRole(role);

        p.removeAllRoles();

        assertTrue(p.getRoles().isEmpty());
    }


    @Test
    public void isPersistable() {
        assertFalse(p.isPersistable());

        p.setName("Foo");

        assertFalse(p.isPersistable());

        p.addMember(new ProjectMember());

        assertTrue(p.isPersistable());
    }


    @Test
    public void testCompareData() {
        assertNull(p.getCompareData()[0]);

        p.setName("12345");

        assertEquals("12345", p.getCompareData()[0]);

        p.setDescription("54321");

        assertEquals("54321", p.getCompareData()[1]);
    }


    @Test
    public void setMembers() {
        Collection<ProjectMember> members = new ArrayList<ProjectMember>();
        ProjectMember member = new ProjectMember();
        members.add(member);

        p.setMembers(members);

        assertNotSame(members, p.getMembers());
        assertSame(member, p.getMembers().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setMembersNull() {
        p.setMembers(null);
    }




    @Test
    public void addMember() {
        ProjectMember member = new ProjectMember();
        p.addMember(member);

        assertFalse(p.getMembers().isEmpty());

        assertSame(member, p.getMembers().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addMemberNull() {
        p.addMember(null);
    }




    @Test
    public void removeMember() {
        ProjectMember member = new ProjectMember();
        p.addMember(member);

        assertFalse(p.getMembers().isEmpty());

        p.removeMember(member);

        assertTrue(p.getMembers().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeMemberNull() {
        p.removeMember(null);
    }




    @Test
    public void removeAllMembers() {
        ProjectMember member = new ProjectMember();
        p.addMember(member);

        p.removeAllMembers();

        assertTrue(p.getMembers().isEmpty());
    }




































    @Test
    public void setDecisions() {
        Collection<Decision> decisions = new ArrayList<Decision>();
        Decision decision = new Decision();
        decisions.add(decision);

        p.setDecisions(decisions);

        assertNotSame(decisions, p.getDecisions());
        assertSame(decision, p.getDecisions().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setDecisionsNull() {
        p.setDecisions(null);
    }




    @Test
    public void addDecision() {
        Decision decision = new Decision();
        p.addDecision(decision);

        assertFalse(p.getDecisions().isEmpty());

        assertSame(decision, p.getDecisions().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addDecisionNull() {
        p.addDecision(null);
    }




    @Test
    public void removeDecision() {
        Decision decision = new Decision();
        p.addDecision(decision);

        assertFalse(p.getDecisions().isEmpty());

        p.removeDecision(decision);

        assertTrue(p.getDecisions().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeDecisionNull() {
        p.removeDecision(null);
    }




    @Test
    public void removeAllDecisions() {
        Decision decision = new Decision();
        p.addDecision(decision);

        p.removeAllDecisions();

        assertTrue(p.getDecisions().isEmpty());
    }
}

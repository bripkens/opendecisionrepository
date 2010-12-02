package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;
import static org.junit.Assert.*;
import static nl.rug.search.odr.Assert.*;
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

        assertTrue(p.getMembers().
                isEmpty());
    }




    @Test
    public void testSetId() {
        p.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, (long) p.getId());
    }




    @Test(expected = BusinessException.class)
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

        assertSame(person, p.getMembers().
                iterator().
                next().
                getPerson());
        assertSame(pm, p.getMembers().
                iterator().
                next());

        assertSame(p, pm.getProject());
        assertSame(person, pm.getPerson());

        assertSame(p, person.getMemberships().
                iterator().
                next().
                getProject());
        assertSame(pm, person.getMemberships().
                iterator().
                next());
    }




    @Test
    public void setIterations() {
        Collection<Iteration> iterations = new ArrayList<Iteration>();
        Iteration it = new Iteration();
        iterations.add(it);

        p.setIterations(iterations);

        assertNotSame(iterations, p.getIterations());
        assertSame(it, p.getIterations().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setIterationsNull() {
        p.setIterations(null);
    }




    @Test
    public void addIteration() {
        Iteration it = new Iteration();
        p.addIteration(it);

        assertFalse(p.getIterations().
                isEmpty());

        assertSame(it, p.getIterations().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addIterationNull() {
        p.addIteration(null);
    }




    @Test
    public void removeIteration() {
        Iteration it = new Iteration();
        p.addIteration(it);

        assertFalse(p.getIterations().
                isEmpty());

        p.removeIteration(it);

        assertTrue(p.getIterations().
                isEmpty());
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

        assertTrue(p.getIterations().
                isEmpty());
    }




    @Test
    public void setRoles() {
        Collection<StakeholderRole> roles = new ArrayList<StakeholderRole>();
        StakeholderRole role = new StakeholderRole();
        roles.add(role);

        p.setRoles(roles);

        assertNotSame(roles, p.getRoles());
        assertSame(role, p.getRoles().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setRolesNull() {
        p.setRoles(null);
    }




    @Test
    public void addRole() {
        StakeholderRole role = new StakeholderRole();
        p.addRole(role);

        assertFalse(p.getRoles().
                isEmpty());

        assertSame(role, p.getRoles().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addRoleNull() {
        p.addRole(null);
    }




    @Test
    public void removeRole() {
        StakeholderRole role = new StakeholderRole();
        p.addRole(role);

        assertFalse(p.getRoles().
                isEmpty());

        p.removeRole(role);

        assertTrue(p.getRoles().
                isEmpty());
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

        assertTrue(p.getRoles().
                isEmpty());
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
        assertSame(member, p.getMembers().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setMembersNull() {
        p.setMembers(null);
    }




    @Test
    public void addMember() {
        ProjectMember member = new ProjectMember();
        p.addMember(member);

        assertFalse(p.getMembers().
                isEmpty());

        assertSame(member, p.getMembers().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addMemberNull() {
        p.addMember(null);
    }




    @Test
    public void removeMember() {
        ProjectMember member = new ProjectMember();
        p.addMember(member);

        assertFalse(p.getMembers().
                isEmpty());

        p.removeMember(member);

        assertTrue(p.getMembers().
                isEmpty());
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

        assertTrue(p.getMembers().
                isEmpty());
    }




    @Test
    public void setDecisions() {
        Collection<Decision> decisions = new ArrayList<Decision>();
        Decision decision = new Decision();
        decisions.add(decision);

        p.setDecisions(decisions);

        assertNotSame(decisions, p.getDecisions());
        assertSame(decision, p.getDecisions().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setDecisionsNull() {
        p.setDecisions(null);
    }




    @Test
    public void addDecision() {
        Decision decision = new Decision();
        p.addDecision(decision);

        assertFalse(p.getDecisions().
                isEmpty());

        assertSame(decision, p.getDecisions().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addDecisionNull() {
        p.addDecision(null);
    }




    @Test
    public void removeDecision() {
        Decision decision = new Decision();
        p.addDecision(decision);

        assertFalse(p.getDecisions().
                isEmpty());

        p.removeDecision(decision);

        assertTrue(p.getDecisions().
                isEmpty());
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

        assertTrue(p.getDecisions().
                isEmpty());
    }




    @Test
    public void setConcerns() {
        Collection<Concern> concerns = new ArrayList<Concern>();
        Concern concern = new Concern();
        concerns.add(concern);

        p.setConcerns(concerns);

        assertNotSame(concerns, p.getConcerns());
        assertSame(concern, p.getConcerns().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setConcernsNull() {
        p.setConcerns(null);
    }




    @Test
    public void addConcern() {
        Concern concern = new Concern();
        p.addConcern(concern);

        assertFalse(p.getConcerns().
                isEmpty());

        assertSame(concern, p.getConcerns().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addConcernNull() {
        p.addConcern(null);
    }




    @Test
    public void removeConcern() {
        Concern concern = new Concern();
        p.addConcern(concern);

        assertFalse(p.getConcerns().
                isEmpty());

        p.removeConcern(concern);

        assertTrue(p.getConcerns().
                isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeConcernNull() {
        p.removeConcern(null);
    }




    @Test
    public void removeAllConcerns() {
        Concern concern = new Concern();
        p.addConcern(concern);

        p.removeAllConcerns();

        assertTrue(p.getConcerns().
                isEmpty());
    }




    @Test
    public void setStates() {
        Collection<State> states = new ArrayList<State>();
        State state = new State();
        states.add(state);

        p.setStates(states);

        assertNotSame(states, p.getStates());
        assertSame(state, p.getStates().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setStatesNull() {
        p.setStates(null);
    }




    @Test
    public void addState() {
        State state = new State();
        p.addState(state);

        assertFalse(p.getStates().
                isEmpty());

        assertSame(state, p.getStates().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addStateNull() {
        p.addState(null);
    }




    @Test
    public void removeState() {
        State state = new State();
        p.addState(state);

        assertFalse(p.getStates().
                isEmpty());

        p.removeState(state);

        assertTrue(p.getStates().
                isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeStateNull() {
        p.removeState(null);
    }




    @Test
    public void removeAllStates() {
        State state = new State();
        p.addState(state);

        p.removeAllStates();

        assertTrue(p.getStates().
                isEmpty());
    }




    @Test
    public void setRelationshipTypes() {
        Collection<RelationshipType> relationshipTypes = new ArrayList<RelationshipType>();
        RelationshipType relationshipType = new RelationshipType();
        relationshipTypes.add(relationshipType);

        p.setRelationshipTypes(relationshipTypes);

        assertNotSame(relationshipTypes, p.getRelationshipTypes());
        assertSame(relationshipType, p.getRelationshipTypes().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setRelationshipTypesNull() {
        p.setRelationshipTypes(null);
    }




    @Test
    public void addRelationship() {
        RelationshipType relationshipType = new RelationshipType();
        p.addRelationshipType(relationshipType);

        assertFalse(p.getRelationshipTypes().
                isEmpty());

        assertSame(relationshipType, p.getRelationshipTypes().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addRelationshipNull() {
        p.addRelationshipType(null);
    }




    @Test
    public void removeRelationship() {
        RelationshipType relationshipType = new RelationshipType();
        p.addRelationshipType(relationshipType);

        assertFalse(p.getRelationshipTypes().
                isEmpty());

        p.removeRelationshipType(relationshipType);

        assertTrue(p.getRelationshipTypes().
                isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeRelationshipNull() {
        p.removeRelationshipType(null);
    }




    @Test
    public void removeAllRelationshipTypes() {
        RelationshipType relationshipType = new RelationshipType();
        p.addRelationshipType(relationshipType);

        p.removeAllRelationshipTypes();

        assertTrue(p.getRelationshipTypes().
                isEmpty());
    }




    @Test
    public void comparatorTest() {
        p.setName("AAAAA");

        Project p2 = new Project();
        p2.setName("BBBBB");

        assertTrue(new Project.NameComparator().compare(p, p2) < 0);
    }




    @Test
    public void testGetDecision() {
        Decision d1 = new Decision();
        d1.setId(1l);
        p.addDecision(d1);

        Decision d2 = new Decision();
        d2.setId(2l);
        p.addDecision(d2);

        Decision d3 = new Decision();
        p.addDecision(d3);

        assertEquals(d1, p.getDecision(d1.getId()));
        assertEquals(d2, p.getDecision(d2.getId()));
        assertNull(p.getDecision(3l));
    }




    @Test
    public void testGetDestinctConcerns() {
        Concern co_1 = new Concern();
        co_1.setName("1");
        co_1.setGroup(1L);
        co_1.setCreatedWhen(new Date());

        Concern co_2 = new Concern();
        co_2.setName("2");
        co_2.setGroup(2L);
        co_2.setCreatedWhen(new Date());

        Concern co_3 = new Concern();
        co_3.setName("3");
        co_3.setGroup(3L);
        co_3.setCreatedWhen(new Date());

        Concern co_4 = new Concern();
        co_4.setName("4");
        co_4.setGroup(4L);
        co_4.setCreatedWhen(new Date());

        Concern co_2_1 = new Concern();
        co_2_1.setName("2_1");
        co_2_1.setGroup(2L);
        co_2_1.setCreatedWhen(new Date());

        Concern co_2_2 = new Concern();
        co_2_2.setName("2_2");
        co_2_2.setGroup(2L);
        co_2_2.setCreatedWhen(new Date());

        Concern co_3_1 = new Concern();
        co_3_1.setName("3_1");
        co_3_1.setGroup(3L);
        co_3_1.setCreatedWhen(new Date());

        Concern co_1_1 = new Concern();
        co_1_1.setName("1_1");
        co_1_1.setGroup(1L);
        co_1_1.setCreatedWhen(new Date());

        Concern co_3_3 = new Concern();
        co_3_3.setName("3_3");
        co_3_3.setGroup(3L);
        co_3_3.setCreatedWhen(new Date());

        Concern co_5 = new Concern();
        co_5.setName("5");
        co_5.setGroup(5L);
        co_5.setCreatedWhen(new Date());

        p.addConcern(co_1);
        p.addConcern(co_2);
        p.addConcern(co_3);
        p.addConcern(co_4);
        p.addConcern(co_2_1);
        p.addConcern(co_2_2);
        p.addConcern(co_3_1);
        p.addConcern(co_1_1);
        p.addConcern(co_3_3);
        p.addConcern(co_5);

        List<Concern> newestVersions = p.getDestinctConcerns();


        assertEquals(co_5, newestVersions.get(0));
        assertEquals(co_4, newestVersions.get(1));
        assertEquals(co_3_3, newestVersions.get(2));
        assertEquals(co_2_2, newestVersions.get(3));
        assertEquals(co_1_1, newestVersions.get(4));

    }




    @Test
    public void testVisualizations() {
        RelationshipViewVisualization v1 = new RelationshipViewVisualization();
        v1.setName("Visualization 1");
        RelationshipViewVisualization v2 = new RelationshipViewVisualization();
        v2.setName("Visualization 2");

        p.addRelationshipView(v1);

        assertTrue(containsReference(p.getRelationshipViews(), v1));
        assertFalse(containsReference(p.getRelationshipViews(), v2));

        p.addRelationshipView(v2);

        assertTrue(containsReference(p.getRelationshipViews(), v1));
        assertTrue(containsReference(p.getRelationshipViews(), v2));

        p.removeRelationshipView(v1);

        assertFalse(containsReference(p.getRelationshipViews(), v1));
        assertTrue(containsReference(p.getRelationshipViews(), v2));

        p.removeAllRelationshipViews();

        assertFalse(containsReference(p.getRelationshipViews(), v1));
        assertFalse(containsReference(p.getRelationshipViews(), v2));

        List<RelationshipViewVisualization> visualizations = new ArrayList<RelationshipViewVisualization>();
        visualizations.add(v1);
        visualizations.add(v2);

        p.setRelationshipViews(visualizations);

        assertTrue(containsReference(p.getRelationshipViews(), v1));
        assertTrue(containsReference(p.getRelationshipViews(), v2));
    }
}

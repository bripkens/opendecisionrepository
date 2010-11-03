package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 * @modified Ben
 */
public class VersionTest {

    private Version v;




    @Before
    public void setUp() {
        v = new Version();
    }




    @Test
    public void testInitialization() {
        assertNull(v.getDocumentedWhen());
        assertNull(v.getDecidedWhen());
        assertNull(v.getId());
        assertNull(v.getState());
        assertFalse(v.isRemoved());
    }




    @Test
    public void testVersionId() {
        v.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) v.getId());
    }







    @Test
    public void testSetDecidedWhen() {
        Date someDate = new Date();
        v.setDecidedWhen(someDate);

        assertEquals(someDate, v.getDecidedWhen());
    }




    @Test(expected = BusinessException.class)
    public void testSetDecidedWhenNull() {
        v.setDecidedWhen(null);
    }




    @Test
    public void testSetDocumentedWhen() {
        Date someDate = new Date();
        v.setDocumentedWhen(someDate);

        assertEquals(someDate, v.getDocumentedWhen());
    }




    @Test(expected = BusinessException.class)
    public void testSetDocumentedWhenNull() {
        v.setDocumentedWhen(null);
    }




    @Test
    public void testSetRemoved() {
        v.setRemoved(true);

        assertTrue(v.isRemoved());

        v.setRemoved(false);

        assertFalse(v.isRemoved());
    }




    @Test
    public void testGetCompareData() {
        Date documentedWhen = new Date();
        Date decidedWhen = new Date(documentedWhen.getTime() - 2000);

        v.setDecidedWhen(decidedWhen);
        v.setDocumentedWhen(documentedWhen);

        assertEquals(documentedWhen, v.getCompareData()[0]);
        assertEquals(decidedWhen, v.getCompareData()[1]);
        assertFalse((Boolean) v.getCompareData()[2]);
    }




    @Test
    public void testIsPersistable() {
        assertFalse(v.isPersistable());

        v.setDecidedWhen(new Date());

        assertFalse(v.isPersistable());

        v.setDocumentedWhen(new Date());

        assertFalse(v.isPersistable());

        v.addInitiator(new ProjectMember());

        assertFalse(v.isPersistable());

        v.setState(new State());

        assertTrue(v.isPersistable());
    }




    @Test
    public void testIsPersistableOtherway() {
        assertFalse(v.isPersistable());

        v.setDocumentedWhen(new Date());

        assertFalse(v.isPersistable());

        v.setDecidedWhen(new Date());

        assertFalse(v.isPersistable());

        v.setState(new State());

        assertFalse(v.isPersistable());

        v.addInitiator(new ProjectMember());

        assertTrue(v.isPersistable());
    }




    @Test
    public void testSetState() {
        State s = new State();

        v.setState(s);

        assertSame(s, v.getState());
    }




    @Test(expected = BusinessException.class)
    public void testSetStateNull() {
        v.setState(null);
    }




    @Test
    public void setRequirements() {
        Collection<Requirement> requirements = new ArrayList<Requirement>();
        Requirement r = new Requirement();
        requirements.add(r);

        v.setRequirements(requirements);

        assertNotSame(requirements, v.getRequirements());
        assertSame(r, v.getRequirements().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setRequirementsNull() {
        v.setRequirements(null);
    }




    @Test
    public void addRequirement() {
        Requirement r = new Requirement();
        v.addRequirement(r);

        assertFalse(v.getRequirements().
                isEmpty());

        assertSame(r, v.getRequirements().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addRequirementNull() {
        v.addRequirement(null);
    }




    @Test
    public void removeRequirement() {
        Requirement r = new Requirement();
        v.addRequirement(r);

        assertFalse(v.getRequirements().
                isEmpty());

        v.removeRequirement(r);

        assertTrue(v.getRequirements().
                isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeRequirementNull() {
        v.removeRequirement(null);
    }




    @Test
    public void removeAllRequirement() {
        Requirement r = new Requirement();
        v.addRequirement(r);

        v.removeAllRequirements();

        assertTrue(v.getRequirements().
                isEmpty());
    }




    @Test
    public void setRelationships() {
        Collection<Relationship> relationships = new ArrayList<Relationship>();
        Relationship relationship = new Relationship();
        relationships.add(relationship);

        v.setRelationships(relationships);

        assertNotSame(relationships, v.getRelationships());
        assertSame(relationship, v.getRelationships().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void setRelationshipsNull() {
        v.setRelationships(null);
    }




    @Test
    public void addRelationship() {
        Relationship relationship = new Relationship();
        v.addRelationship(relationship);

        assertFalse(v.getRelationships().
                isEmpty());

        assertSame(relationship, v.getRelationships().
                iterator().
                next());
    }




    @Test(expected = BusinessException.class)
    public void addRelationshipNull() {
        v.addRelationship(null);
    }




    @Test
    public void removeRelationship() {
        Relationship relationship = new Relationship();
        v.addRelationship(relationship);

        assertFalse(v.getRelationships().
                isEmpty());

        v.removeRelationship(relationship);

        assertTrue(v.getRelationships().
                isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeRelationshipNull() {
        v.removeRelationship(null);
    }




    @Test
    public void removeAllRelationship() {
        Relationship relationship = new Relationship();
        v.addRelationship(relationship);

        v.removeAllRelationships();

        assertTrue(v.getRelationships().
                isEmpty());
    }



    @Test
    public void setProjectMembers() {
        Collection<ProjectMember> initiators = new ArrayList<ProjectMember>();
        ProjectMember initiator = new ProjectMember();
        initiators.add(initiator);

        v.setInitiators(initiators);

        assertNotSame(initiators, v.getInitiators());
        assertSame(initiator, v.getInitiators().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setProjectMembersNull() {
        v.setInitiators(null);
    }




    @Test
    public void addProjectMember() {
        ProjectMember initiator = new ProjectMember();
        v.addInitiator(initiator);

        assertFalse(v.getInitiators().isEmpty());

        assertSame(initiator, v.getInitiators().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addProjectMemberNull() {
        v.addInitiator(null);
    }




    @Test
    public void removeProjectMember() {
        ProjectMember initiator = new ProjectMember();
        v.addInitiator(initiator);

        assertFalse(v.getInitiators().isEmpty());

        v.removeInitiator(initiator);

        assertTrue(v.getInitiators().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeProjectMemberNull() {
        v.removeInitiator(null);
    }




    @Test
    public void removeAllProjectMember() {
        ProjectMember initiator = new ProjectMember();
        v.addInitiator(initiator);

        v.removeAllInitiators();

        assertTrue(v.getInitiators().isEmpty());
    }




    @Test
    public void testGetVersion() {
        Decision d = new Decision();

        Version v1 = new Version();
        v1.setId(1l);
        d.addVersion(v1);

        Version v2 = new Version();
        d.addVersion(v2);

        Version v3 = new Version();
        v3.setId(2l);
        d.addVersion(v3);

        assertEquals(v3, d.getVersion(v3.getId()));
        assertEquals(v1, d.getVersion(v1.getId()));
        assertNull(d.getVersion(5l));

    }



    @Test
    public void testDecidedWhenComparator() {
        Date earlier = new Date();
        Date later = new Date(earlier.getTime() + 1);

        Version predecessor = new Version();
        predecessor.setDecidedWhen(earlier);

        Version successor = new Version();
        successor.setDecidedWhen(later);

        assertTrue(new Version.DecidedWhenComparator().compare(predecessor, successor) < 0);

        assertTrue(new Version.DecidedWhenComparator().compare(successor, predecessor) > 0);
    }
}

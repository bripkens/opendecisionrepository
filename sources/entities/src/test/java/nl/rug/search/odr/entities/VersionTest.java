package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.TestUtil;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Stefan
 */
public class VersionTest {

    private Version v;

    @Before
    public void setUp() {
        v = new Version();
    }

    @Test
    public void testInitialization() {
        assertNull(v.getCreateDate());
//        assertNull(v.getRequirement());
//        assertNull(v.getStatus());
//        assertNull(v.getVersionId());

//        assertNotNull(v.getRelationships());
//        assertTrue(v.getRelationships().isEmpty());
    }

//    @Test
//    public void testVersionId() {
//        v.setVersionId(Long.MIN_VALUE);
//        assertEquals(Long.MIN_VALUE, (long) v.getVersionId());
//    }
//
//    @Test(expected = BusinessException.class)
//    public void testNullVersionId() {
//        v.setVersionId(null);
//    }

    @Test
    public void testSetRevision() {
        String name = "Hans Gerhard";
        int revision = 2;
        v.setRevision(revision);
        assertEquals(revision, v.getRevision());
    }

    @Test(expected = BusinessException.class)
    public void testEmptyRevision() {
        v.setRevision(-1);
    }

    @Test
    public void testRelationshipversionDecision(){
        Version v2 = new Version();
        v2.setCreateDate(new Date());
        v2.setRevision(1);

        ArchitecturalDecision decision = new ArchitecturalDecision();
        decision.setName("testName");
        decision.setArguments("testArguments");
        decision.setOprId("testOprId");
        decision.setProblem("testProblem");
        decision.setDecision("testDecision");

        decision.addVersion(v2);

        assertEquals(v2,decision.getVersions().iterator().next());
        assertEquals(decision, v2.getDecision());
    }

//    @Test
//    public void testSetAction() {
//        Action a = new Action();
//        v.setAction(a);
//
//        assertEquals(a, v.getAction());
//    }

//    @Test(expected = BusinessException.class)
//    public void testNullAction() {
//        v.setAction(null);
//    }

    @Test
    public void testLaterDate() {
        Date now = new Date();
        long time = new Date().getTime() + 10000;
        Date laterDate = new Date(time);

        int i = now.compareTo(laterDate);
        assertTrue(i < 0);
    }

//    @Test
//    public void testSetSatus() {
//        Status s = new Status();
//        v.setStatus(s);
//        assertEquals(s, v.getStatus());
//    }
//
//    @Test(expected = BusinessException.class)
//    public void testNullSetStatus() {
//        v.setStatus(null);
//    }
//
//    @Test
//    public void testSetRequirment() {
//        Requirement r = new Requirement();
//        v.setRequirement(r);
//
//        assertEquals(r, v.getRequirement());
//    }

//    @Test(expected = BusinessException.class)
//    public void testNullSetRequirment() {
//        v.setRequirement(null);
//    }

//    @Test
//    public void testSetRelationship() {
//        Collection<Relationship> relationships = new ArrayList<Relationship>();
//
//        v.setRelationships(relationships);
//
//        assertNotSame(relationships, v.getRelationships());
//    }
//
//    @Test(expected = BusinessException.class)
//    public void testNullRelationship() {
//        v.setRelationships(null);
//    }
//
//    @Test
//    public void testAddRelationship() {
//        Relationship relationship = new Relationship();
//
//        v.addRelationship(relationship);
//        assertTrue(TestUtil.containsReference(v.getRelationships(), relationship));
//        assertSame(v, relationship.getSource());
//    }

//    @Test
//    public void testRemoveRelationShip() {
//        Relationship relationship = new Relationship();
//        v.addRelationship(relationship);
//        v.removeRelationShip(relationship);
//
//        assertFalse(TestUtil.containsReference(v.getRelationships(), relationship));
//    }
//
//    @Test(expected=BusinessException.class)
//    public void setNUlladdRelationship(){
//        Relationship relationship = new Relationship();
//        v.addRelationship(null);
//    }

    @Test
    public void testToString(){
        assertTrue(TestUtil.toStringHelper(v));
    }

//    @Test
//    public void testHashCode(){
//        Date d = new Date();
//        Version v2 = new Version();
//        v2.setVersionId(Long.MIN_VALUE);
//        v2.setCreateDate(d);
//        v2.setRevision(1);
//        v2.setStatus(new Status());
//        v2.setRequirement(new Requirement());
//        Collection<Relationship> rships = new ArrayList<Relationship>();
//        rships.add(new Relationship());
//        v2.setRelationships(rships);
//
//        Version v3 = new Version();
//        v3.setVersionId(Long.MIN_VALUE);
//        v3.setCreateDate(d);
//        v3.setRevision(1);
//        v3.setStatus(new Status());
//        v3.setRequirement(new Requirement());
//        Collection<Relationship> rships1 = new ArrayList<Relationship>();
//        rships.add(new Relationship());
//        v3.setRelationships(rships1);
//
//
//        assertEquals(v2.hashCode(),v3.hashCode());
//        TestUtil.assertNotEquals(v.hashCode(), v2.hashCode());
//    }

//    @Test
//    public void testEquals(){
//        assertFalse(v.equals(new TestUtil()));
//
//        Version v2 = new Version();
//        v2.setVersionId(Long.MIN_VALUE);
//        v2.setCreateDate(new Date());
//        v2.setRevision(1);
//        v2.setStatus(new Status());
//        v2.setRequirement(new Requirement());
//        Collection<Relationship> rships = new ArrayList<Relationship>();
//        rships.add(new Relationship());
//        v2.setRelationships(rships);
//
//        Version v3 = new Version();
//        v3.setVersionId(Long.MAX_VALUE);
//        v3.setCreateDate(new Date());
//        v3.setRevision(2);
//        v3.setStatus(new Status());
//        v3.setRequirement(new Requirement());
//        Collection<Relationship> rships1 = new ArrayList<Relationship>();
//        rships.add(new Relationship());
//        v3.setRelationships(rships1);
//
//        assertFalse(v2.equals(v3));
//
//        assertTrue(v.equals(v));
//    }

    @Test
    public void testNullEquals(){
        assertFalse(v.equals(null));
    }

}

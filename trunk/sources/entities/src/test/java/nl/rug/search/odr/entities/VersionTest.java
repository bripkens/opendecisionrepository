package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
        assertNull(v.getRequirement());
        assertNull(v.getStatus());
        assertNull(v.getVersionId());

        assertNotNull(v.getRelationships());
        assertTrue(v.getRelationships().isEmpty());
    }

    @Test
    public void testVersionId() {
        v.setVersionId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) v.getVersionId());
    }

    @Test(expected = NullPointerException.class)
    public void testNullVersionId() {
        v.setVersionId(null);
    }

    @Test
    public void testSetRevision() {
        String name = "Hans Gerhard";
        int revision = 2;
        v.setRevision(revision);
        assertEquals(revision, v.getRevision());
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyRevision() {
        v.setRevision(-1);
    }

    @Test
    public void testSetAction() {
        Action a = new Action();
        v.setAction(a);

        assertEquals(a, v.getAction());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAction() {
        v.setAction(null);
    }

    @Test
    public void testLaterDate() {
        Date now = new Date();
        long time = new Date().getTime() + 10000;
        Date laterDate = new Date(time);

        int i = now.compareTo(laterDate);
        assertTrue(i < 0);
    }

    @Test
    public void testSetSatus() {
        Status s = new Status();
        v.setStatus(s);
        assertEquals(s, v.getStatus());
    }

    @Test(expected = NullPointerException.class)
    public void testNullSetStatus() {
        v.setStatus(null);
    }

    @Test
    public void testSetRequirment() {
        Requirement r = new Requirement();
        v.setRequirement(r);

        assertEquals(r, v.getRequirement());
    }

    @Test(expected = NullPointerException.class)
    public void testNullSetRequirment() {
        v.setRequirement(null);
    }

    @Test
    public void testSetRelationship() {
        Collection<Relationship> relationships = new ArrayList<Relationship>();

        v.setRelationships(relationships);

        assertNotSame(relationships, v.getRelationships());
    }

    @Test(expected = NullPointerException.class)
    public void testNullRelationship() {
        v.setRelationships(null);
    }

    @Test
    public void testAddRelationship() {
        Relationship relationship = new Relationship();
        Version v2 = new Version();

        relationship.setSourceVersion(v2);
        relationship.setTargetVersion(v);

        v.addRelationship(relationship);

        assertTrue(containsReference(v.getRelationships(), relationship));
        assertTrue(containsReference(v2.getRelationships(), relationship));
    }

    @Test
    public void testRemoveRelationShip() {
        Relationship relationship = new Relationship();
        v.addRelationship(relationship);
        v.removeRelationShip(relationship);

        assertFalse(containsReference(v.getRelationships(), relationship));
    }

//    
    private static <T> boolean containsReference(Collection<T> collection, T item) {
        for (T currentItem : collection) {
            if (currentItem == item) {
                return true;
            }
        }

        return false;
    }
}

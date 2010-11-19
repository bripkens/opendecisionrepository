package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sun.security.provider.certpath.BuildStep;

/**
 *
 * @author Stefan
 * @modified Ben
 */
public class ConcernTest {

    private Concern r;




    @Before
    public void setUp() {
        r = new Concern();
    }




    @Test
    public void testInitialization() {
        assertNull(r.getId());
        assertNull(r.getDescription());
        assertTrue(r.getInitiators().isEmpty());
    }




    @Test
    public void testId() {
        long id = 1;
        r.setId(id);
        assertEquals(1, id);
    }




    @Test(expected = BusinessException.class)
    public void testIdNull() {
        r.setId(null);
    }




    @Test
    public void testSetDescription() {
        String description = "xxx";
        r.setDescription(description);

        assertEquals(description, r.getDescription());
    }




    @Test
    public void testSetName() {
        String name = "xxx";
        r.setName(name);

        assertEquals(name, r.getName());
    }




    @Test(expected = BusinessException.class)
    public void testNullName() {
        r.setName(null);
    }




    @Test(expected = RuntimeException.class)
    public void testEmptyName() {
        r.setName("     ");
    }




    @Test
    public void testCompareData() {
        String name = "sadasds";
        r.setName(name);
        String description = "djsiaodasda";
        r.setDescription(description);


        assertEquals(name, r.getCompareData()[0]);
        assertEquals(description, r.getCompareData()[1]);
    }




    @Test
    public void testIsPersistable() {
        assertFalse(r.isPersistable());

        r.setName("1sdasdsa");

        assertFalse(r.isPersistable());
        r.addInitiator(new ProjectMember());
        assertFalse(r.isPersistable());
        r.setCreatedWhen(new Date());
        assertTrue(r.isPersistable());
    }




    @Test
    public void testSetCreatedWhen() {
        Date now = new Date();
        r.setCreatedWhen(now);
        assertEquals(now, r.getCreatedWhen());
    }




    @Test(expected = BusinessException.class)
    public void testSetCreatedWhenNull() {
        r.setCreatedWhen(null);
    }




    @Test
    public void setProjectMembers() {
        Collection<ProjectMember> initiators = new ArrayList<ProjectMember>();
        ProjectMember initiator = new ProjectMember();
        initiators.add(initiator);

        r.setInitiators(initiators);

        assertNotSame(initiators, r.getInitiators());
        assertSame(initiator, r.getInitiators().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void setProjectMembersNull() {
        r.setInitiators(null);
    }




    @Test
    public void addProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.addInitiator(initiator);

        assertFalse(r.getInitiators().isEmpty());

        assertSame(initiator, r.getInitiators().iterator().next());
    }




    @Test(expected = BusinessException.class)
    public void addProjectMemberNull() {
        r.addInitiator(null);
    }




    @Test
    public void removeProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.addInitiator(initiator);

        assertFalse(r.getInitiators().isEmpty());

        r.removeInitiator(initiator);

        assertTrue(r.getInitiators().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void removeProjectMemberNull() {
        r.removeInitiator(null);
    }




    @Test
    public void removeAllProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.addInitiator(initiator);

        r.removeAllInitiators();

        assertTrue(r.getInitiators().isEmpty());
    }




    @Test
    public void comparatorTestName() {
        r.setName("AAAAA");

        Concern r2 = new Concern();
        r2.setName("BBBBB");

        assertTrue(new Concern.NameComparator().compare(r, r2) < 0);
    }




    @Test
    public void comparatorTestExternalId() {
        r.setExternalId("1");

        Concern r2 = new Concern();
        r2.setExternalId("2");

        Concern.ExternalIdComparator com = new Concern.ExternalIdComparator();
        
        assertTrue(com.compare(r, r2) < 0);
    }




    @Test
    public void addTags() {
        r.addTag("tag1");
        r.addTag("tag2");
        r.addTag("tag3");
        r.addTag("tag4");
        r.addTag("tag5");

        assertEquals(r.getTags().size(), 5);
        r.removeTag("tag2");
        assertFalse(r.getTags().contains("tag2"));
        assertEquals(r.getTags().size(), 4);

        r.addTag("tag6");
        assertEquals(r.getTags().size(), 5);

        r.removeAllTags();
        assertTrue(r.getTags().isEmpty());
    }




    @Test(expected = BusinessException.class)
    public void addTagNull() {
        r.addTag(null);
    }




    @Test(expected = BusinessException.class)
    public void removeTagNull() {
        r.addTag("tag1");
        r.removeTag(null);
    }




    @Test
    public void setTags() {
        Collection<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        r.setTags(tags);
    }




    @Test(expected = BusinessException.class)
    public void setTagsNull() {
        r.setTags(null);
    }




    @Test
    public void externalIdTest() {
        String concern = "Concern-1";
        r.setExternalId(concern);
        assertEquals(r.getExternalId(), concern);
    }




    @Test(expected = BusinessException.class)
    public void externalIdNull() {
        String concern = null;
        r.setExternalId(concern);
    }




    @Test(expected = BusinessException.class)
    public void externalIdEmpty() {
        String concern = "             ";
        r.setExternalId(concern);
    }




    @Test
    public void previousVerison() {
        Long id = 1L;
        r.setGroup(id);
        assertEquals(id, r.getGroup());
    }




    @Test(expected = BusinessException.class)
    public void previousVerisonNull() {
        r.setGroup(null);
    }
}

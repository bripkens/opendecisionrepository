package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import nl.rug.search.odr.BusinessException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        r.setInitiator(new ProjectMember());
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
        ProjectMember initiator = new ProjectMember();

        r.setInitiator(initiator);

        assertSame(initiator, r.getInitiators());
    }




    @Test(expected = BusinessException.class)
    public void setProjectMembersNull() {
        r.setInitiator(null);
    }




    @Test
    public void removeProjectMember() {
        ProjectMember initiator = new ProjectMember();
        r.setInitiator(initiator);

        assertFalse(r.getInitiators() == null);

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
        List<String> tags = new ArrayList<String>();
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




    @Test
    public void testGroupDateComparator() {
        Concern co_1 = new Concern();
        co_1.setName("1");
        co_1.setGroup(1L);
        co_1.setCreatedWhen(new Date(1000));

        Concern co_2 = new Concern();
        co_2.setName("2");
        co_2.setGroup(2L);
        co_2.setCreatedWhen(new Date(2000));

        Concern co_3 = new Concern();
        co_3.setName("3");
        co_3.setGroup(3L);
        co_3.setCreatedWhen(new Date(3000));

        Concern co_4 = new Concern();
        co_4.setName("4");
        co_4.setGroup(4L);
        co_4.setCreatedWhen(new Date(4000));

        Concern co_2_1 = new Concern();
        co_2_1.setName("2_1");
        co_2_1.setGroup(2L);
        co_2_1.setCreatedWhen(new Date(5000));

        Concern co_2_2 = new Concern();
        co_2_2.setName("2_2");
        co_2_2.setGroup(2L);
        co_2_2.setCreatedWhen(new Date(6000));

        Concern co_3_1 = new Concern();
        co_3_1.setName("3_1");
        co_3_1.setGroup(3L);
        co_3_1.setCreatedWhen(new Date(7000));

        Concern co_1_1 = new Concern();
        co_1_1.setName("1_1");
        co_1_1.setGroup(1L);
        co_1_1.setCreatedWhen(new Date(8000));

        Concern co_3_3 = new Concern();
        co_3_3.setName("3_3");
        co_3_3.setGroup(3L);
        co_3_3.setCreatedWhen(new Date(9000));

        List<Concern> concerns = new ArrayList<Concern>();
        concerns.add(co_1);
        concerns.add(co_2);
        concerns.add(co_3);
        concerns.add(co_4);
        concerns.add(co_2_1);
        concerns.add(co_2_2);
        concerns.add(co_3_1);
        concerns.add(co_1_1);
        concerns.add(co_3_3);

        Collections.sort(concerns, new Concern.GroupDateComparator());

        assertEquals(co_1_1.getName(), concerns.get(0).getName());
        assertEquals(co_1.getName(), concerns.get(1).getName());
        assertEquals(co_2_2.getName(), concerns.get(2).getName());
        assertEquals(co_2_1.getName(), concerns.get(3).getName());
        assertEquals(co_2.getName(), concerns.get(4).getName());
        assertEquals(co_3_3.getName(), concerns.get(5).getName());
        assertEquals(co_3_1.getName(), concerns.get(6).getName());
        assertEquals(co_3.getName(), concerns.get(7).getName());
        assertEquals(co_4.getName(), concerns.get(8).getName());
    }
    
    @Test
    public void testGroupDateCompareTo() {
        Concern co_1 = new Concern();
        co_1.setName("1");
        co_1.setGroup(1L);
        co_1.setCreatedWhen(new Date(1000));

        Concern co_2 = new Concern();
        co_2.setName("2");
        co_2.setGroup(2L);
        co_2.setCreatedWhen(new Date(2000));

        Comparator<Concern> comparator = new Concern.GroupDateComparator();

        assertTrue(comparator.compare(co_1, co_2) < 0);

        co_1.setGroup(3l);
        assertTrue(comparator.compare(co_1, co_2) > 0);

        co_1.setCreatedWhen(new Date(3000));
        assertTrue(comparator.compare(co_1, co_2) > 0);

        co_2.setGroup(1L);
        assertTrue(comparator.compare(co_1, co_2) > 0);

        co_1.setGroup(1l);
        co_1.setCreatedWhen(new Date(4000));
        assertTrue(comparator.compare(co_1, co_2) < 0);


        co_2.setCreatedWhen(new Date(4000));
        assertTrue(comparator.compare(co_1, co_2) == 0);
    }




}




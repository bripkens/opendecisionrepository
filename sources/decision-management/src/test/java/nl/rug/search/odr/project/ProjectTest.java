package nl.rug.search.odr.project;

import org.junit.Ignore;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.user.UserBean;
import nl.rug.search.odr.user.UserLocal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectTest extends AbstractEjbTest {

    private ProjectLocal pl;
    private UserLocal ul;
    private StakeholderRoleLocal srl;

    @Before
    public void setUp() {
        pl = lookUp(ProjectBean.class, ProjectLocal.class);
        ul = lookUp(UserBean.class, UserLocal.class);
        srl = lookUp(StakeholderRoleBean.class, StakeholderRoleLocal.class);
    }

    @Test(expected = BusinessException.class)
    public void basics() {
        Project p = new Project();
        pl.createProject(p);
    }

    @Test
    public void projectCreationTest() {
        Person person1 = new Person();
        person1.setName("User1");
        person1.setPlainPassword("Pw1");
        person1.setEmail("dasdsa@dasdas.de");
        ul.register(person1);

        Person person2 = new Person();
        person2.setName("User2");
        person2.setPlainPassword("Pw1");
        person2.setEmail("dasdadassa@dasdas.de");
        ul.register(person2);

        Person person3 = new Person();
        person3.setName("User3");
        person3.setPlainPassword("Pw1");
        person3.setEmail("daaasdadassa@dasdas.de");
        ul.register(person3);

        StakeholderRole role1 = new StakeholderRole();
        role1.setCommon(true);
        role1.setName("Architect");
        srl.persistRole(role1);

        StakeholderRole role2 = new StakeholderRole();
        role2.setCommon(true);
        role2.setName("Manager");
        srl.persistRole(role2);

        StakeholderRole role3 = new StakeholderRole();
        role3.setCommon(true);
        role3.setName("Customer");
        srl.persistRole(role3);


        Project p = new Project();
        p.setName("SomeProject");
        p.setDescription("Adescription");

        ProjectMember member1 = new ProjectMember();
        member1.setPerson(person1);
        member1.setRole(role1);
        p.addMember(member1);

        ProjectMember member2 = new ProjectMember();
        member2.setPerson(person2);
        member2.setRole(role2);
        p.addMember(member2);

        ProjectMember member3 = new ProjectMember();
        member3.setPerson(person3);
        member3.setRole(role3);
        p.addMember(member3);

        pl.createProject(p);

        assertEquals(3, srl.getPublicRoles().size());
        assertNotNull(srl.getSomePublicRole());

        Project pFromDb = pl.getById(p.getId());

        assertEquals(p, pFromDb);

        for(ProjectMember pm : pFromDb.getMembers()) {
            if (!(pm.getPerson().equals(member1.getPerson())
                    || pm.getPerson().equals(member2.getPerson())
                    || pm.getPerson().equals(member3.getPerson()))) {
                fail("Missing members");
            }
        }

        ProjectMember someMember = pFromDb.getMembers().iterator().next();

        someMember.setRemoved(true);

        String newName = "oiuhj132312";
        pFromDb.setName(newName);

        String newDescription = "op32131 23u129120312iu3 12";
        pFromDb.setDescription(newDescription);

        pl.updateProject(pFromDb);

        pFromDb = pl.getById(p.getId());

        boolean found = false;

        for(ProjectMember pm : pFromDb.getMembers()) {
            if ( pm.isRemoved()) {
                found = true;
            }
        }

        assertTrue(found);
        assertEquals(newName, pFromDb.getName());
        assertEquals(newDescription, pFromDb.getDescription());
    }

    @Test
    public void testIsUsedAndIsMember() {
        String name = "foo";

        Person person1 = new Person();
        person1.setName("User1");
        person1.setPlainPassword("Pw1");
        person1.setEmail("dasdsa@dasdas.de");
        ul.register(person1);

        Person person2 = new Person();
        person2.setName("User2");
        person2.setPlainPassword("Pw2");
        person2.setEmail("dasdsa@dadas.de");
        ul.register(person2);

        assertFalse(pl.isUsed(name));

        Project p = new Project();
        p.setName(name);

        StakeholderRole role1 = new StakeholderRole();
        role1.setCommon(true);
        role1.setName("Architect");
        srl.persistRole(role1);

        ProjectMember member = new ProjectMember();
        member.setPerson(person1);
        member.setRole(role1);
        member.setProject(p);

        pl.createProject(p);

        assertTrue(pl.isUsed(name));
        assertTrue(pl.isMember(person1.getId(), p.getId()));
        assertFalse(pl.isMember(person2.getId(), p.getId()));
        

        pl.deleteProject(p);

        assertFalse(pl.isUsed(name));
    }
}

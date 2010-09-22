package nl.rug.search.odr.project;

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

        ProjectMember member = new ProjectMember();
        member.setPerson(person1);
        member.setRole(role1);
        p.addMember(member);

        member = new ProjectMember();
        member.setPerson(person2);
        member.setRole(role2);
        p.addMember(member);

        member = new ProjectMember();
        member.setPerson(person3);
        member.setRole(role3);
        p.addMember(member);

        pl.createProject(p);

        assertEquals(3, srl.getPublicRoles().size());
        assertNotNull(srl.getSomePublicRole());
    }
}

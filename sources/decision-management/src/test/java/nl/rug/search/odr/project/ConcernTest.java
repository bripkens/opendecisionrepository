package nl.rug.search.odr.project;

import java.util.Collection;
import java.util.ArrayList;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.entities.Concern;
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
 * @author Stefan
 */
public class ConcernTest extends AbstractEjbTest {

    private ProjectLocal pl;
    private UserLocal ul;
    private StakeholderRoleLocal srl;
    private ConcernLocal cl;




    @Before
    public void setUp() {
        pl = lookUp(ProjectBean.class, ProjectLocal.class);
        ul = lookUp(UserBean.class, UserLocal.class);
        srl = lookUp(StakeholderRoleBean.class, StakeholderRoleLocal.class);
        cl = lookUp(ConcernBean.class, ConcernLocal.class);
    }




    @Test(expected = BusinessException.class)
    public void basics() {
        Project p = new Project();
        pl.persist(p);
    }




    @Test
    public void concernTest() {
        Person person1 = new Person();
        person1.setName("User1");
        person1.setPlainPassword("Pw1");
        person1.setEmail("dasdsa@dasdas.de");
        ul.register(person1);

        StakeholderRole role1 = new StakeholderRole();
        role1.setCommon(true);
        role1.setName("Architect");
        srl.persist(role1);

        Project p = new Project();
        p.setName("SomeProject");
        p.setDescription("Adescription");

        ProjectMember member1 = new ProjectMember();
        member1.setPerson(person1);
        member1.setRole(role1);
        p.addMember(member1);

        Concern con = new Concern();
        con.setName("Performance");
        con.setDescription("System should be fast");
        con.addTag("performance");
        con.addTag("p2");
        con.addTag("p3");
        con.addTag("speed");
        con.addTag("fast");
        con.setGroup(0L);

        p.addConcern(con);

        pl.persist(p);

        Project pFromDb = pl.getById(p.getId());

        assertEquals(con.getTags().size(), pFromDb.getConcerns().iterator().next().getTags().size());

        Collection<String> similar = new ArrayList<String>();
        similar = cl.getPossibleStrings("p");

        assertTrue(similar.contains("performance"));
        assertTrue(similar.contains("p2"));
        assertTrue(similar.contains("p3"));

        similar = cl.getPossibleStrings("s");
        assertTrue(similar.contains("speed"));
        assertFalse(similar.contains("performance"));

    }

    @Test (expected=BusinessException.class)
        public void testPersist(){
        Person person1 = new Person();
        person1.setName("User1");
        person1.setPlainPassword("Pw1");
        person1.setEmail("dasdsa@dasdas.de");
        ul.register(person1);

        StakeholderRole role1 = new StakeholderRole();
        role1.setCommon(true);
        role1.setName("Architect");
        srl.persist(role1);

        Project p = new Project();
        p.setName("SomeProject");
        p.setDescription("Adescription");

        ProjectMember member1 = new ProjectMember();
        member1.setPerson(person1);
        member1.setRole(role1);
        p.addMember(member1);

        Concern con = new Concern();
        con.setName("Performance");
        con.setDescription("System should be fast");
      
        con.addTag("performance");
        con.addTag("p2");
        con.addTag("p3");
        con.addTag("speed");
        con.addTag("fast");
        con.setInitiator(member1);

        cl.persist(con);


    }
}

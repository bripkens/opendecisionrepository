package nl.rug.search.odr.project;

import java.util.List;
import java.util.Date;
import nl.rug.search.odr.decision.ActionBean;
import nl.rug.search.odr.decision.ActionLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.rug.search.odr.DatabaseCleaner;
import nl.rug.search.odr.decision.ArchitecturalDecisionLocal;
import nl.rug.search.odr.decision.ArchitecturalDecisionBean;
import nl.rug.search.odr.decision.VersionBean;
import nl.rug.search.odr.decision.VersionLocal;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.decision.ActionTypeBean;
import nl.rug.search.odr.decision.ActionTypeLocal;
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
public class IterationTest extends AbstractEjbTest {

    private IterationLocal il;
    private UserLocal ul;
    private StakeholderRoleLocal srl;
    private ProjectLocal pl;
    private VersionLocal vl;
    private ArchitecturalDecisionLocal al;
    private StatusLocal sl;
    private RequirementLocal rl;
    private DatabaseCleaner delteh;
    private ActionTypeLocal atl;
    private ActionLocal alo;

    @Before
    public void setUp() {

        il = lookUp(IterationBean.class, IterationLocal.class);
        ul = lookUp(UserBean.class, UserLocal.class);
        srl = lookUp(StakeholderRoleBean.class, StakeholderRoleLocal.class);
        pl = lookUp(ProjectBean.class, ProjectLocal.class);
        vl = lookUp(VersionBean.class, VersionLocal.class);
        al = lookUp(ArchitecturalDecisionBean.class, ArchitecturalDecisionLocal.class);
        sl = lookUp(StatusBean.class, StatusLocal.class);
        rl = lookUp(RequirementBean.class, RequirementLocal.class);
        atl = lookUp(ActionTypeBean.class, ActionTypeLocal.class);
        alo = lookUp(ActionBean.class, ActionLocal.class);

    }

    @Test
    public void projectIterationTest() {

        System.out.println("es gibt .. user: " + ul.getAll().size());

        // <editor-fold defaultstate="collapsed" desc="create Persons ans save them">
        Person p = TestRelationHelper.createPerson("12345", "a@a.de");
        ul.register(p);

        Person p1 = TestRelationHelper.createPerson("23456", "b@b.de");
        ul.register(p1);

        Person p2 = TestRelationHelper.createPerson("34567", "c@c.de");
        ul.register(p2);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create Stakeholder Roles and save them">
        StakeholderRole role1 = TestRelationHelper.createStakeholderRole("architekt");
        srl.persistRole(role1);

        StakeholderRole role2 = TestRelationHelper.createStakeholderRole("manager");
        srl.persistRole(role2);

        StakeholderRole role3 = TestRelationHelper.createStakeholderRole("customer");
        srl.persistRole(role3);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create ProjectMember ans link them to roles">
        ProjectMember member1 = TestRelationHelper.createProjectMember(p, role1);
        ProjectMember member2 = TestRelationHelper.createProjectMember(p1, role2);
        ProjectMember member3 = TestRelationHelper.createProjectMember(p2, role3);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createProject + add Members ans save it">
        Project pr = TestRelationHelper.createProject(member1, "project1");
        pl.createProject(pr);

        Project pr1 = TestRelationHelper.createProject(member2, "project2");
        pl.createProject(pr1);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createIterationToProject">
        Iteration i = new Iteration();
        i.setDescription("aaa");
        i.setName("first");
        i.setStartDate(new Date());

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }

        Iteration i1 = new Iteration();
        i1.setDescription("bbb");
        i1.setName("second");
        i1.setStartDate(new Date());

        Iteration i2 = new Iteration();
        i2.setDescription("ccc");
        i2.setName("third");
        i2.setStartDate(new Date());

        Iteration i3 = new Iteration();
        i3.setDescription("ccc");
        i3.setName("fourth");
        i3.setStartDate(new Date());


        pr.addIteration(i);
        il.addIteration(pr, i);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IterationTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        pr = pl.getById(pr.getId());
        il.addIteration(pr, i1);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IterationTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        pr1 = pl.getById(pr1.getId());
        il.addIteration(pr1, i2);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IterationTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        pr1 = pl.getById(pr1.getId());
        il.addIteration(pr1, i3);

        assertEquals(2, pl.getById(pr.getId()).getIterations().size());
        assertEquals(2, pl.getById(pr1.getId()).getIterations().size());


        assertEquals(4, il.getAll().size());

        List<Iteration> iterations = il.getAll();
        int count = 0;
        for (Iteration it : iterations) {
            if (it.getEndDate() != null) {
                count++;
            }
        }
        assertEquals(2, count);
    }
}

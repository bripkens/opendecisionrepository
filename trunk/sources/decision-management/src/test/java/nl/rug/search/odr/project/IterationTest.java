package nl.rug.search.odr.project;

import org.junit.Ignore;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.rug.search.odr.DatabaseCleaner;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.decision.DecisionBean;
import nl.rug.search.odr.decision.VersionBean;
import nl.rug.search.odr.decision.VersionLocal;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.AbstractEjbTest;
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

    private IterationLocal iterationLocal;

    private UserLocal ul;

    private StakeholderRoleLocal srl;

    private ProjectLocal projectLocal;

    private VersionLocal vl;

    private DecisionLocal al;

    private StateLocal sl;

    private ConcernLocal rl;

    private DatabaseCleaner delteh;




    @Before
    public void setUp() {

        iterationLocal = lookUp(IterationBean.class, IterationLocal.class);
        ul = lookUp(UserBean.class, UserLocal.class);
        srl = lookUp(StakeholderRoleBean.class, StakeholderRoleLocal.class);
        projectLocal = lookUp(ProjectBean.class, ProjectLocal.class);
        vl = lookUp(VersionBean.class, VersionLocal.class);
        al = lookUp(DecisionBean.class, DecisionLocal.class);
        sl = lookUp(StateBean.class, StateLocal.class);
        rl = lookUp(ConcernBean.class, ConcernLocal.class);

    }




    @Test
    public void projectIterationTest() {

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
        srl.persist(role1);

        StakeholderRole role2 = TestRelationHelper.createStakeholderRole("manager");
        srl.persist(role2);

        StakeholderRole role3 = TestRelationHelper.createStakeholderRole("customer");
        srl.persist(role3);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create ProjectMember ans link them to roles">
        ProjectMember member1 = TestRelationHelper.createProjectMember(p, role1);
        ProjectMember member2 = TestRelationHelper.createProjectMember(p1, role2);
        ProjectMember member3 = TestRelationHelper.createProjectMember(p2, role3);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createProject + add Members ans save it">
        Project pr = TestRelationHelper.createProject(member1, "project1");
        projectLocal.persist(pr);

        Project pr1 = TestRelationHelper.createProject(member2, "project2");
        projectLocal.persist(pr1);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createIterationToProject">
        GregorianCalendar startCal = new GregorianCalendar(2012, 1, 10);
        GregorianCalendar endCal = new GregorianCalendar(2012, 1, 12);

        GregorianCalendar startCal1 = new GregorianCalendar(2012, 1, 15);
        GregorianCalendar endCal1 = new GregorianCalendar(2012, 1, 17);

//        //#################################################################
//        fails because the dates are not valid
//        GregorianCalendar startCal2 = new GregorianCalendar(2012, 1, 9);
//        GregorianCalendar endCal2 = new GregorianCalendar(2012,1,18);
//
//        GregorianCalendar startCal3 = new GregorianCalendar(2012, 1, 11);
//        GregorianCalendar endCal3 = new GregorianCalendar(2012,1,13);
//
//        GregorianCalendar startCal4 = new GregorianCalendar(2012, 1, 13);
//        GregorianCalendar endCal4 = new GregorianCalendar(2012,1,16);
//
//        GregorianCalendar startCal5 = new GregorianCalendar(2012, 1, 11);
//        GregorianCalendar endCal5 = new GregorianCalendar(2012,1,16);
//        //################################################################

        GregorianCalendar startCal6 = new GregorianCalendar(2012, 1, 18);
        GregorianCalendar endCal6 = new GregorianCalendar(2012, 1, 19);

        GregorianCalendar startCal7 = new GregorianCalendar(2012, 1, 20);
        GregorianCalendar endCal7 = new GregorianCalendar(2012, 2, 2);

        Iteration i = new Iteration();
        i.setDescription("aaa");
        i.setName("first");
        i.setProjectMember(member1);
        i.setStartDate(startCal.getTime());
        i.setDocumentedWhen(new Date());
        i.setEndDate(endCal.getTime());

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }

        Iteration i1 = new Iteration();
        i1.setDescription("bbb");
        i1.setName("second");
        i1.setProjectMember(member2);
        i1.setStartDate(startCal1.getTime());
        i1.setDocumentedWhen(new Date());
        i1.setEndDate(endCal1.getTime());

        Iteration i2 = new Iteration();
        Date date = new Date();
        i2.setDescription("ccc");
        i2.setName("third");
        i2.setProjectMember(member3);
        i2.setStartDate(startCal6.getTime());
        i2.setDocumentedWhen(date);
        i2.setEndDate(endCal6.getTime());


        Iteration i3 = new Iteration();
        i3.setDescription("ccc");
        i3.setName("fourth");
        i3.setProjectMember(member1);
        i3.setStartDate(startCal7.getTime());
        i3.setDocumentedWhen(new Date());
        i3.setEndDate(endCal7.getTime());


        pr.addIteration(i);
        pr.addIteration(i);
        projectLocal.merge(pr);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IterationTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        pr = projectLocal.getById(pr.getId());
        pr.addIteration(i1);
        projectLocal.merge(pr);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IterationTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        pr1 = projectLocal.getById(pr1.getId());
        pr1.addIteration(i2);
        projectLocal.merge(pr1);

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IterationTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        pr1 = projectLocal.getById(pr1.getId());
        pr1.addIteration(i3);
        projectLocal.merge(pr1);

        assertEquals(2, projectLocal.getById(pr.getId()).getIterations().size());
        assertEquals(2, projectLocal.getById(pr1.getId()).getIterations().size());

        assertEquals(4, iterationLocal.getAll().size());

        List<Iteration> iterations = iterationLocal.getAll();
        int count = 0;
        for (Iteration it : iterations) {
            if (it.getEndDate() != null) {
                count++;
            }
        }
        assertEquals(4, count);

        pr1 = projectLocal.getById(pr1.getId());
        pr1.removeIteration(i3);
        projectLocal.merge(pr1);

        assertEquals(1, projectLocal.getById(pr1.getId()).getIterations().size());

        Iteration iter = projectLocal.getById(pr1.getId()).getIterations().iterator().next();
        assertEquals(member3, iter.getProjectMember());
        assertEquals(startCal6.getTime(), iter.getStartDate());
        assertEquals(endCal6.getTime(), iter.getEndDate());
        assertEquals(date, iter.getDocumentedWhen());

    }




    @Test
    public void intersectionTest() {

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
        srl.persist(role1);

        StakeholderRole role2 = TestRelationHelper.createStakeholderRole("manager");
        srl.persist(role2);

        StakeholderRole role3 = TestRelationHelper.createStakeholderRole("customer");
        srl.persist(role3);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create ProjectMember ans link them to roles">
        ProjectMember member1 = TestRelationHelper.createProjectMember(p, role1);
        ProjectMember member2 = TestRelationHelper.createProjectMember(p1, role2);
        ProjectMember member3 = TestRelationHelper.createProjectMember(p2, role3);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createProject + add Members ans save it">
        Project project = TestRelationHelper.createProject(member1, "project1");
        projectLocal.persist(project);

        Project pr1 = TestRelationHelper.createProject(member2, "project2");
        projectLocal.persist(pr1);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createIterationToProject">
        GregorianCalendar start1940 = new GregorianCalendar(1940, 1, 10);
        GregorianCalendar end2012 = new GregorianCalendar(2012, 1, 12);

        GregorianCalendar start2010 = new GregorianCalendar(2010, 1, 15);
        GregorianCalendar end2014 = new GregorianCalendar(2014, 1, 17);


        GregorianCalendar start2011 = new GregorianCalendar(2011, 1, 18);
        GregorianCalendar end2011 = new GregorianCalendar(2011, 1, 19);

        GregorianCalendar start1950 = new GregorianCalendar(1950, 1, 20);
        GregorianCalendar end1970 = new GregorianCalendar(1970, 2, 2);

        GregorianCalendar start2020 = new GregorianCalendar(2020,1,1);
        GregorianCalendar end2040 = new GregorianCalendar(2040,1,1);

        Iteration smallIteration = new Iteration();
        smallIteration.setDescription("aaa");
        smallIteration.setName("first");
        smallIteration.setProjectMember(member1);
        smallIteration.setStartDate(start2010.getTime());
        smallIteration.setDocumentedWhen(new Date());
        smallIteration.setEndDate(end2012.getTime());



        Iteration spanningIteration = new Iteration();
        spanningIteration.setDescription("bbb");
        spanningIteration.setName("second");
        spanningIteration.setProjectMember(member2);
        spanningIteration.setStartDate(start2010.getTime());
        spanningIteration.setDocumentedWhen(new Date());
        spanningIteration.setEndDate(end2014.getTime());

        Iteration earlyIteration = new Iteration();
        Date date = new Date();
        earlyIteration.setDescription("ccc");
        earlyIteration.setName("early");
        earlyIteration.setProjectMember(member3);
        earlyIteration.setStartDate(start1950.getTime());
        earlyIteration.setDocumentedWhen(date);
        earlyIteration.setEndDate(end1970.getTime());


        Iteration laterIteration = new Iteration();
        laterIteration.setDescription("ccc");
        laterIteration.setName("fourth");
        laterIteration.setProjectMember(member1);
        laterIteration.setStartDate(start2020.getTime());
        laterIteration.setDocumentedWhen(new Date());
        laterIteration.setEndDate(end2040.getTime());

        Iteration startDateIntersectionIteration = new Iteration();
        startDateIntersectionIteration.setDescription("ddd");
        startDateIntersectionIteration.setName("fiveth");
        startDateIntersectionIteration.setProjectMember(member2);
        startDateIntersectionIteration.setStartDate(start2011.getTime());
        startDateIntersectionIteration.setDocumentedWhen(new Date());
        startDateIntersectionIteration.setEndDate(end2040.getTime());


        Iteration endDateIntersectionIteration = new Iteration();
        endDateIntersectionIteration.setDescription("ddd");
        endDateIntersectionIteration.setName("fiveth");
        endDateIntersectionIteration.setProjectMember(member2);
        endDateIntersectionIteration.setStartDate(start1950.getTime());
        endDateIntersectionIteration.setDocumentedWhen(new Date());
        endDateIntersectionIteration.setEndDate(end2011.getTime());


        project.addIteration(smallIteration);
        projectLocal.merge(project);

        project = projectLocal.getById(project.getId());
        assertTrue(iterationLocal.isIntersection(spanningIteration, project.getId()));
        assertFalse(iterationLocal.isIntersection(earlyIteration, project.getId()));
        assertFalse(iterationLocal.isIntersection(laterIteration, project.getId()));
        assertTrue(iterationLocal.isIntersection(startDateIntersectionIteration, project.getId()));
        assertTrue(iterationLocal.isIntersection(endDateIntersectionIteration, project.getId()));

        project.addIteration(earlyIteration);
        projectLocal.merge(project);


        List<Iteration>  iterations = iterationLocal.getAll();
        for (int i = 0; i < iterations.size(); i++) {
            if(iterations.get(i).getName().equals("early")){
                earlyIteration = iterations.get(i);
                break;
            }

        }

        earlyIteration.setStartDate(start1940.getTime());
        assertFalse(iterationLocal.isIntersection(earlyIteration, project.getId()));



//        try {
//            pr = pl.getById(pr.getId());
//            il.persist(i2);
//            pl.merge(pr);
//            fail();
//        } catch (Exception ex) {
//        }


//
//        try {
//            pr = pl.getById(pr.getId());
//            il.persist(i3);
//            pl.merge(pr);
//            fail();
//        } catch (Exception ex) {
//        }
    }


}

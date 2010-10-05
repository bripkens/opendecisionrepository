package nl.rug.search.odr.project;

import nl.rug.search.odr.entities.Action;
import nl.rug.search.odr.decision.ActionBean;
import nl.rug.search.odr.decision.ActionLocal;
import nl.rug.search.odr.entities.ActionType;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.rug.search.odr.DatabaseCleaner;
import nl.rug.search.odr.entities.Requirement;
import nl.rug.search.odr.decision.ArchitecturalDecisionLocal;
import nl.rug.search.odr.decision.ArchitecturalDecisionBean;
import nl.rug.search.odr.decision.VersionBean;
import nl.rug.search.odr.decision.VersionLocal;
import nl.rug.search.odr.entities.Status;
import nl.rug.search.odr.entities.Version;
import java.util.Collection;
import java.util.ArrayList;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.decision.ActionTypeBean;
import nl.rug.search.odr.decision.ActionTypeLocal;
import nl.rug.search.odr.entities.ArchitecturalDecision;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.user.UserBean;
import nl.rug.search.odr.user.UserLocal;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stefan
 */
@Ignore
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

        // <editor-fold defaultstate="collapsed" desc="create Persons">
        Person p = TestRelationHelper.createPerson("12345", "a@a.de");
        ul.register(p);

        Person p1 = TestRelationHelper.createPerson("23456", "b@b.de");
        ul.register(p1);

        Person p2 = TestRelationHelper.createPerson("34567", "c@c.de");
        ul.register(p2);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create Stakeholder Roles">
        StakeholderRole role1 = TestRelationHelper.createStakeholderRole("architekt");
        srl.persistRole(role1);

        StakeholderRole role2 = TestRelationHelper.createStakeholderRole("manager");
        srl.persistRole(role2);

        StakeholderRole role3 = TestRelationHelper.createStakeholderRole("customer");
        srl.persistRole(role3);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create ProjectMember">
        ProjectMember member1 = TestRelationHelper.createProjectMember(p, role1);
        ProjectMember member2 = TestRelationHelper.createProjectMember(p1, role2);
        ProjectMember member3 = TestRelationHelper.createProjectMember(p2, role3);

        Collection<ProjectMember> members = new ArrayList<ProjectMember>();
        members.add(member1);
        members.add(member2);
        members.add(member3);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createProject + add Members">
        Project pr = TestRelationHelper.createProject(members);
        pl.createProject(pr);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="createIterationToProject">
        Iteration i = TestRelationHelper.createIteration();
        pr.addIteration(i);
        pl.updateProject(pr);
        // </editor-fold>

        //create Decisions
        ArchitecturalDecision decision = TestRelationHelper.createArchiteturalDecision("decision1");
        al.persistDecision(decision);
        ArchitecturalDecision decision2 = TestRelationHelper.createArchiteturalDecision("decision2");
        al.persistDecision(decision2);

        assertEquals("decision1", al.getById(decision.getId()).getName());
        assertEquals("decision2", al.getById(decision2.getId()).getName());


        //create statuses
        Status statusConfirm = TestRelationHelper.createStatus("Confirm");
        sl.persistStatus(statusConfirm);
        Status statusRejected = TestRelationHelper.createStatus("Rejected");
        sl.persistStatus(statusRejected);

        //create requirement
        Requirement require = TestRelationHelper.createRequirement("fast");
        rl.persistRequirement(require);


        //create Version
        Version v1 = TestRelationHelper.createVersion(1);
        Version v2 = TestRelationHelper.createVersion(2);

        //status auf version setzen
        v1.setStatus(statusConfirm);
        v1.setArchitecturalDecision(decision);
        v1.addRequirment(require);

        v2.setStatus(statusRejected);
        v2.setArchitecturalDecision(decision2);


        //save version
        vl.persistVersion(v1);
        vl.persistVersion(v2);

        assertEquals(1, vl.getById(v1.getId()).getRequirements().size());

        //add version to iteration
//        i.addVersion(v1);
//        i.addVersion(v2);

        assertEquals(2, sl.getAll().size());

        assertEquals(statusConfirm, vl.getById(v1.getId()).getStatus());
        assertEquals(statusRejected, vl.getById(v2.getId()).getStatus());

        //create Actiontypes
        ActionType validate = TestRelationHelper.createActionType("validate");
        atl.persistActionType(validate);
        ActionType confirm = TestRelationHelper.createActionType("confirm");
        atl.persistActionType(confirm);

        assertEquals(2, atl.getAll().size());

        Action action = TestRelationHelper.createAction(member1, validate);

        Action action2 = TestRelationHelper.createAction(member2, confirm);

        v1.setAction(action);
        v2.setAction(action2);

        vl.updateVersion(v1);
        vl.updateVersion(v2);

        assertEquals(action, vl.getById(v1.getId()).getAction());
        assertEquals(action2, vl.getById(v2.getId()).getAction());

        assertEquals(member1, vl.getById(v1.getId()).getAction().getMember());
        assertEquals(member2, vl.getById(v2.getId()).getAction().getMember());

        assertEquals(validate, vl.getById(v1.getId()).getAction().getType());
        assertEquals(confirm, vl.getById(v2.getId()).getAction().getType());


    }
}

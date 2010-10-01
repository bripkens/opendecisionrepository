package nl.rug.search.odr.project;

import nl.rug.search.odr.entities.Requirement;
import nl.rug.search.odr.decision.ArchitecturalDecisionLocal;
import nl.rug.search.odr.decision.ArchitecturalDecisionBean;
import nl.rug.search.odr.decision.VersionBean;
import nl.rug.search.odr.decision.VersionLocal;
import nl.rug.search.odr.entities.Status;
import nl.rug.search.odr.entities.Version;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.AbstractEjbTest;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.entities.ArchitecturalDecision;
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

        //add version to iteration
        i.addVersion(v1);
        i.addVersion(v2);


    }
}

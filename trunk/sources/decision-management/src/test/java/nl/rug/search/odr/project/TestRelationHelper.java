/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.project;

import java.util.Collection;
import java.util.Date;
import nl.rug.search.odr.entities.ArchitecturalDecision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Requirement;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.entities.Status;
import nl.rug.search.odr.entities.Version;
import org.junit.Ignore;

/**
 *
 * @author Stefan
 */
@Ignore
public class TestRelationHelper {

    public static Person createPerson(String name, String email) {
        Person person1 = new Person();
        person1.setName(name);
        person1.setPlainPassword("Pw1341");
        person1.setEmail(email);
        return person1;
    }


    public static StakeholderRole createStakeholderRole(String name) {
        StakeholderRole role1 = new StakeholderRole();
        role1.setCommon(true);
        role1.setName(name);
        return role1;
    }

    public static ProjectMember createProjectMember(Person p, StakeholderRole role) {
        ProjectMember member = new ProjectMember();
        member.setPerson(p);
        member.setRole(role);
        return member;
    }

    public static Project createProject(Collection<ProjectMember> members) {
        Project p = new Project();
        p.setName("SomeProject1");
        p.setDescription("Adescription1");
        p.setMembers(members);
        return p;
    }

    public static Iteration createIteration() {
        Iteration i = new Iteration();
        i.setDescription("first Iteration for ODR");
        i.setName("First iteration");
        i.setStartDate(new Date());
        i.setEndDate(new Date());
        return i;
    }

    public static Version createVersion(int revision) {
        Version v = new Version();
        v.setCreateDate(new Date());
        v.setRevision(revision);
        return v;
    }

    public static ArchitecturalDecision createArchiteturalDecision(String name){
        ArchitecturalDecision decision = new ArchitecturalDecision();
        decision.setArguments("testArguments");
        decision.setDecision("testDecision");
        decision.setName(name);
        decision.setOprId("testoprID");
        decision.setProblem("lotoProblems");

        return decision;
    }

    public static Status createStatus(String name){
        Status status = new Status();
        status.setCommon(true);
        status.setName(name);
        return status;
    }

    public static Requirement createRequirement(String name){
        Requirement re = new Requirement();
        re.setDescription(name);
        return re;
    }

}

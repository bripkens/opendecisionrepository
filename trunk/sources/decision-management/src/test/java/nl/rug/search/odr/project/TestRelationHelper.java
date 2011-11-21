/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.project;

import java.util.Date;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.entities.State;
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




    public static Project createProject(ProjectMember member, String name) {
        Project p = new Project();
        p.setName(name);
        p.setDescription("Adescription1");
        p.addMember(member);
        return p;
    }




    public static Iteration createIteration(String name) {
        Iteration i = new Iteration();
        i.setDescription("first Iteration for ODR");
        i.setName(name);
        i.setStartDate(new Date());
        return i;
    }




    public static Version createVersion(int revision) {
        Version v = new Version();
        v.setDocumentedWhen(new Date());
        return v;
    }




    public static Decision createArchiteturalDecision(String name) {
        Decision decision = new Decision();
        decision.setName(name);

        return decision;
    }





    public static Concern createConcerns(String name) {
        Concern re = new Concern();
        re.setDescription(name);
        return re;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 * @modified Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Requirement.getAll",
                query= "SELECT r FROM Requirement r")
})
@Entity
public class Requirement extends BaseEntity<Requirement> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @ManyToMany
    private Collection<ProjectMember> initiators;




    public Requirement() {
        initiators = new ArrayList<ProjectMember>();
    }




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.id = id;
    }




    public String getDescription() {
        return description;
    }




    public void setDescription(String description) {
        StringValidator.isValid(description);
        this.description = description;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }





    public Collection<ProjectMember> getInitiators() {
        return Collections.unmodifiableCollection(initiators);
    }




    public void setInitiators(Collection<ProjectMember> initiators) {
        if (initiators == null) {
            throw new BusinessException("Initiators is null");
        }

        this.initiators = initiators;
    }




    public void addInitiator(ProjectMember initiator) {
        if (initiator == null) {
            throw new BusinessException("Initiator is null");
        }

        this.initiators.add(initiator);
    }




    public void removeInitiator(ProjectMember initiator) {
        if (initiator == null) {
            throw new BusinessException("Initiator is null");
        }

        this.initiators.remove(initiator);
    }




    public void removeAllInitiators() {
        initiators.clear();
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, description};
    }




    @Override
    public boolean isPersistable() {
        return name != null && !initiators.isEmpty();
    }

    public static class NameComparator implements Comparator<Requirement> {

        @Override
        public int compare(Requirement o1, Requirement o2) {
            return o1.name.compareToIgnoreCase(o2.name);
        }

    }
}

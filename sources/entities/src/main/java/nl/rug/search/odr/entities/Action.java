/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
//@Entity
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private ActionType type;
    @ManyToOne
    private ProjectMember member;

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        if (type != null) {
            this.type = type;
        } else {
            throw new NullPointerException("ActionTyp is null");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new NullPointerException("Please provide an id");
        }
        this.id = id;
    }

    public ProjectMember getMember() {
        return member;
    }

    public void setMember(ProjectMember member) {
        if (member == null) {
            throw new NullPointerException("Please provide a member");
        }
        this.member = member;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(member).append(serialVersionUID).append(type).toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }

        Action ac = (Action) object;
        return new EqualsBuilder().append(id, ac.id).
                append(member, ac.member).
                append(type, ac.type).
                isEquals();
    }

    @Override
    public String toString() {
        return "Action{" + "id=" + id + "type=" + type + '}';
    }
}

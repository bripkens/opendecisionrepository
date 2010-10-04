/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Stefan
 */
@Entity (name="Actions")
public class Action extends BaseEntity<Action> {

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
            throw new BusinessException("ActionTyp is null");
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Action id is null");
        }
        this.id = id;
    }

    public ProjectMember getMember() {
        return member;
    }

    public void setMember(ProjectMember member) {
        if (member == null) {
            throw new BusinessException("Member is null");
        }
        this.member = member;
    }


    @Override
    public String toString() {
        return "Action{type=" + type + '}';
    }


    @Override
    protected Object[] getCompareData() {
        return new Object[]{type};
    }

}

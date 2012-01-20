/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author fragger
 */
@Entity
public class Rating extends BaseEntity<Rating> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Effect effect;
    @ManyToOne(optional=false)
    private Decision decision;
    @ManyToOne(optional=false)
    private Concern concern;

    public Concern getConcern() {
        return concern;
    }

    public void setConcern(Concern concern) {
        this.concern = concern;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect ratingType) {
        this.effect = ratingType;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isPersistable() {
        return !(decision == null || concern == null || effect == null);
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{effect};
    }
}

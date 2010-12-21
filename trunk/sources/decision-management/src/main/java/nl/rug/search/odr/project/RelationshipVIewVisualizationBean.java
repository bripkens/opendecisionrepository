/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.project;

import java.io.Serializable;
import javax.ejb.Stateless;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

/**
 *
 * @author ben
 */
@Stateless
public class RelationshipVIewVisualizationBean extends GenericDaoBean<RelationshipViewVisualization, Long> implements RelationshipViewVisualizationLocal {

    @Override
    public boolean isPersistable(RelationshipViewVisualization entity) {
        return entity.isPersistable();
    }

}

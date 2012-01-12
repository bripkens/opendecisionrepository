/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

import javax.ejb.Stateless;

/**
 *
 * @author ben
 */
@Stateless
public class RelationshipViewVisualizationBean extends GenericDaoBean<RelationshipViewVisualization, Long> implements RelationshipViewVisualizationLocal {

    @Override
    public boolean isPersistable(RelationshipViewVisualization entity) {
        return entity.isPersistable();
    }

}

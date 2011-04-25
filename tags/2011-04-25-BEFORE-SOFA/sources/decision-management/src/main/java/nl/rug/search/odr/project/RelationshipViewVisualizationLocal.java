/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.project;

import java.io.Serializable;
import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

/**
 *
 * @author ben
 */
@Local
public interface RelationshipViewVisualizationLocal extends GenericDaoLocal<RelationshipViewVisualization, Long>{

}

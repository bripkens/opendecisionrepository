/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.project;

import java.io.Serializable;
import javax.ejb.Stateless;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.viewpoint.Visualization;

/**
 *
 * @author ben
 */
@Stateless
public class VisualizationBean extends GenericDaoBean<Visualization, Long> implements VisualizationLocal {

    @Override
    public boolean isPersistable(Visualization entity) {
        return entity.isPersistable();
    }

}

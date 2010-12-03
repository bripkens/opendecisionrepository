/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.viewpoint.chronological.ChronologicalViewVisualization;

/**
 *
 * @author ben
 */
@Stateless
public class ChronologicalVIewVisualizationBean extends GenericDaoBean<ChronologicalViewVisualization, Long> implements ChronologicalViewVisualizationLocal {

    @Override
    public boolean isPersistable(ChronologicalViewVisualization entity) {
        return entity.isPersistable();
    }

}

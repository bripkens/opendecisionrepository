/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.project;

import javax.ejb.Local;
import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.viewpoint.chronological.ChronologicalViewVisualization;

/**
 *
 * @author ben
 */
@Local
public interface ChronologicalViewVisualizationLocal extends GenericDaoLocal<ChronologicalViewVisualization, Long>{
    
}

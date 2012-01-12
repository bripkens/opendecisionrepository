/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common.comparator;

import java.util.Comparator;
import nl.rug.search.odr.ws.dto.DecisionDTO;

/**
 *
 * @author Michael
 */
public class DecisionNameComparator implements Comparator<DecisionDTO> {

    @Override
    public int compare(DecisionDTO o1, DecisionDTO o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}

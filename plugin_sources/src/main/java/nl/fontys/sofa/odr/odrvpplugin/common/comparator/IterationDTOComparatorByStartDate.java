/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common.comparator;

import java.util.Comparator;
import nl.rug.search.odr.ws.dto.IterationDTO;

/**
 *
 * @author Michael
 */
public class IterationDTOComparatorByStartDate implements Comparator<IterationDTO> {

        @Override
        public int compare(IterationDTO o1, IterationDTO o2) {
            return o1.getStartDate().compareTo(o2.getStartDate());
        }
    }

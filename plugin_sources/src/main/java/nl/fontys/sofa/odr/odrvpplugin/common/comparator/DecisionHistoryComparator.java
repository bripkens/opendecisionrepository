/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common.comparator;

import java.util.Comparator;
import nl.fontys.sofa.odr.odrvpplugin.controller.Views.Helper.DecisionHistory;

/**
 *
 * @author Michael
 */
/**
 * Comparator to compare two DecisionHistories by it's decided when date
 */
public class DecisionHistoryComparator implements Comparator<DecisionHistory> {

    @Override
    public int compare(DecisionHistory o1, DecisionHistory o2) {
        return o1.getHistoryDTO().getDecidedWhen().compareTo(o2.getHistoryDTO().getDecidedWhen());
    }
}

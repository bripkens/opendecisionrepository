/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.controller.Views.Helper;

import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;

/**
 *
 * @author eigo
 */
public class DecisionHistory {

    private DecisionDTO decisionDTO;
    private HistoryDTO historyDTO;

    /**
     * Helperclass for chronolgical view
     * @param decisionDTO stores decisionDTO
     * @param historyDTO actual historyDTO used
     */
    public DecisionHistory(DecisionDTO decisionDTO, HistoryDTO historyDTO) {
        this.decisionDTO = decisionDTO;
        this.historyDTO = historyDTO;
    }

    /**
     * getter for DecisionDTO
     * @return
     */
    public DecisionDTO getDecisionDTO() {
        return decisionDTO;
    }

    /**
     * getter for HistoryDTO
     * @return
     */
    public HistoryDTO getHistoryDTO() {
        return historyDTO;
    }

    /**
     * setter for DecisionDTO
     * @param decisionDTO
     */
    public void setDecisionDTO(DecisionDTO decisionDTO) {
        this.decisionDTO = decisionDTO;
    }

    /**
     * setter for HistoryDTO
     * @param historyDTO
     */
    public void setHistoryDTO(HistoryDTO historyDTO) {
        this.historyDTO = historyDTO;
    }
}

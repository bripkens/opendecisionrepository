package nl.rug.search.odr.ws.assembler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionStateAssembler {
    public DecisionStateAssembler() {
        throw new UnsupportedOperationException("Utility Class");
    }
    
    public static List<DecisionStateDTO> ASSEMBLE(
            Collection<State> allStates) {
        List<DecisionStateDTO> allDTOs;
        allDTOs = new LinkedList<DecisionStateDTO>();
        
        for(State state : allStates) {
            allDTOs.add(ASSEMBLE(state));
        }
        
        return allDTOs;
    }
    
    public static DecisionStateDTO ASSEMBLE(State state) {
        DecisionStateDTO dto = new DecisionStateDTO();
        dto.setId(state.getId());
        dto.setName(state.getStatusName());
        dto.setInitialState(state.isInitialState());
        dto.setCommon(state.isCommon());
        return dto;
    }
}

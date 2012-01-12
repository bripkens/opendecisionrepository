package nl.fontys.sofa.odr.odrvpplugin.controller.helper;

import nl.rug.search.odr.ws.dto.DecisionDTO;

/**
 *
 * @author Michael
 */
public class DecisionDTOWrapper extends DecisionDTO {

    private boolean modified=false;
    
    /**
     * wrapperclass for decisiondto to support modified field
     * it's used for identification 
     */
    public DecisionDTOWrapper() {
    }
    
    /**
     * sets modified flag
     * @param b modified
     */
    public void setModified(boolean b){
        modified = b;
    }
    
    /**
     * returns modified flag
     * @return
     */
    public boolean isModified(){
        return modified;
    }
}

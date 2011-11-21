package nl.rug.search.odr.ws.assembler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipTypeAssembler {
    public RelationshipTypeAssembler() {
        throw new UnsupportedOperationException("Utility Class");
    }
    
    public static List<RelationshipTypeDTO> ASSEMBLE(
            Collection<RelationshipType> allTypes) {
        List<RelationshipTypeDTO> allDTOs;
        allDTOs = new LinkedList<RelationshipTypeDTO>();
        
        for(RelationshipType type : allTypes) {
            allDTOs.add(ASSEMBLE(type));
        }
        
        return allDTOs;
    }
    
    public static RelationshipTypeDTO ASSEMBLE(RelationshipType type) {
        RelationshipTypeDTO dto = new RelationshipTypeDTO();
        dto.setId(type.getId());
        dto.setDescription(type.getDescription());
        dto.setName(type.getName());
        dto.setCommon(type.isCommon());
        return dto;
    }
}

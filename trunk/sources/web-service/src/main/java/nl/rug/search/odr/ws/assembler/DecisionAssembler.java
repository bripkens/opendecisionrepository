package nl.rug.search.odr.ws.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.TemplateComponent;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;
import nl.rug.search.odr.ws.dto.RelationshipDTO;
import nl.rug.search.odr.ws.dto.TextualDescriptionDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DecisionAssembler {
    
    public DecisionAssembler() {
        throw new UnsupportedOperationException("Utility Class");
    }
    
    public static DecisionDTO ASSEMBLE(Decision d) {
        DecisionDTO dto = new DecisionDTO();
        dto.setName(d.getName());
        dto.setId(d.getId());
        
        Version currentVersion = d.getCurrentVersion();
        
        for(Relationship relationship : currentVersion.getOutgoingRelationships()) {
            RelationshipDTO rDTO = new RelationshipDTO();
            rDTO.setId(relationship.getId());
            rDTO.setRelationshipType(relationship.getType().getName());
            rDTO.setTargetId(relationship.getTarget().getDecision().getId());
            dto.getRelationships().add(rDTO);
        }
        
        for(Version version : d.getVersions()) {
            HistoryDTO hDTO = new HistoryDTO();
            hDTO.setDecidedWhen(version.getDecidedWhen());
            hDTO.setDocumentedWhen(version.getDocumentedWhen());
            hDTO.setState(version.getState().getStatusName());
            
            List<String> initiators = new ArrayList<String>(version
                    .getInitiators().size());
            for (ProjectMember pm : version.getInitiators()) {
                initiators.add(pm.getPerson().getName());
            }
            hDTO.setInitiators(initiators);
            dto.getHistory().add(hDTO);
        }
        
        Collections.sort(dto.getHistory(), 
                Collections.reverseOrder(new HistoryDTO.DecidedWhenComparator()));
        
        
        Collection<TemplateComponent> components;
        components = d.getTemplate().getComponents();
        
        Collection<ComponentValue> textualDescriptions = d.getValues();
        for (ComponentValue textualDescription : textualDescriptions) {
            TemplateComponent component = textualDescription.getComponent();
            
            if (components.contains(component)) {
                TextualDescriptionDTO tDTO = new TextualDescriptionDTO();
                tDTO.setLabel(component.getLabel());
                tDTO.setOrder(component.getOrder());
                tDTO.setContent(textualDescription.getValue());
                dto.getDescription().add(tDTO);
            }
        }
        
        Collections.sort(dto.getDescription(),
                new TextualDescriptionDTO.OrderComparator());
        
        return dto;
    }
}

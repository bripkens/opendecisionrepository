package nl.rug.search.odr.ws.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.ws.MimeType;
import nl.rug.search.odr.ws.SessionBeanRepository;
import nl.rug.search.odr.ws.assembler.DecisionStateAssembler;
import nl.rug.search.odr.ws.dto.DecisionStateDTOList;
import nl.rug.search.odr.ws.security.SkipAuthentication;
import nl.rug.search.odr.ws.security.SkipGroupVerification;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/")
public class DecisionStateResource {
    
    private StateLocal sl = SessionBeanRepository
            .getInstance()
            .getStateLocal();
    
    @GET
    @Path("/decisionstates")
    @Produces({MimeType.JSON, MimeType.XML})
    @SkipAuthentication
    @SkipGroupVerification
    public DecisionStateDTOList getCommonDecisionStates() {
        return new DecisionStateDTOList(DecisionStateAssembler
                .ASSEMBLE(sl.getCommonStates()));
    }
}

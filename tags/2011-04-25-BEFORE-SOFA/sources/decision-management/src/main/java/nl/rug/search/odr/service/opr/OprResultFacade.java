
package nl.rug.search.odr.service.opr;

import java.util.List;
import nl.rug.search.odr.service.Paragraph;
import nl.rug.search.odr.service.Result;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class OprResultFacade implements Result {

    private final nl.rug.search.opr.entities.Result oprResult;
    private final OprWebservice service;

    private List<Paragraph> paragraphs;

    OprResultFacade(nl.rug.search.opr.entities.Result oprResult, OprWebservice service) {
        this.oprResult = oprResult;
        this.service = service;
    }


    @Override
    public String getName() {
        return oprResult.getName();
    }




    @Override
    public List<Paragraph> getDocumentation() {
        if (paragraphs == null) {
            paragraphs = service.getDocumentation(oprResult.getId());
        }
        
        return paragraphs;
    }

}

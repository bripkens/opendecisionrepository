package nl.rug.search.odr.service.opr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.service.AbstractWebservice;
import nl.rug.search.odr.service.Paragraph;
import nl.rug.search.odr.service.ParagraphImpl;
import nl.rug.search.odr.service.Result;
import nl.rug.search.opr.entities.Content;
import nl.rug.search.opr.entities.PatternDTO;
import nl.rug.search.opr.entities.ResultList;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class OprWebservice implements AbstractWebservice {

    private static final String ENTITIES_PACKAGE = "nl.rug.search.opr.entities";

    private static final String SEARCH_URL = "http://www.patternrepository.com:8080/WebServices/resources/search?q=";

    private static final String GET_URL = "http://www.patternrepository.com:8080/WebServices/resources/pattern/";




    @Override
    public List<Result> search(String query) {
        try {
            Unmarshaller u = ContextHolder.CONTEXT.createUnmarshaller();

            URL url = new URL(SEARCH_URL.concat(query));

            JAXBElement<ResultList> result = (JAXBElement<ResultList>) u.unmarshal(url.openStream());

            return transformToOdrResults(result.getValue().getResult());
        } catch (JAXBException ex) {
            throw new BusinessException(ex);
        } catch (MalformedURLException ex) {
            throw new BusinessException(ex);
        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
    }




    private List<Result> transformToOdrResults(List<nl.rug.search.opr.entities.Result> oprResults) {
        List<Result> odrResults = new ArrayList<Result>(oprResults.size());

        for (nl.rug.search.opr.entities.Result oprResult : oprResults) {
            Result odrResult = new OprResultFacade(oprResult, this);
            odrResults.add(odrResult);
        }

        return odrResults;
    }




    List<Paragraph> getDocumentation(long id) {
        try {
            Unmarshaller u = ContextHolder.CONTEXT.createUnmarshaller();

            URL url = new URL(GET_URL + id);

            JAXBElement<PatternDTO> result = (JAXBElement<PatternDTO>) u.unmarshal(url.openStream());

            return transformToOdrDocumentation(result.getValue());
        } catch (JAXBException ex) {
            throw new BusinessException(ex);
        } catch (MalformedURLException ex) {
            throw new BusinessException(ex);
        } catch (IOException ex) {
            throw new BusinessException(ex);
        }
    }




    private List<Paragraph> transformToOdrDocumentation(PatternDTO pattern) {
        List<Paragraph> result = new ArrayList<Paragraph>(pattern.getContent().size());

        for (Content content : pattern.getContent()) {
            Paragraph p = new ParagraphImpl(content.getName(), content.getText());
            result.add(p);
        }

        return result;
    }

    private static class ContextHolder {

        private static final JAXBContext CONTEXT;




        static {
            try {
                CONTEXT = JAXBContext.newInstance(ENTITIES_PACKAGE);
            } catch (JAXBException ex) {
                throw new BusinessException(ex);
            }
        }
    }
}

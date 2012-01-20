package nl.rug.search.odr.ws;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "rest-schema",
            urlPatterns = {"/rest-schema"})
public class SchemaProvider extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/xml");

        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(WebServiceSchema.SCHEMA);
        } catch (JAXBException ex) {
            throw new ServletException(ex);
        }
        
        final OutputStream out = response.getOutputStream();
        
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri,
                    String suggestedFileName) throws IOException {
                Result r = new StreamResult(out);
                r.setSystemId("odr");
                return r;
            }
            
            
        });
    }
}

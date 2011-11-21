package nl.rug.search.odr.ws.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import nl.rug.search.odr.ws.WebServiceSchema;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Stefan
 */
class WebServiceConnection {

    private final DefaultHttpClient httpClient;
    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;

    WebServiceConnection(String email, String password) {
        httpClient = new DefaultHttpClient();
        UsernamePasswordCredentials credentials;
        credentials = new UsernamePasswordCredentials(email + ":" + password);
        httpClient.getCredentialsProvider()
                .setCredentials(AuthScope.ANY, credentials);
        
        try {
            JAXBContext ctx = JAXBContext.newInstance(WebServiceSchema.SCHEMA);
            marshaller = ctx.createMarshaller();
            unmarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    <T> T getAndUnmarshal(String url, Class<T> expected) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = execute(request);
        
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new WebServiceConnectionException(response.getStatusLine());
        }
        
        String entity = IOUtils.toString(response.getEntity().getContent());
        return unmarshal(entity, expected);
    }
    
    <T> T unmarshal(String xml, Class<T> expected) {
        try {
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    StringEntity marshal(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(obj, out);
            StringEntity result;
            result = new StringEntity(new String(out.toByteArray()), "UTF-8");
            result.setContentType("application/xml");
            return result;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    HttpResponse execute(HttpUriRequest request) throws IOException {
        request.setHeader("Accept", "application/xml");
        return httpClient.execute(request);
    }
    
}

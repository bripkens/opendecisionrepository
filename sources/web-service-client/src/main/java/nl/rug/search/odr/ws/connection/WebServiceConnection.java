package nl.rug.search.odr.ws.connection;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import nl.rug.search.odr.ws.WebServiceSchema;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Stefan
 */
public class WebServiceConnection {

    private final String email, authHeader;
    private final Unmarshaller unmarshaller;

    public WebServiceConnection(String email, String password) {
        this.email = email;

        byte[] rawAuthInformation = (email + ":" + password).getBytes();
        authHeader = new String(Base64.encodeBase64(rawAuthInformation));

        try {
            unmarshaller = JAXBContext.newInstance(WebServiceSchema.SCHEMA)
                    .createUnmarshaller();
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getEmail() {
        return email;
    }

    public <T> T retrieveAndUnmarshal(String url, Class<T> expected)
            throws IOException {
        return unmarshal(retrieve(url), expected);
    }

    private <T> T unmarshal(String xml, Class<T> expected) {
        try {
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String retrieve(String url) throws IOException {
        HttpURLConnection connection = null;

        try {
            connection = connect(url);

            return IOUtils.toString(connection.getInputStream(), "UTF-8").trim();
        } catch (IOException ex) {
            if (connection != null) {
                throw new WebServiceConnectionException(ex,
                        connection.getResponseCode());
            } else {
                throw ex;
            }
        }
    }

    private HttpURLConnection connect(String url) throws MalformedURLException,
            IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url)
                .openConnection();

        connection.setRequestProperty("Authorization",
                "Basic ".concat(authHeader));
        connection.setRequestProperty("Accept", "application/xml");

        return connection;
    }
}

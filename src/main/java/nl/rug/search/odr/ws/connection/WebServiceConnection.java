package nl.rug.search.odr.ws.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import nl.rug.search.odr.ws.WebServiceSchema;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Stefan
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class WebServiceConnection {

    private static final int HTTPS_PORT = 443;
    private static final int HTTP_LOWEST_OK_STATUS = 200;
    private static final int HTTP_HIGHEST_OK_STATUS = 299;
    
    private final DefaultHttpClient httpClient;
    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;

    WebServiceConnection() {
        httpClient = wrapClient(new DefaultHttpClient());
        
        try {
            JAXBContext ctx = JAXBContext.newInstance(WebServiceSchema.SCHEMA);
            marshaller = ctx.createMarshaller();
            unmarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            throw new WebServiceSetupException(ex);
        }
    }
    
    WebServiceConnection(String email, String password) {
        this();
        UsernamePasswordCredentials credentials;
        credentials = new UsernamePasswordCredentials(email + ":" + password);
        httpClient.getCredentialsProvider()
                .setCredentials(AuthScope.ANY, credentials);
    }

    <T> T unmarshal(String xml, Class<T> expected) {
        return unmarshal(IOUtils.toInputStream(xml), expected);
    }
    
    <T> T unmarshal(InputStream in, Class<T> expected) {
        try {
            return (T) unmarshaller.unmarshal(in);
        } catch (JAXBException ex) {
            throw new WebServiceMarshalException(ex);
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
            throw new WebServiceMarshalException(ex);
        } catch (JAXBException ex) {
            throw new WebServiceMarshalException(ex);
        }
    }

    HttpResponse execute(HttpUriRequest request) throws IOException {
        request.setHeader("Accept", "application/xml");
        request.setHeader("Content-Type", "application/xml; charset=UTF-8");
        HttpResponse response = httpClient.execute(request);
        
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < HTTP_LOWEST_OK_STATUS ||
                statusCode > HTTP_HIGHEST_OK_STATUS) {
            String error = IOUtils.toString(response.getEntity().getContent());
            consume(response.getEntity());
            throw new WebServiceRequestException(response.getStatusLine(), error);
        }
        
        return response;
    }
    
    <T> T getAndUnmarshal(String url, Class<T> expected) throws IOException {
        HttpGet request = new HttpGet(url);
        return executeAndUnmarshal(request, expected);
    }
    
    <T> T executeAndUnmarshal(HttpUriRequest request,
            Class<T> expected) throws IOException {
        HttpResponse response = execute(request);
        T result = unmarshal(response.getEntity().getContent(), expected);
        consume(response.getEntity());
        return result;
    }
    
    void consume(HttpEntity entity) throws IOException {
        EntityUtils.consume(entity);
    }
    
    private static DefaultHttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String s)
                        throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] xcs, String s)
                        throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            X509HostnameVerifier verifier = new X509HostnameVerifier() {
                @Override
                public void verify(String string, SSLSocket ssls)
                        throws IOException {
                }
                @Override
                public void verify(String string, X509Certificate xc)
                        throws SSLException {
                }
                @Override
                public void verify(String s1, String[] s2, String[] s3)
                        throws SSLException {
                }
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, verifier);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", HTTPS_PORT, ssf));
            return new DefaultHttpClient(ccm);
        } catch (Exception ex) {
            throw new WebServiceSetupException(ex);
        }
    }
}

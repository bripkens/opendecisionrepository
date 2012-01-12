/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import nl.rug.search.odr.ws.WebServiceSchema;

/**
 *
 * @author Michael
 */
public final class Serializer {

    private static Marshaller marshaller = null;
    private static Unmarshaller unmarshaller = null;
    private static ByteArrayOutputStream out = new ByteArrayOutputStream();

    private Serializer() {
    }

    /**
     * serializer for objects
     * @param <T> generic identifier
     * @param t object to serialize
     * @param expected expected type
     * @return serialized object
     */
    public static <T> String encode(T t, Class<T> expected) {
        Class<?>[] schema = Arrays.copyOf(WebServiceSchema.SCHEMA, WebServiceSchema.SCHEMA.length + 1);
        schema[schema.length - 1] = DiagramProperties.class;

        try {
            if (marshaller == null) {
                marshaller = JAXBContext.newInstance(getExtendedSchema()).createMarshaller();
            }
            marshaller.marshal(t, out);
            return new String(out.toByteArray(), "UTF-8");
        } catch (JAXBException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out = new ByteArrayOutputStream();
        }
        return "";
    }

    /**
     * deserializer for serialized objects
     * @param <T> generic type
     * @param xml serialized object
     * @param expected expected type
     * @return deserialized object
     */
    public static <T> T decode(String xml, Class<T> expected) {

        if (xml == null || xml.isEmpty() || expected==null) {
            throw new IllegalArgumentException("check ur arguments");
        }
        try {
            if (unmarshaller == null) {
                unmarshaller = JAXBContext.newInstance(getExtendedSchema()).createUnmarshaller();
            }
            return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        } catch (JAXBException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Class[] getExtendedSchema() {
        Class<?>[] schema = Arrays.copyOf(WebServiceSchema.SCHEMA, WebServiceSchema.SCHEMA.length + 1);
        schema[schema.length - 1] = DiagramProperties.class;
        return schema;
    }
}

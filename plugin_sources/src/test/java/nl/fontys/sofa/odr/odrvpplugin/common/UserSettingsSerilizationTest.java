/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import junit.framework.Assert;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test class to see if the UserSettings class can be serialized.
 * @author theo
 */
public class UserSettingsSerilizationTest {

    UserSettings original;
    UserSettings deSerialized;
    List<DecisionStateDTO> decisionStates;
    List<RelationshipTypeDTO> relationships;
    RelationshipTypeDTO[] relationshipsArr;
    DecisionStateDTO[] decisionstateDTOArr;

    public UserSettingsSerilizationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        original = new UserSettings();
        original.setUsername("original user");
        original.setPassword("original password");
        Map<Long, Color> colorMapping = new HashMap<Long, Color>();
        colorMapping.put(1l, Color.red);
        colorMapping.put(2l, Color.GREEN);

        decisionStates = new ArrayList<DecisionStateDTO>();

        decisionstateDTOArr = new DecisionStateDTO[5];
        for (int i = 0; i < decisionstateDTOArr.length; i++) {
            decisionstateDTOArr[i] = new DecisionStateDTO();
            decisionstateDTOArr[i].setId(i);
            decisionstateDTOArr[i].setName("DTO" + i);
            decisionStates.add(decisionstateDTOArr[i]);
        }

        original.setDecisionStates(decisionStates);

        relationships = new ArrayList<RelationshipTypeDTO>();
        relationshipsArr = new RelationshipTypeDTO[5];
        for (int i = 0; i < relationshipsArr.length; i++) {
            relationshipsArr[i] = new RelationshipTypeDTO();
            relationshipsArr[i].setId(i);
            relationshipsArr[i].setName("relationship" + i);
            relationships.add(relationshipsArr[i]);
        }

        original.setRelationshipTypes(relationships);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSerialization() {
        System.out.println("Test: serialization");
        JAXBContext context;
        Marshaller marshaller;

        int expectedException = 0;
        int actualException = 0;
        String xml = null;
        try {
            context = JAXBContext.newInstance(UserSettings.class);
            marshaller = context.createMarshaller();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(original, out);
            xml = out.toString();
            out.close();
        } catch (JAXBException jxb) {
            System.out.println(jxb.getMessage());
            actualException = 1;
        } catch (IOException ioEx) {
        }

        Assert.assertEquals(expectedException, actualException);

        System.out.println("Test: de-serialization");
        Unmarshaller unmarshaller;
        UserSettings unmarshalled = null;

        try {
            context = JAXBContext.newInstance(UserSettings.class);
            unmarshaller = context.createUnmarshaller();
            InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            unmarshalled = (UserSettings) unmarshaller.unmarshal(in);
        } catch (JAXBException jxb) {
            System.out.println(jxb.getMessage());
        } catch (UnsupportedEncodingException usEncEx) {
            System.out.println(usEncEx.getMessage());
        }

        assertThat(unmarshalled.getUsername(), is(equalTo(original.getUsername())));
        assertThat(unmarshalled.getPassword(), is(equalTo(original.getPassword())));
        List<RelationshipTypeDTO> unmarshalledRelationshipTypes = unmarshalled.getRelationshipTypes();
        List<DecisionStateDTO> unmarshalledDecisionStates = unmarshalled.getDecisionStates();

        // TODO: There is probally room for improvement here......
        for (RelationshipTypeDTO actual : unmarshalledRelationshipTypes) {
            int index = (int) actual.getId();
            assertThat(relationshipsArr[index].getName(), is(equalTo(actual.getName())));
            assertThat(relationshipsArr[index].getId(), is(equalTo(actual.getId())));
        }

        for (DecisionStateDTO actual : unmarshalledDecisionStates) {
            int index = (int) actual.getId();
            assertThat(decisionstateDTOArr[index].getId(), is(equalTo(actual.getId())));
            assertThat(decisionstateDTOArr[index].getName(), is(equalTo(actual.getName())));
        }

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import java.util.List;
import nl.rug.search.odr.ws.dto.EditDecisionDTO;
import java.util.ArrayList;
import java.util.Date;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;
import nl.rug.search.odr.ws.dto.RelationshipDTO;
import nl.rug.search.odr.ws.dto.TextualDescriptionDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael
 */
public class SerializerTest {

    private DecisionDTO decisionDTO;
    private EditDecisionDTO editDecisionAsReference;

    public SerializerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        decisionDTO = new DecisionDTO();
        decisionDTO.setId(Long.MAX_VALUE);
        decisionDTO.setName("testName");
        decisionDTO.setDescription(new ArrayList<TextualDescriptionDTO>());
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setDecidedWhen(new Date());
        historyDTO.setDocumentedWhen(new Date());
        historyDTO.setId(Long.MIN_VALUE);
        historyDTO.setInitiators(new ArrayList<String>());
        historyDTO.setState("state");
        List<HistoryDTO> list = new ArrayList<HistoryDTO>();
        list.add(historyDTO);
        decisionDTO.setHistory(list);
        decisionDTO.setRelationships(new ArrayList<RelationshipDTO>());
        
        editDecisionAsReference = decisionDTO.toEditDecisionDTO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of encode method, of class Serializer.
     */
    @Test
    public void testDecodeAndEncode() {
        String result = Serializer.encode(decisionDTO, DecisionDTO.class);
        DecisionDTO deserializedObj = Serializer.decode(result, DecisionDTO.class);

        assertTrue(deserializedObj.toEditDecisionDTO().equals(editDecisionAsReference));
    }

    /**
     * Test of encode method, of class Serializer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecode() {
        Serializer.decode("", DecisionDTO.class);
    }
}

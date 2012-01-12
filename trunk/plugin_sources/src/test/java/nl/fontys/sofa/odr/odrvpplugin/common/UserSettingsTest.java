/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import java.util.ArrayList;
import java.util.List;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 *
 * @author theo
 */
public class UserSettingsTest {

    UserSettings original;

    public UserSettingsTest() {
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
        String username = "original user";
        String password = "original password";
        Map<Long, Color> colorMapping = new HashMap<Long, Color>();
        colorMapping.put(1l, Color.GREEN);
        colorMapping.put(2l, Color.red);
        colorMapping.put(3l, Color.black);

        original.setUsername(username);
        original.setPassword(password);
        original.setDecisionStateColorMapping(colorMapping);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of clone method, of class UserSettings.
     */
    @Test
    public void testClone() {
        System.out.println("Test: clone");
        UserSettings clone = original.toDeepCopy();
        clone.setUsername("clones username");
        clone.setPassword("clones password");

        clone.putDecisionStateColor(1l, Color.yellow);

        assertThat(original.getUsername(), is(not(equalTo(clone.getUsername()))));
        assertThat(original.getPassword(), is(not(equalTo(clone.getPassword()))));

        assertThat(original.getDecisionStateColor(1l), is(not(equalTo(clone.getDecisionStateColor(1l)))));
    }

    @Test
    public void testAddNewDecisions() {
        List<DecisionStateDTO> decisionStateDTOList = new ArrayList<DecisionStateDTO>();

        DecisionStateDTO[] decisionStateDTOArr = new DecisionStateDTO[10];
        for (int i = 0; i < 10; i++) {
            decisionStateDTOArr[i] = new DecisionStateDTO();
            decisionStateDTOArr[i].setId(i + 1);
            decisionStateDTOList.add(decisionStateDTOArr[i]);
        }

        assertThat(original.getDecisionStateColorMap().size(), is(3));
        original.setDecisionStates(decisionStateDTOList);
        assertThat(original.getDecisionStateColorMap().size(), is(decisionStateDTOList.size()));
        // at this point, in the usersettings object, the decisionstateColorMapping-
        // object should have more entries, filled with random values.

        // make sure the stored values havent changed.
        assertThat(original.getDecisionStateColor(1l), is(equalTo(Color.GREEN)));
        assertThat(original.getDecisionStateColor(2l), is(equalTo(Color.red)));
        assertThat(original.getDecisionStateColor(3l), is(equalTo(Color.black)));

        // make sure that all decisionstate has a color mapped to it.
        for (int i = 1; i < 11; i++) { 
            assertThat(original.getDecisionStateColor((long)i), is(notNullValue()));
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

/**
 *
 * @author theo
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * "Repository" to store settings specific for a user as well as 
 * specifics for use with the decision repository. In this version these 
 * settings are stored:
 * Username, 
 * Password, non-encrypted
 * A mapping between the id-field of a DecisionStateDTO 
 * and a color,
 * A list of DecisionStateDTO's,
 * A List of RelationshipDTO's,
 * @author Theo Rutten
 */
@XmlRootElement(name = "root")
public class UserSettings {

    private String username;
    private String password;
    private String url;
    @XmlJavaTypeAdapter(HashMapAdapter.class)
    private Map<Long, Color> decisionStateColorMap;
    private List<DecisionStateDTO> decisionStates;
    private List<RelationshipTypeDTO> relationshipTypes;
    private static final int OFFSET = 200;
    private static final int MAX = 255;
    private BasicTextEncryptor textEncryptor;

    public UserSettings() {
        decisionStateColorMap = new HashMap<Long, Color>();
        decisionStates = new ArrayList<DecisionStateDTO>();
        relationshipTypes = new ArrayList<RelationshipTypeDTO>();
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("myVeryStrongPassword");
    }

    /**
     * @return UserSettings object that is deep copied.
     */
    public UserSettings toDeepCopy() {
        UserSettings clone = new UserSettings();
        clone.setUsername(this.username);
        clone.setPassword(this.password);
        clone.setDecisionStateColorMapping(
                new HashMap(this.decisionStateColorMap));
        clone.setDecisionStates(new ArrayList(this.decisionStates));
        clone.setRelationshipTypes(new ArrayList(this.relationshipTypes));
        return clone;
    }

    /**
     * Overrides a possible present mapping between a decisions state and color.
     * @param decisionStateDTOId - the decision state id
     * @param color - the newly chosen color object 
     */
    public void putDecisionStateColor(Long decisionStateDTOId, Color color) {
        this.decisionStateColorMap.put(decisionStateDTOId, color);
    }

    public Color getDecisionStateColor(Long decisionStateDTOid) {
        return decisionStateColorMap.get(decisionStateDTOid);
    }

    public Color getDecisionStateColor(String decisionStateDTOName) {
        Color ret = null;
        for (DecisionStateDTO decisionState : decisionStates) {
            if (decisionState.getName().equals(decisionStateDTOName)) {
                ret = decisionStateColorMap.get(decisionState.getId());
                break;
            }
        }
        return ret;
    }

    public Map<Long, Color> getDecisionStateColorMap() {
        return decisionStateColorMap;
    }

    public List<DecisionStateDTO> getDecisionStates() {
        return decisionStates;
    }

    @XmlTransient
    public String getEncryptedPassword() {
        return textEncryptor.decrypt(this.password);

    }

    
    public void setPassword(String password) {
        this.password = password;

    }

    public List<RelationshipTypeDTO> getRelationshipTypes() {
        return relationshipTypes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setEncryptedPassword(String password) {
        this.password = textEncryptor.encrypt(password);

    }

    public void setDecisionStateColorMapping(
            Map<Long, Color> decisionStateColorMap) {
        this.decisionStateColorMap = decisionStateColorMap;
    }

    /**
     * In the case that there are new decision states they are downloaded and 
     * stored in the user settings object. If there are new decision states, 
     * there should be 
     * new random colors generated for them.
     * @param decisionStates 
     */
    public void setDecisionStates(List<DecisionStateDTO> decisionStates) {
        this.decisionStates = decisionStates;
        for (DecisionStateDTO decisionStateDTO : decisionStates) {
            Color color = decisionStateColorMap.get(decisionStateDTO.getId());
            if (color == null) {
                int r = getValue();
                int g = getValue();
                int b = getValue();
                decisionStateColorMap.put(
                        decisionStateDTO.getId(),
                        new Color(r, g, b));
            }
        }
    }

    /*
     * return a value between OFFSET and MAX
     */
    private int getValue() {
        Random r = new Random();
        return OFFSET + r.nextInt(MAX - OFFSET);
    }

    public void setRelationshipTypes(
            List<RelationshipTypeDTO> relationshipStates) {
        this.relationshipTypes = relationshipStates;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * HashMapAdapter : XmlAdapter
     * Class that 'helps' the JAXB-marshaller to (un-)marshal a hashmap. 
     * JAXB in its current version 2.1 at the time (I believe) has some issues
     * handling HashMaps. This adapter class prevents that problem.
     */
    private static class HashMapAdapter extends XmlAdapter<MapType, Map<Long, Color>> {

        @Override
        public MapType marshal(Map<Long, Color> map) {
            MapType mapType = new MapType();
            for (Entry<Long, Color> entry : map.entrySet()) {
                MapEntry mapEntry = new MapEntry();
                mapEntry.decisionState = entry.getKey();

                // this ackward code is explained in the MapEntry class
                mapEntry.rValue = entry.getValue().getRed();
                mapEntry.gValue = entry.getValue().getGreen();
                mapEntry.bValue = entry.getValue().getBlue();
                mapType.entryList.add(mapEntry);
            }
            return mapType;
        }

        @Override
        public Map<Long, Color> unmarshal(MapType type) throws Exception {
            Map<Long, Color> map = new HashMap<Long, Color>();
            for (MapEntry entry : type.entryList) {
                map.put(entry.decisionState,
                        new Color(entry.rValue, entry.gValue, entry.bValue));
            }
            return map;
        }
    }

    private static class MapType {

        public MapType() {
        }
        @XmlElement(name = "mapping")
        private List<MapEntry> entryList = new ArrayList<MapEntry>();//NOSONAR 
        //used by JAX-RS
    }

    private static class MapEntry {

        public MapEntry() {
        }
        // decision state
        @XmlElement
        private Long decisionState; //NOSONAR used by JAX-RS
        /**
         * the color accosiated with it.. but not in a javax.awt.Color object, 
         * because Color has no default empty constructor and thus could not be 
         * handled by the JAXB marshaller.
         */
        @XmlElement
        private int rValue;//NOSONAR used by JAX-RS
        @XmlElement
        private int gValue;//NOSONAR used by JAX-RS
        @XmlElement
        private int bValue;//NOSONAR used by JAX-RS
    }
}
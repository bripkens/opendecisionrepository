package nl.rug.search.odr.ws.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name="decisionState")
@DTO
public class DecisionStateDTO {
    private long id;
    private String name;
    private boolean common, initialState;

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInitialState() {
        return initialState;
    }

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }
}

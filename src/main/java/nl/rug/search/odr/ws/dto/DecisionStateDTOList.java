package nl.rug.search.odr.ws.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name = "decisionStateList")
@DTO
public class DecisionStateDTOList {
    private List<DecisionStateDTO> list;

    public DecisionStateDTOList() {
    }

    public DecisionStateDTOList(List<DecisionStateDTO> list) {
        this.list = list;
    }

    @XmlElement(name = "item")
    public List<DecisionStateDTO> getList() {
        return list;
    }

    public void setList(List<DecisionStateDTO> list) {
        this.list = list;
    }
}

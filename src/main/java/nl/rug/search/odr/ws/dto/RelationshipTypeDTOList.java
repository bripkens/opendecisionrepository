package nl.rug.search.odr.ws.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name = "relationshipTypeList")
@DTO
public class RelationshipTypeDTOList {
    private List<RelationshipTypeDTO> list;

    public RelationshipTypeDTOList() {
    }

    public RelationshipTypeDTOList(List<RelationshipTypeDTO> list) {
        this.list = list;
    }

    @XmlElement(name = "item")
    public List<RelationshipTypeDTO> getList() {
        return list;
    }

    public void setList(List<RelationshipTypeDTO> list) {
        this.list = list;
    }
}

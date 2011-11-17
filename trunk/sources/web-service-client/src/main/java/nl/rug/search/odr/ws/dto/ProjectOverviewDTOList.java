package nl.rug.search.odr.ws.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name = "projectOverviewList")
@DTO
public class ProjectOverviewDTOList {

    private List<ProjectOverviewDTO> list;

    public ProjectOverviewDTOList() {
    }

    public ProjectOverviewDTOList(List<ProjectOverviewDTO> list) {
        this.list = list;
    }

    @XmlElement(name = "item")
    public List<ProjectOverviewDTO> getList() {
        return list;
    }

    public void setList(List<ProjectOverviewDTO> list) {
        this.list = list;
    }
}

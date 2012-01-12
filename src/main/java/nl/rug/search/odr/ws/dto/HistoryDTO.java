package nl.rug.search.odr.ws.dto;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name="history")
@DTO
public class HistoryDTO {
    private Long id;
    private String state;
    private List<String> initiators;
    private Date documentedWhen;
    private Date decidedWhen;

    public HistoryDTO() {
        initiators = new LinkedList<String>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDecidedWhen() {
        return decidedWhen;
    }

    public void setDecidedWhen(Date decidedWhen) {
        this.decidedWhen = decidedWhen;
    }

    public Date getDocumentedWhen() {
        return documentedWhen;
    }

    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }

    public List<String> getInitiators() {
        return initiators;
    }

    public void setInitiators(List<String> initiators) {
        this.initiators = initiators;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public static class DecidedWhenComparator implements Comparator<HistoryDTO> {

        @Override
        public int compare(HistoryDTO o1, HistoryDTO o2) {
            return o1.getDecidedWhen().compareTo(o2.getDecidedWhen());
        }
        
    }
}

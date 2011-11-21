package nl.rug.search.odr.ws.dto;

import java.util.Comparator;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name="textualDescription")
@DTO
public class TextualDescriptionDTO {
    private String label, content;
    private int order;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
    public static class OrderComparator implements Comparator<TextualDescriptionDTO> {
        @Override
        public int compare(TextualDescriptionDTO o1, TextualDescriptionDTO o2) {
            return o1.getOrder() - o2.getOrder();
        }
        
    }
}

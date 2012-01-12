package nl.rug.search.odr.ws.dto;

import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author fragger
 */
@XmlRootElement(name="rating")
@DTO
public class RatingDTO {
        
    private Long id;
    private Effect effect;
    private long decisionId;
    private long concernId;

    public long getConcernId() {
        return concernId;
    }

    public void setConcernId(long concernId) {
        this.concernId = concernId;
    }

    public long getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(long decisionId) {
        this.decisionId = decisionId;
    }


    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect ratingType) {
        this.effect = ratingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}

package nl.rug.search.odr.ws.assembler;

import nl.rug.search.odr.entities.Rating;
import nl.rug.search.odr.ws.dto.Effect;
import nl.rug.search.odr.ws.dto.RatingDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public final class RatingAssembler {

    private RatingAssembler() {
        throw new UnsupportedOperationException("Utility Class");
    }

    public static RatingDTO assemble(Rating rating) {
        RatingDTO dto = new RatingDTO();
        
        dto.setId(rating.getId());
        dto.setConcernId(rating.getConcern().getId());
        dto.setDecisionId(rating.getDecision().getId());
        dto.setEffect(Effect.valueOf(rating.getEffect().name()));
        
        return dto;
    }

    public static Rating disassemble(RatingDTO dto) {
        Rating rating = new Rating();
        
        rating.setId(dto.getId());
        if (dto.getEffect() == null) {
            rating.setEffect(nl.rug.search.odr.entities.Effect.NEUTRAL);
        } else {
            rating.setEffect(nl.rug.search.odr.entities.Effect.valueOf(dto
                .getEffect().name()));
        }
        
        return rating;
    }
}
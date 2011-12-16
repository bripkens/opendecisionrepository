package nl.rug.search.odr.ws;

import nl.rug.search.odr.ws.dto.DecisionDTO;
import org.reflections.Reflections;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface WebServiceSchema {
    Class<?>[] SCHEMA = new Reflections(DecisionDTO.class.getPackage().getName())
                .getTypesAnnotatedWith(DTO.class)
                .toArray(new Class<?>[]{});;
}

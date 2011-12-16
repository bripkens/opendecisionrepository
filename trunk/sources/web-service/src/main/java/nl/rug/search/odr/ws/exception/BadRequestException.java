package nl.rug.search.odr.ws.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class BadRequestException extends WebApplicationException {

    private static final int HTTP_STATUS_CODE = 400;
    
    public BadRequestException(String message) {
        super(Response
                .status(HTTP_STATUS_CODE)
                .entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build());
    }
    
}

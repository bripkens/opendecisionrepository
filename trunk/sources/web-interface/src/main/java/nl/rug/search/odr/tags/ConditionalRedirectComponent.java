
package nl.rug.search.odr.tags;

import java.io.IOException;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ConditionalRedirectComponent extends UIOutput{
    private String when, url;

    @Override
    public String getFamily() {
        return "redirect";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
    }

    @Override
    public void decode(FacesContext context) {
        // required when using the Sun reference implementation
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        // required when using the Sun reference implementation
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}

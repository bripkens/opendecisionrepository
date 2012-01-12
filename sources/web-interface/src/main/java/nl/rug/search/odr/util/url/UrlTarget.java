
package nl.rug.search.odr.util.url;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UrlTarget implements Target {

    private final String urlFormatString;

    public UrlTarget(String url) {
        this.urlFormatString = url;
    }

    @Override
    public void call(HttpServletRequest request, HttpServletResponse respone,
            List<String> args) {
        String url = String.format(urlFormatString, args.toArray());

        try {
            request.getRequestDispatcher(url).forward(request, respone);
        } catch (ServletException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}

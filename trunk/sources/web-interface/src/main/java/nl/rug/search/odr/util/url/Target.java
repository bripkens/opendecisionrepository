
package nl.rug.search.odr.util.url;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface Target {
    void call(HttpServletRequest request, HttpServletResponse respone,
            List<String> args);
}

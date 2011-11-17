
package nl.rug.search.odr.service;

import java.util.List;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface Result {
    String getName();
    List<Paragraph> getDocumentation();
}

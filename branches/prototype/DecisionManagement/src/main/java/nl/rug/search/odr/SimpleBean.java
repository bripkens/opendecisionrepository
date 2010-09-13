
package nl.rug.search.odr;

import javax.ejb.Stateless;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class SimpleBean implements SimpleBeanLocal {
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public String getText() {
        return "42";
    }
 
}

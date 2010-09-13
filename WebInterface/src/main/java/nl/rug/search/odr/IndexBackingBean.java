
package nl.rug.search.odr;

import javax.ejb.EJB;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */

public class IndexBackingBean {

    @EJB
    private SimpleBeanLocal bean;

    public String getGet() {
        return bean.getText() + " a";
    }

}

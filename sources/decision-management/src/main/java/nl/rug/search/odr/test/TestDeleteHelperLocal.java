
package nl.rug.search.odr.test;

import javax.ejb.Local;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface TestDeleteHelperLocal {

    public void deleteAll();
    
}

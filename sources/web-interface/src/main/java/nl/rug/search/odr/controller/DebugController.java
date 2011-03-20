
package nl.rug.search.odr.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class DebugController {

    public boolean isDebugEnabled() {
        return Boolean.getBoolean("odr.debug");
    }
}

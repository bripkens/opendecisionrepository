package nl.rug.search.odr.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ApplicationScoped
public class BaseController {

    private static final String BASE_HREF = System.getProperty("odr.base.href");
    
    public String getBaseHref() {
        return BASE_HREF;
    }
    
}

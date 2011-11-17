package nl.rug.search.odr.controller;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class ResourceHashController {
    private static final String HASH;
    
    static {
        HASH = Long.toString(new Date().getTime());
    }

    public String getHASH() {
        return HASH;
    }
}

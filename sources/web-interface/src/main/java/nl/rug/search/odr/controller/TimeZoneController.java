
package nl.rug.search.odr.controller;

import java.util.TimeZone;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ApplicationScoped
public class TimeZoneController {
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}

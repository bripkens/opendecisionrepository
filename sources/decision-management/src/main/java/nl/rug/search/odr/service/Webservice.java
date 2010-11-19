package nl.rug.search.odr.service;

import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.service.opr.OprWebservice;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public enum Webservice {

    OPR(OprWebservice.class);

    private final Class<? extends AbstractWebservice> service;




    private Webservice(Class<? extends AbstractWebservice> service) {
        this.service = service;
    }




    public AbstractWebservice getInstance() {
        try {
            return service.newInstance();
        } catch (InstantiationException ex) {
            throw new BusinessException(ex);
        } catch (IllegalAccessException ex) {
            throw new BusinessException(ex);
        }
    }
}

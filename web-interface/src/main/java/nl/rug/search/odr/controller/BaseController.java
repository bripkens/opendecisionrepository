package nl.rug.search.odr.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ApplicationScoped
public class BaseController {

    /**
     * TODO: only do this on application startup
     * @return the baseHREF
     */
    public String getBaseHref() {
        HttpServletRequest request;
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme());
        sb.append("://");
        sb.append(request.getServerName());
        sb.append(":");
        sb.append(request.getServerPort());
        sb.append(request.getContextPath());
        sb.append("/");

        return sb.toString();
    }
    
}

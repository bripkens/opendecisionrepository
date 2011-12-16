package nl.rug.search.odr;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import nl.rug.search.odr.util.url.Binding;
import nl.rug.search.odr.util.url.UrlBindings;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebFilter(filterName = "UrlRewriteFilter",
           urlPatterns = {"/*"})
public class UrlRewriteFilter implements Filter {

    private static final Binding ROOT = UrlBindings.getInstance().getRoot();
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        if (doFilter(httpRequest, httpResponse)) {
            chain.doFilter(request, response);
        }
    }
    
    private boolean doFilter(HttpServletRequest request,
            HttpServletResponse response) {
        String path = request.getRequestURI().substring(request
                .getContextPath().length());
        
        return !ROOT.match(path, request, response);
    }

    @Override
    public void destroy() {
    }
    
}

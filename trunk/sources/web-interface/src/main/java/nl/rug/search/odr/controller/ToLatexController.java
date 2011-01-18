package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class ToLatexController {

    // <editor-fold defaultstate="collapsed" desc="attributes">
    private Project project;

    private ProjectMember member;

    private final String openingBracketPlaceholder = "!\"!{!\"!";

    private final String closingBracketPlaceholder = "!\"!}!\"!";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private ProjectLocal pl;
    // </editor-fold>




    @PostConstruct
    private void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, pl);

        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            this.project = result.getProject();
            this.member = result.getMember();
        } else {
            result.executeErrorAction();
        }
    }




    public ProjectMember getMember() {
        return member;
    }




    public Project getProject() {
        return project;
    }




    public <T> List<T> makeModifiable(Collection<T> original) {
        return new ArrayList<T>(original);
    }




    public boolean isValid() {
        return project != null && member != null;
    }




    public List<Iteration> getOrderedIterations() {
        List<Iteration> iterations = makeModifiable(project.getIterations());
        Collections.sort(iterations, new Iteration.EndDateComparator());

        return iterations;
    }




    public String texEncode(String text) {
        text = text.replace("{", openingBracketPlaceholder);
        text = text.replace("}", closingBracketPlaceholder);

        text = text.replace("\\", "{\\backslash}");
        text = text.replace("#", "{\\#}");
        text = text.replace("$", "{\\$}");
        text = text.replace("%", "{\\%}");
        text = text.replace("&", "{\\&}");
        text = text.replace("~", "{\\~}");
        text = text.replace("_", "{\\_}");
        text = text.replace("^", "{\\^}");

        text = text.replace(openingBracketPlaceholder, "{\\{}");
        text = text.replace(closingBracketPlaceholder, "{\\}}");

        return text;
    }




}




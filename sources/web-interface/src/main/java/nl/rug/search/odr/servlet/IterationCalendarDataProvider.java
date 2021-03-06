package nl.rug.search.odr.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;

/**
 *
 * @author Stefan
 */
@WebServlet(name = "IterationCalendarDataProvider",
            urlPatterns = {"/IterationCalendarDataProvider"})
public class IterationCalendarDataProvider extends HttpServlet {

    @EJB
    private ProjectLocal projectLocal;




    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (!AuthenticationUtil.isAuthenticated(request.getSession())) {
            return;
        }

        long userId = AuthenticationUtil.getUserId(request.getSession());

        long projectId;

        try {
            projectId = Long.parseLong(request.getParameter(RequestParameter.ID));
        } catch (NumberFormatException ex) {
            return;
        }

        Project p = projectLocal.getById(projectId);

        if (p == null) {
            return;
        }

        boolean isMember = false;

        for (ProjectMember pm : p.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                isMember = true;
                break;
            }
        }

        if (!isMember) {
            return;
        }

        List<Iteration> iterations = new ArrayList<Iteration>(p.getIterations());
        Collections.sort(iterations, new Iteration.EndDateComparator());

        List<JSonIteration> allIterations = new ArrayList<JSonIteration>();

        for (Iteration i : iterations) {
            JSonIteration it = new JSonIteration(i.getStartDate(),
                    i.getEndDate(),
                    i.getProjectMember().getPerson().getName(),
                    i.getName(),
                    i.getDocumentedWhen());

            allIterations.add(it);
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        out.print(gson.toJson(allIterations));
        out.close();
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">




    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);


    }




    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);


    }




    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";

    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="JSonIteration inner class">



    public class JSonIteration {

        private Date startDate;

        private Date endDate;

        private String Initiator;

        private String Name;

        private Date creationDate;




        public JSonIteration(Date startDate, Date endDate, String Initiator, String Name, Date creationDate) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.Initiator = Initiator;
            this.Name = Name;
            this.creationDate = creationDate;
        }




        public String getInitiator() {
            return Initiator;
        }




        public void setInitiator(String Initiator) {
            this.Initiator = Initiator;
        }




        public String getName() {
            return Name;
        }




        public void setName(String Name) {
            this.Name = Name;
        }




        public Date getEndDate() {
            return endDate;
        }




        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }




        public Date getStartDate() {
            return startDate;
        }




        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }




        public Date getCreationDate() {
            return creationDate;
        }




        public void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
        }




    }
    // </editor-fold>
}




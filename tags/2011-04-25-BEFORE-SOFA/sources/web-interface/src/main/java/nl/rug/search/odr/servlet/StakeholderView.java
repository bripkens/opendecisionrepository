package nl.rug.search.odr.servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.Pipe;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.viewpoint.GraphvizStakeholderViewBuilder;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "StakeholderView",
            urlPatterns = {"/StakeholderView"})
public class StakeholderView extends HttpServlet {

    @EJB
    private ProjectLocal pl;

    public static final int ERROR_CODE = 303;


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String format = request.getParameter(RequestParameter.FORMAT);
        String filename = request.getParameter(RequestParameter.FILENAME);

        if (format == null || format.isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("Invalid parameters.");
            return;
        }

        if (filename == null) {
            filename = "diagram";
        }

        String contentType;
        String filenameExtension;

        if (format.equalsIgnoreCase(RequestParameter.FORMAT_JPEG)) {
            contentType = "image/jpeg";
            filenameExtension = "jpg";
        } else if (format.equalsIgnoreCase(RequestParameter.FORMAT_PNG)) {
            contentType = "image/png";
            filenameExtension = "png";
        } else if (format.equalsIgnoreCase(RequestParameter.FORMAT_PDF)) {
            contentType = "application/pdf";
            filenameExtension = "pdf";
        } else if (format.equalsIgnoreCase(RequestParameter.FORMAT_SVG)) {
            contentType = "image/svg+xml";
            filenameExtension = "svg";
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("Invalid parameters.");
            return;
        }






        if (!AuthenticationUtil.isAuthenticated(request.getSession())) {
            // TODO inform the user that he is not logged in? This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
            return;
        }

        long userId = AuthenticationUtil.getUserId(request.getSession());

        long projectId, iterationId;

        try {
            projectId = Long.parseLong(request.getParameter(RequestParameter.ID));
            iterationId = Long.parseLong(request.getParameter(RequestParameter.ITERATION_ID));
        } catch (NumberFormatException ex) {
            // TODO change to a more appropriate error value
            response.sendError(ERROR_CODE);
            return;
        }

        Project p = pl.getById(projectId);

        if (p == null) {
            // TODO: Inform the user that the project is not existing? This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
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
            // TODO: Inform the user that he is not a member of the project? This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
            return;
        }




        Iteration it = null;

        for(Iteration eachIteration : p.getIterations()) {
            if (eachIteration.getId().equals(iterationId)) {
                it = eachIteration;
                break;
            }
        }

        if (it == null) {
            // TODO: Inform the user that the iteration is not existing? This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
            return;
        }



        String dot = new GraphvizStakeholderViewBuilder(p, it).getDot();

        System.out.println(dot);


        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + ".".concat(filenameExtension).
                concat("\""));

        response.setContentType(contentType);



        Process process = Runtime.getRuntime().exec("dot -T" + filenameExtension);


        final OutputStream out = process.getOutputStream();
        final InputStream in = process.getInputStream();
        final OutputStream servletOut = response.getOutputStream();



        new Pipe(servletOut, in).start();

        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        writer.write(dot);
        writer.flush();
        writer.close();

        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        servletOut.flush();
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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
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




}




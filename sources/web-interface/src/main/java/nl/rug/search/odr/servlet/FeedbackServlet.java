package nl.rug.search.odr.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "FeedbackServlet",
            urlPatterns = {"/FeedbackServlet"})
public class FeedbackServlet extends HttpServlet {


    private static final String PROPERTIES_FILE = "nl.rug.search.odr.mail";

//    public static final String SMTP_HOST = "smtp.1und1.de";
//
//    public static final String SMTP_USER = "info@decisionrepository.com";
//
//    public static final String SMTP_PASSWORD = "ludupo17";
//
//    public static final String SMTP_PORT = "25";
//
//    public static final String FROM = "info@decisionrepository.com";
//
//    public static final String TO = /*"uwe@vanheesch.net", */ "bripkens.dev@gmail.com"/*, "stefan.arians@gmail.com"*/;
//
//    public static final String SUBJECT = "[ODR] Feedback";
//
//    public static final String MESSAGE = "From: %s \n\nUrl: %s \n\nMessage: %s \n\nPage: %s";




    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userEmail = request.getParameter("feedbackInEmail");
        String message = request.getParameter("feedbackInMessage");
        String url = request.getParameter("feedbackInUrl");
        String page = request.getParameter("feedbackInPage");

        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE, request.getLocale());

        message = String.format(bundle.getString("feedback.message"), userEmail, url, message, page);

        Properties props = new Properties();
        props.put("mail.smtp.host", bundle.getString("feedback.smtp.host"));
        props.put("mail.smtp.port", bundle.getString("feedback.smtp.port"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.submitter", bundle.getString("feedback.smtp.user"));

        Session session = Session.getInstance(props, new Authenticator(bundle.getString("feedback.smtp.user"),
                bundle.getString("feedback.smtp.password")));

        Message email = new MimeMessage(session);
        try {
            email.setFrom(new InternetAddress(bundle.getString("feedback.from")));

            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bundle.getString("feedback.to"), false));

            email.setSubject(bundle.getString("feedback.subject"));

            email.setText(message);

            Transport.send(email);
        } catch (AddressException ex) {
            throw new RuntimeException(ex);
        } catch (MessagingException ex) {
            Logger.getLogger(FeedbackServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            response.sendRedirect(url);
        }
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




    private class Authenticator extends javax.mail.Authenticator {

        private PasswordAuthentication authentication;




        public Authenticator(String user, String password) {
            authentication = new PasswordAuthentication(user, password);
        }




        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }




    }




}




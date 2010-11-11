package nl.rug.search.odr.servlet;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.RequestParameter;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.fop.svg.PDFTranscoder;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "transcoder",
            urlPatterns = {"/diagram.png", "/diagram.jpg", "/diagram.pdf", "/diagram.svg"})
public class TranscoderServlet extends HttpServlet {



    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String data = request.getParameter(RequestParameter.DATA);
        String format = request.getParameter(RequestParameter.FORMAT);

        if (data == null || data.isEmpty() || format == null || format.isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("Invalid parameters.");
            return;
        }

        Transcoder t;
        String contentType;
        String filenameExtension;

        if (format.equalsIgnoreCase(RequestParameter.FORMAT_JPEG)) {
            t = new JPEGTranscoder();
            contentType = "image/jpeg";
            filenameExtension = "jpg";
            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                             new Float(.8));
            t.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.white);
        } else if (format.equalsIgnoreCase(RequestParameter.FORMAT_PNG)) {
            t = new PNGTranscoder();
            contentType = "image/png";
            filenameExtension = "png";
        } else if (format.equalsIgnoreCase(RequestParameter.FORMAT_PDF)) {
            t = new PDFTranscoder();
            contentType = "application/pdf";
            filenameExtension = "pdf";
        } else if (format.equalsIgnoreCase(RequestParameter.FORMAT_SVG)) {
            t = new SVGTranscoder();
            contentType = "image/svg+xml";
            filenameExtension = "svg";
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("Invalid parameters.");
            return;
        }


        response.setHeader("Content-Disposition", "attachment;filename=\"diagram.".
                concat(filenameExtension).
                concat("\""));

        response.setContentType(contentType);

        if (filenameExtension.equals("svg")) {
            PrintWriter out = response.getWriter();
            out.println(data);
            out.flush();
            out.close();
            return;
        }

        OutputStream stream = response.getOutputStream();

        TranscoderInput input = new TranscoderInput(new StringReader(data));

        TranscoderOutput output = new TranscoderOutput(stream);
        try {
            t.transcode(input, output);
        } catch (TranscoderException ex) {
            System.out.println("###################");
            ex.printStackTrace();
            throw new ServletException(ex.getMessage(), ex);

        }

        stream.flush();
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

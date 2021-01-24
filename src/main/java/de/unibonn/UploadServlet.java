package de.unibonn;

import de.unibonn.model.OntologyClass;
import org.apache.commons.io.IOUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        OntologyProcessor ontologyProcessor = new OntologyProcessor();
        RDFConnector rdfConnectorQuery = new RDFConnector("isa_rdf_triples", "query");
        String fileName = "";
        String ontology_url = request.getParameter("ontology_url");

        InputStream fileContent = null;
        if(ontology_url==null || ontology_url.equals("")) {
            Part filePart = request.getPart("file");
            fileName = extractFileName(filePart);
            System.out.println("FILENAME : " + fileName);
            fileContent = filePart.getInputStream();
        } else {
            fileContent = new URL(ontology_url).openStream();
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(fileContent, baos);
            byte[] bytes = baos.toByteArray();
            fileContent = new ByteArrayInputStream(bytes);
            fileContent.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<OntologyClass> ontologyClasses = new ArrayList<>();
        ontologyProcessor.setClasses(session, rdfConnectorQuery, fileContent, ontology_url, ontologyClasses, fileName);
        ontologyProcessor.setIndividuals(rdfConnectorQuery.getAllTriples(), ontologyClasses);
        fileContent.reset();
        ontologyProcessor.setQualifiedCardinalityRestrictions(ontologyClasses, fileContent);

        session.setAttribute("ontologyClasses", ontologyClasses);

        Collections.sort(ontologyClasses);
        request.setAttribute("ontologyClasses", ontologyClasses);
        RequestDispatcher view = request.getRequestDispatcher("webform.jsp");
        view.forward(request, response);
    }
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}
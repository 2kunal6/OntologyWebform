package de.unibonn;

import de.unibonn.model.OntologyClass;
import org.apache.jena.graph.Triple;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        OntologyProcessor ontologyProcessor = new OntologyProcessor();
        RDFConnector rdfConnectorQuery = new RDFConnector("isa_rdf_triples", "query");

        String ontology_url = request.getParameter("ontology_url");

        InputStream fileContent = null;
        if(ontology_url==null || ontology_url.equals("")) {
            Part filePart = request.getPart("file");
            fileContent = filePart.getInputStream();
        }

        List<OntologyClass> ontologyClasses = new ArrayList<>();
        ontologyProcessor.setClasses(fileContent, ontology_url, ontologyClasses);

        //Map<OntClass, List<Restriction>> classRestrictions = ontologyProcessor.getClassRestrictions(classes);

        List<Triple> allTriples = rdfConnectorQuery.getAllTriples();

        ontologyProcessor.setPredicates(allTriples, ontologyClasses);

        session.setAttribute("ontologyClasses", ontologyClasses);

        request.setAttribute("ontologyClasses", ontologyClasses);
        RequestDispatcher view = request.getRequestDispatcher("webform.jsp");
        view.forward(request, response);
    }
}
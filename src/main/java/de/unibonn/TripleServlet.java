package de.unibonn;

import de.unibonn.model.OntologyClass;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/triple")
@MultipartConfig
public class TripleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        OntologyProcessor ontologyProcessor = new OntologyProcessor();
        RDFConnector rdfConnector = new RDFConnector("isa_rdf_triples", "update");
        RDFConnector rdfConnectorQuery = new RDFConnector("isa_rdf_triples", "query");

        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if(entry.getValue().length!=0 && !entry.getValue()[0].equals("")) {
                rdfConnector.insertTriple(entry.getKey() + "/" + entry.getValue()[0], "rdf:type", entry.getKey());
            }
        }

        List<OntologyClass> ontologyClasses = (List<OntologyClass>) session.getAttribute("ontologyClasses");
        ontologyProcessor.setIndividuals(rdfConnectorQuery.getAllTriples(), ontologyClasses);
        ontologyProcessor.setRestrictions(ontologyClasses);

        request.setAttribute("ontologyClasses", ontologyClasses);
        RequestDispatcher view = request.getRequestDispatcher("triple.jsp");
        view.forward(request, response);

    }
}
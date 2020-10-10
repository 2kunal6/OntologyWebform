package de.unibonn;

import de.unibonn.model.OntologyClass;
import org.apache.commons.io.IOUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.Restriction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        RDFConnector rc = new RDFConnector("test", "query");
        Set<OntClass> classes = rc.getClasses(fileContent, ontology_url);

        Map<OntClass, List<Restriction>> classRestrictions = ontologyProcessor.getClassRestrictions(classes);

        List<OntologyClass> ontologyClasses = new ArrayList<>();
        for(OntClass ontClass : classes) {
            OntologyClass ontologyClass = new OntologyClass();
            ontologyClass.setOntclass(ontClass);
            ontologyClasses.add(ontologyClass);
        }
        List<Triple> allTriples = rdfConnectorQuery.getAllTriples();

        ontologyProcessor.setPredicates(allTriples, ontologyClasses);

        session.setAttribute("classes", classes);

        List<String> classList = classes.stream().map(s -> s.toString()).collect(Collectors.toList());

        request.setAttribute("classList", classList);
        request.setAttribute("ontologyClasses", ontologyClasses);
        RequestDispatcher view = request.getRequestDispatcher("webform.jsp");
        view.forward(request, response);
    }
}
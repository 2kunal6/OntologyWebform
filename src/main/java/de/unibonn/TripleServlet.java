package de.unibonn;

import de.unibonn.model.OntologyClass;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Restriction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/triple")
@MultipartConfig
public class TripleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        OntologyProcessor ontologyProcessor = new OntologyProcessor();
        RDFConnector rdfConnector = new RDFConnector("isa_rdf_triples", "update");
        RDFConnector rdfConnectorQuery = new RDFConnector("isa_rdf_triples", "query");


        System.out.println("INSIDE Triple servlet");
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if(entry.getValue().length!=0 && !entry.getValue()[0].equals("")) {
                rdfConnector.insertTriple(entry.getKey(), "rdf:type", entry.getValue()[0]);
            }
        }

        Set<OntClass> classes = (Set<OntClass>) session.getAttribute("classes");

        Map<String, List<String>> classPropertiesAsString = new HashMap<>();
        Map<OntClass, List<OntProperty>> classAndProperties = ontologyProcessor.getClassAndProperties(classes);

        for (Map.Entry<OntClass, List<OntProperty>> pair : classAndProperties.entrySet()) {
            List<String> propertiesAsString = new ArrayList<>();
            for(OntProperty ontProperty : pair.getValue())propertiesAsString.add(ontProperty.toString());
            classPropertiesAsString.put(pair.getKey().toString(), propertiesAsString);
        }

        request.setAttribute("classPropertiesAsString", classPropertiesAsString);
        RequestDispatcher view = request.getRequestDispatcher("triple.jsp");
        view.forward(request, response);

    }
}
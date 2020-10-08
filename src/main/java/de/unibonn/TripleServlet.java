package de.unibonn;

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

        System.out.println("INSIDE Triple servlet");
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().toString());
        }
        Set<OntClass> classes = (Set<OntClass>) session.getAttribute("classes");
        Map<OntClass, List<Restriction>> classRestrictions = ontologyProcessor.getClassRestrictions(classes);

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
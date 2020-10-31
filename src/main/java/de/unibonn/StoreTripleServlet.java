package de.unibonn;

import de.unibonn.model.OntologyClass;
import de.unibonn.service.TripleValidator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet("/storeTriple")
@MultipartConfig
public class StoreTripleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("STORE TRIPLE*************************************************************************************************************");
        HttpSession session = request.getSession();
        RDFConnector rdfConnector = new RDFConnector("isa_rdf_triples", "update");
        TripleValidator tripleValidator = new TripleValidator();

        List<OntologyClass> ontologyClasses = (List<OntologyClass>) session.getAttribute("ontologyClasses");
        Map<String, String[]> params = request. getParameterMap();

        String validation = tripleValidator.validate(ontologyClasses, params);
        if(validation.length()>0) {
            request.setAttribute("validation", validation+"\n\n\n");
            request.setAttribute("ontologyClasses", ontologyClasses);
            RequestDispatcher view = request.getRequestDispatcher("triple.jsp");
            view.forward(request, response);
        }

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if(entry.getValue().length!=0 && !entry.getValue()[0].equals("")) {
                String[] keySplit = entry.getKey().split("_XXX_CLASS_PROPERTY_SEPARATOR_XXX_");
                for(String ind : Arrays.asList((entry.getValue()[0]).split(","))) {
                    rdfConnector.insertTriple(keySplit[0], keySplit[1], ind);
                }
            }
        }

        request.setAttribute("validation", "SUCCESS SUCCESS SUCCESS\n\n\n");
        request.setAttribute("ontologyClasses", ontologyClasses);
        RequestDispatcher view = request.getRequestDispatcher("triple.jsp");
        view.forward(request, response);

    }
}
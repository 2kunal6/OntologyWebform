package de.unibonn;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Restriction;

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

        Map<String, String[]> params = request. getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue(). toString());
        }

        //request.setAttribute("classPropertiesAsString", classPropertiesAsString);
        //RequestDispatcher view = request.getRequestDispatcher("triple.jsp");
        //view.forward(request, response);

    }
}
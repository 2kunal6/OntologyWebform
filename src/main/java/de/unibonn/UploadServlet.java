package de.unibonn;

import org.apache.commons.io.IOUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.OntClass;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String description = request.getParameter("description");
        String ontology_url = request.getParameter("ontology_url");

        InputStream fileContent = null;
        if(ontology_url==null || ontology_url.equals("")) {
            Part filePart = request.getPart("file");
            //String fileName = Paths.get(filePart.getName()).getFileName().toString();
            fileContent = filePart.getInputStream();
            //System.out.println(IOUtils.toString(fileContent));
        }

        RDFConnector rc = new RDFConnector("test", "query");
        //Set<Node> classes = rc.getFusekiClasses();
        //Set<Node> classes = rc.getClasses(fileContent);
        Set<OntClass> classes = rc.getClasses(fileContent, ontology_url);
        session.setAttribute("classes", classes);

        List<String> classList = classes.stream().map(s -> s.toString()).collect(Collectors.toList());
        //List<String> classList = new ArrayList<String>();

        request.setAttribute("classList", classList);
        RequestDispatcher view = request.getRequestDispatcher("webform.jsp");
        view.forward(request, response);
    }
}
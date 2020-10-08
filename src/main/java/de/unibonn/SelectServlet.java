package de.unibonn;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(
        name = "selectservlet",
        urlPatterns = "/Select"
)
public class SelectServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String type = req.getParameter("Type");

        Service service = new Service();

        String retValue = service.getValues(type);

        req.setAttribute("retVal", retValue);
        RequestDispatcher view = req.getRequestDispatcher("result.jsp");
        view.forward(req, resp);

        //RDFConnector rc = new RDFConnector("test");
        //rc.query();
        //rc.insertData();
        //rc.connectFuseki();
        //rc.getFusekiClasses();

        RDFConnector irc = new RDFConnector("isa_rdf_triples", "update");
        //irc.insertTriple();
    }
}
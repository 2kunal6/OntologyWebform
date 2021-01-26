package de.unibonn.util;

import org.apache.jena.rdf.model.Statement;

import java.util.List;

public class OntologyStatementUtil {
    public Integer getPos(List<Statement> stmtList) {
        String base_uri = stmtList.get(0).getSubject().getURI().split("#")[0];
        for(Statement stmt : stmtList) {
            if(stmt.getPredicate().toString().equals(base_uri + "#view_position")) {
                return stmt.getObject().asLiteral().getInt();
            }
        }
        return null;
    }
}

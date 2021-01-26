package de.unibonn.util;

import org.apache.jena.ontology.OntProperty;

import java.util.Comparator;

public class OntPropertyComparator implements Comparator<OntProperty> {
    public int compare(OntProperty a, OntProperty b) {
        OntologyStatementUtil ontologyStatementUtil = new OntologyStatementUtil();
        Integer aPos = ontologyStatementUtil.getPos(a.listProperties().toList());
        Integer bPos = ontologyStatementUtil.getPos(b.listProperties().toList());

        if(bPos == null)return -1;
        if(aPos == null)return 1;
        if(aPos < bPos)return -1;
        return 1;
    }
}

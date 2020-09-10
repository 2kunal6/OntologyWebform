package de.unibonn;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

public class ReadOntology {

    void loadModel() {
        OntModel model = ModelFactory.createOntologyModel();
        Model mod = model.read("https://raw.githubusercontent.com/ISA-tools/linkedISA-ontologies/master/isaterms.owl");
        ResIterator it = mod.listSubjects();
        while (it.hasNext()) {
            Resource r = it.nextResource();
            System.out.println(r.toString());
        }
    }

}
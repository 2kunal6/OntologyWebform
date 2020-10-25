/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unibonn;

import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;
import java.util.*;

/** RDF Connection example */
public class RDFConnector {

    RDFConnectionFuseki conn;

    RDFConnector(String dataset, String sparql_endpoint) {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://localhost:3030/" + dataset + "/" + sparql_endpoint);
        conn = (RDFConnectionFuseki)builder.build();
    }

    String prefixes = "";

    List<Triple> getAllTriples() {

        System.out.println("Java connecting to FUSEKI*************************************");
        Query query = QueryFactory.create(prefixes + "SELECT ?subject ?predicate ?object WHERE { ?subject ?predicate ?object }");

        List<Triple> triples = new ArrayList<>();
        conn.queryResultSet(query, rs->{
            List<QuerySolution> list = Iter.toList(rs);
            for(QuerySolution qs : list) {
                triples.add(new Triple(qs.get("subject").asNode(), qs.get("predicate").asNode(), qs.get("object").asNode()));
            }
        });

        return triples;

    }

    Set<OntClass> getClasses(InputStream fileContent, String fileName, String ontology_url) {
        OntModel model = ModelFactory.createOntologyModel();
        if(ontology_url==null || ontology_url.equals("")) {
            if(fileName.toLowerCase().endsWith(".ttl"))model.read(fileContent, null, "TTL");
            else if(fileName.toLowerCase().endsWith(".rdf") || fileName.toLowerCase().endsWith(".xml"))model.read(fileContent,null,"RDF/XML");
            else if(fileName.toLowerCase().endsWith(".n3"))model.read(fileContent,null,"n3");
            else model.read(fileContent, "");
        }
        else {
            if(ontology_url.toLowerCase().endsWith(".ttl"))model.read(ontology_url,null,"TTL");
            else if(ontology_url.toLowerCase().endsWith(".rdf") || ontology_url.toLowerCase().endsWith(".xml"))model.read(ontology_url,null,"RDF/XML");
            else if(ontology_url.toLowerCase().endsWith(".n3"))model.read(ontology_url,null,"n3");
            else model.read(ontology_url);
        }

        ExtendedIterator<OntClass> iter = model.listNamedClasses();

        Set<OntClass> classSet = new HashSet<>();
        while ( iter.hasNext()){
            classSet.add(iter.next());
        }

        for (Map.Entry<String, String> entry : model.getNsPrefixMap().entrySet()) {
            prefixes+=("prefix " + entry.getKey() + ": <" + entry.getValue() + "> \n");
        }
        System.out.println(prefixes);

        return classSet;
    }

    void insertTriple(String subject, String predicate, String object) {
        System.out.println("Inserting triple to FUSEKI*************************************");
        if(object.startsWith("http://")) {
            object = "<" + object + ">";
        } else {
            object = "'" + object + "'";
        }
        String query = prefixes + "INSERT DATA { <" + subject + "> <" + predicate + "> " + object + " . }";
        System.out.println(query);
        conn.update(query);

        System.out.println("Insert to FUSEKI Finished *************************************");
    }
}


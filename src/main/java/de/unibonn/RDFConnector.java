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
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** RDF Connection example */
public class RDFConnector {

    RDFConnectionFuseki conn;

    RDFConnector(String dataset, String sparql_endpoint) {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create().destination("http://localhost:3030/" + dataset + "/" + sparql_endpoint);
        conn = (RDFConnectionFuseki)builder.build();
    }

    String prefixes = "prefix :      <http://www.isa-tools.org/owl/isa.owl#> \n" +
            "prefix isaterms: <http://purl.org/isaterms/> \n" +
            "prefix terms: <http://purl.org/dc/terms/> \n" +
            "prefix owl:   <http://www.w3.org/2002/07/owl#> \n" +
            "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "prefix xsd:   <http://www.w3.org/2001/XMLSchema#> \n" +
            "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> ";

    void connectFuseki() {

        System.out.println("Java connecting to FUSEKI*************************************");
        Query query = QueryFactory.create(prefixes + "SELECT ?subject ?predicate ?object\n" +
                "WHERE {\n" +
                "  ?subject ?predicate ?object\n" +
                "}");

        // In this variation, a connection is built each time.

        conn.queryResultSet(query, ResultSetFormatter::out);
        conn.update("INSERT DATA { <http://example.org/data/transaction/999> a <http://example.org/ont/transaction-log/zzz> . }");

        System.out.println("Java connecting to FUSEKI Finished *************************************");
    }

    Set<Node> getFusekiClasses() {

        System.out.println("Java connecting to FUSEKI*************************************");
        Query query = QueryFactory.create(prefixes + "SELECT DISTINCT ?class ?label ?description\n" +
                "WHERE {\n" +
                "  ?class a owl:Class.\n" +
                "  OPTIONAL { ?class rdfs:label ?label}\n" +
                "  OPTIONAL { ?class rdfs:comment ?description}\n" +
                "}");

        // In this variation, a connection is built each time.

        Set<Node> results = new HashSet<>();
        conn.queryResultSet(query, rs->{
            List<QuerySolution> list = Iter.toList(rs);
            list.stream()
                    .map(qs->qs.get("class"))
                    .filter(Objects::nonNull)
                    .map(RDFNode::asNode)
                    .forEach(n->results.add(n));
        });

        //for(Node n : results)System.out.println("THIS : " + n.toString());
        return results;

        //System.out.println("Java connecting to FUSEKI Finished *************************************");
    }

    Set<OntClass> getClasses(InputStream fileContent, String ontology_url) {
        OntModel model = ModelFactory.createOntologyModel();
        if(ontology_url==null || ontology_url.equals(""))model.read(fileContent, "");
        else model.read(ontology_url);

        ExtendedIterator<OntClass> iter = model.listClasses();

        Set<OntClass> classSet = new HashSet<>();
        while ( iter.hasNext()){
            classSet.add(iter.next());
        }
        return classSet;
    }

    void insertTriple(String subject, String predicate, String object) {
        System.out.println("Inserting triple to FUSEKI*************************************");
        String query = prefixes + "INSERT DATA { " + subject + " " + predicate + " " + object + " . }";
        conn.update(query);

        System.out.println("Insert to FUSEKI Finished *************************************");
    }
}


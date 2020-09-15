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
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.system.Txn;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** RDF Connection example */
public class RDFConnector {
    void query() {
        Query query = QueryFactory.create("SELECT * { {?s ?p ?o } UNION { GRAPH ?g { ?s ?p ?o } } }");
        Dataset dataset = DatasetFactory.createTxnMem();
        RDFConnection conn = RDFConnectionFactory.connect(dataset);

        Txn.executeWrite(conn, () ->{
            System.out.println("Load a file");
            conn.load("data.ttl");
            conn.load("http://example/g0", "data.ttl");
            System.out.println("In write transaction");
            conn.queryResultSet(query, ResultSetFormatter::out);
        });
        // And again - implicit READ transaction.
        System.out.println("After write transaction");
        conn.queryResultSet(query, ResultSetFormatter::out);
    }

    void insertData() {
        Dataset dataset = DatasetFactory.createTxnMem();
        RDFConnection conn = RDFConnectionFactory.connect(dataset);
        Txn.executeWrite(conn, () ->{
            System.out.println("Load a file");
            conn.load("data.ttl");
            conn.load("http://example/g0", "data.ttl");
            System.out.println("In write transaction");
            conn.update("INSERT DATA { <http://example.org/data/transaction/999> a <http://example.org/ont/transaction-log/zzz> . }");
        });
        conn.put("data.ttl");
        conn.close();
        System.out.println("After write transaction");
    }

    void connectFuseki() {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create()
                .destination("http://localhost:3030/test/query");

        System.out.println("Java connecting to FUSEKI*************************************");
        Query query = QueryFactory.create("prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "\n" +
                "SELECT ?subject ?predicate ?object\n" +
                "WHERE {\n" +
                "  ?subject ?predicate ?object\n" +
                "}");

        // In this variation, a connection is built each time.
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query, ResultSetFormatter::out);
            conn.update("INSERT DATA { <http://example.org/data/transaction/999> a <http://example.org/ont/transaction-log/zzz> . }");
        }

        System.out.println("Java connecting to FUSEKI Finished *************************************");
    }

    Set<Node> getFusekiClasses() {
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create()
                .destination("http://localhost:3030/test/query");

        System.out.println("Java connecting to FUSEKI*************************************");
        Query query = QueryFactory.create("prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT ?class ?label ?description\n" +
                "WHERE {\n" +
                "  ?class a owl:Class.\n" +
                "  OPTIONAL { ?class rdfs:label ?label}\n" +
                "  OPTIONAL { ?class rdfs:comment ?description}\n" +
                "}");

        // In this variation, a connection is built each time.
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
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
        }

        //System.out.println("Java connecting to FUSEKI Finished *************************************");
    }
}


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

import de.unibonn.model.OntologyClass;
import de.unibonn.model.OntologyClassRestriction;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.Restriction;

import java.io.InputStream;
import java.util.*;

public class OntologyProcessor {

    void setClassAndProperties(List<OntologyClass> classes) {
        for(OntologyClass ontologyClass : classes) {
            ontologyClass.getProperties().addAll(ontologyClass.getOntclass().listDeclaredProperties().toList());
        }
    }

    void setIndividuals(List<Triple> allTriples, List<OntologyClass> ontologyClasses) {
        for(int i=0;i<allTriples.size();i++) {
            Triple triple = allTriples.get(i);
            for(int j=0;j<ontologyClasses.size();j++) {
                OntologyClass ontologyClass = ontologyClasses.get(j);
                if(ontologyClass.getOntclass().toString().equals(triple.getObject().toString())) {
                    if(triple.getPredicate().toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") || triple.getPredicate().toString().equals("rdf:type")) {
                        ontologyClass.getIndividuals().add(triple.getSubject().toString());
                    }
                    break;
                }
            }
        }
    }

    void setClasses(InputStream fileContent, String ontology_url, List<OntologyClass> ontologyClasses, String fileName) {
        RDFConnector rc = new RDFConnector("test", "query");
        Set<OntClass> classes = rc.getClasses(fileContent, fileName, ontology_url);
        for(OntClass ontClass : classes) {
            OntologyClass ontologyClass = new OntologyClass();
            ontologyClass.setOntclass(ontClass);
            ontologyClasses.add(ontologyClass);
        }
    }

    void setRestrictions(List<OntologyClass> ontologyClasses) {
        for(OntologyClass ontologyClass : ontologyClasses) {
            OntClass ontClass = ontologyClass.getOntclass();
            Iterator<OntClass> superIter = ontClass.listSuperClasses();
            while(superIter.hasNext()) {
                OntClass superOntClass = superIter.next();
                if(superOntClass.isRestriction()) {
                    OntologyClassRestriction ontologyClassRestriction = new OntologyClassRestriction();
                    ontologyClassRestriction.setRestriction(superOntClass.asRestriction());
                    ontologyClass.getRestrictions().add(ontologyClassRestriction);
                }
            }
            if(ontologyClass.getRestrictions().size()>0) {
                for(OntologyClassRestriction ontologyClassRestriction : ontologyClass.getRestrictions()) {
                    Restriction restriction = ontologyClassRestriction.getRestriction();
                    if(restriction.isSomeValuesFromRestriction()) {
                        for(OntologyClass restrictedOntologyClass : ontologyClasses) {
                            if(restrictedOntologyClass.getOntclass().toString().equals(restriction.asSomeValuesFromRestriction().getSomeValuesFrom().toString())) {
                                ontologyClassRestriction.setOntProperty(restriction.asSomeValuesFromRestriction().getOnProperty());
                                ontologyClassRestriction.setIndividuals(new ArrayList<String>(restrictedOntologyClass.getIndividuals()));
                                ontologyClassRestriction.setDescription("AT LEAST ONE value of the command separated values must be from dropdown (Create individuals to see dropdown)");
                            }
                        }
                    } else if(restriction.isAllValuesFromRestriction()) {
                        for(OntologyClass restrictedOntologyClass : ontologyClasses) {
                            if(restrictedOntologyClass.getOntclass().toString().equals(restriction.asAllValuesFromRestriction().getAllValuesFrom().toString())) {
                                ontologyClassRestriction.setOntProperty(restriction.asAllValuesFromRestriction().getOnProperty());
                                ontologyClassRestriction.setIndividuals(new ArrayList<String>(restrictedOntologyClass.getIndividuals()));
                                ontologyClassRestriction.setDescription("ALL value of the command separated values must be from dropdown (Create individuals to see dropdown)");       }
                        }
                    }
                }
            }
        }
    }
}
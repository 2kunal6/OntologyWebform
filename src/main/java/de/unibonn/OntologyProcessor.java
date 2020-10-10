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
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Restriction;

import java.util.*;

public class OntologyProcessor {
    Map<OntClass, List<Restriction>> getClassRestrictions(Set<OntClass> classes) {
        Map<OntClass, List<Restriction>> classRestrictions = new HashMap<>();
        Iterator<OntClass> iter = classes.iterator();
        while (iter.hasNext()) {
            OntClass ontClass = iter.next();
            Iterator<OntClass> superIter = ontClass.listSuperClasses();
            List<Restriction> restrictions = new ArrayList<>();
            while(superIter.hasNext()) {
                OntClass superOntClass = superIter.next();
                if(superOntClass.isRestriction()) {
                    restrictions.add(superOntClass.asRestriction());
                }
            }
            classRestrictions.put(ontClass, restrictions);
        }
        return classRestrictions;
    }

    Map<OntClass, List<OntProperty>> getClassAndProperties(Set<OntClass> classes) {
        Map<OntClass, List<OntProperty>> classAndProperties = new HashMap<>();
        Iterator<OntClass> iter = classes.iterator();
        while (iter.hasNext()) {
            OntClass ontClass = iter.next();
            classAndProperties.put(ontClass, ontClass.listDeclaredProperties().toList());
        }
        return classAndProperties;
    }

    void setPredicates(List<Triple> allTriples, List<OntologyClass> ontologyClasses) {
        for(int i=0;i<allTriples.size();i++) {
            Triple triple = allTriples.get(i);
            for(int j=0;j<ontologyClasses.size();j++) {
                OntologyClass ontologyClass = ontologyClasses.get(j);
                if(ontologyClass.getOntclass().toString().equals(triple.getSubject().toString())) {
                    ontologyClass.getTriples().add(triple);
                    if(triple.getPredicate().toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") || triple.getPredicate().toString().equals("rdf:type")) {
                        ontologyClass.getIndividuals().add(triple.getObject().toString());
                    }
                    break;
                }
            }
        }
    }
}


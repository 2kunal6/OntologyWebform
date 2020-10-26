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
import de.unibonn.model.QualifiedCardinalityRestriction;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.Restriction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

    void setClasses(RDFConnector rc, InputStream fileContent, String ontology_url, List<OntologyClass> ontologyClasses, String fileName) {
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
                        setSomeValuesFromRestriction(ontologyClasses, ontologyClassRestriction, restriction);
                    } else if(restriction.isAllValuesFromRestriction()) {
                        setAllValuesFromRestriction(ontologyClasses, ontologyClassRestriction, restriction);
                    } else if(restriction.isCardinalityRestriction()) {
                        setCardinalityRestriction(ontologyClassRestriction, restriction);
                    } else if(restriction.isMinCardinalityRestriction()) {
                        setMinCardinalityRestriction(ontologyClassRestriction, restriction);
                    } else if(restriction.isMaxCardinalityRestriction()) {
                        setMaxCardinalityRestriction(ontologyClassRestriction, restriction);
                    }
                }
            }
        }
    }
    void setSomeValuesFromRestriction(List<OntologyClass> ontologyClasses, OntologyClassRestriction ontologyClassRestriction, Restriction restriction) {
        for(OntologyClass restrictedOntologyClass : ontologyClasses) {
            if(restrictedOntologyClass.getOntclass().toString().equals(restriction.asSomeValuesFromRestriction().getSomeValuesFrom().toString())) {
                ontologyClassRestriction.setOntProperty(restriction.asSomeValuesFromRestriction().getOnProperty());
                ontologyClassRestriction.setIndividuals(new ArrayList<String>(restrictedOntologyClass.getIndividuals()));
                ontologyClassRestriction.setDescription("AT LEAST ONE value of the command separated values must be from dropdown (Create individuals to see dropdown)");
            }
        }
    }

    void setAllValuesFromRestriction(List<OntologyClass> ontologyClasses, OntologyClassRestriction ontologyClassRestriction, Restriction restriction) {
        for(OntologyClass restrictedOntologyClass : ontologyClasses) {
            if(restrictedOntologyClass.getOntclass().toString().equals(restriction.asAllValuesFromRestriction().getAllValuesFrom().toString())) {
                ontologyClassRestriction.setOntProperty(restriction.asAllValuesFromRestriction().getOnProperty());
                ontologyClassRestriction.setIndividuals(new ArrayList<String>(restrictedOntologyClass.getIndividuals()));
                ontologyClassRestriction.setDescription("ALL value of the command separated values must be from dropdown (Create individuals to see dropdown)");       }
        }
    }
    void setCardinalityRestriction(OntologyClassRestriction ontologyClassRestriction, Restriction restriction) {
       ontologyClassRestriction.setOntProperty(restriction.asCardinalityRestriction().getOnProperty());
       ontologyClassRestriction.setCardinality(restriction.asCardinalityRestriction().getCardinality());
       ontologyClassRestriction.setDescription("EXACTLY " + restriction.asCardinalityRestriction().getCardinality() + " value(s) required here.");
    }
    void setMinCardinalityRestriction(OntologyClassRestriction ontologyClassRestriction, Restriction restriction) {
        ontologyClassRestriction.setOntProperty(restriction.asMinCardinalityRestriction().getOnProperty());
        ontologyClassRestriction.setMinCardinality(restriction.asMinCardinalityRestriction().getMinCardinality());
        ontologyClassRestriction.setDescription("AT LEAST " + restriction.asMinCardinalityRestriction().getMinCardinality() + " value(s) required here.");
    }
    void setMaxCardinalityRestriction(OntologyClassRestriction ontologyClassRestriction, Restriction restriction) {
        ontologyClassRestriction.setOntProperty(restriction.asMaxCardinalityRestriction().getOnProperty());
        ontologyClassRestriction.setMaxCardinality(restriction.asMaxCardinalityRestriction().getMaxCardinality());
        ontologyClassRestriction.setDescription("AT MOST " + restriction.asMaxCardinalityRestriction().getMaxCardinality() + " value(s) required here.");
    }

    void setQualifiedCardinalityRestrictions(List<OntologyClass> ontologyClasses, InputStream fileContent) {
        String fileString = "";
        try {
            fileString = IOUtils.toString(fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> lines = Arrays.asList(fileString.split("\n"));
        int i=0,newsearch=0;
        for(i=0;i<lines.size();i++) {
            //System.out.println(lines.get(i));
            if(lines.get(i).contains("owl:qualifiedCardinality")) {
                QualifiedCardinalityRestriction qualifiedCardinalityRestriction = new QualifiedCardinalityRestriction();
                int exactCount = Integer.parseInt(StringUtils.substringBetween(lines.get(i), "\">", "</owl:qualifiedCardinality>"));
                qualifiedCardinalityRestriction.setExact(exactCount);
                newsearch=i;
                setCurrentRestriction(qualifiedCardinalityRestriction, ontologyClasses, i, lines);
                i=newsearch;
            }
        }
    }
    void setCurrentRestriction(QualifiedCardinalityRestriction qualifiedCardinalityRestriction, List<OntologyClass> ontologyClasses, int lineNum, List<String> lines) {
        while(!lines.get(lineNum).contains("<owl:onClass"))lineNum--;
        String onClass=StringUtils.substringBetween(lines.get(lineNum), "<owl:onClass", "\"/>");
        while(!lines.get(lineNum).contains("<owl:Class "))lineNum--;
        String owlClass=StringUtils.substringBetween(lines.get(lineNum), "<owl:Class ", "\">");
        System.out.println(onClass + " " + owlClass);
    }
}
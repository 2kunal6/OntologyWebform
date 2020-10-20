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
package de.unibonn.service;

import de.unibonn.model.OntologyClass;
import de.unibonn.model.OntologyClassRestriction;
import org.apache.jena.ontology.Restriction;

import java.util.*;

public class TripleValidator {
    public String validate(List<OntologyClass> ontologyClasses, Map<String, String[]> params) {
        String validation="";
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if(entry.getValue().length!=0 && !entry.getValue()[0].equals("")) {
                String[] keySplit = entry.getKey().split("_XXX_CLASS_PROPERTY_SEPARATOR_XXX_");
                List<String> individuals = Arrays.asList(entry.getValue()[0].trim().split(","));
                for(OntologyClass ontologyClass : ontologyClasses) {
                    if(!ontologyClass.getOntclass().toString().equals(keySplit[0]))continue;
                    for(OntologyClassRestriction ontologyClassRestriction : ontologyClass.getRestrictions()) {
                        if(!ontologyClassRestriction.getOntProperty().toString().equals(keySplit[1]))continue;
                        Restriction restriction = ontologyClassRestriction.getRestriction();
                        if(restriction.isSomeValuesFromRestriction()) {
                            boolean isSomeValuesFrom=false;
                            for(String individual : individuals) {
                                for(String allowedIndividual : ontologyClassRestriction.getIndividuals()) {
                                    if(individual.equals(allowedIndividual)) {
                                        isSomeValuesFrom=true;
                                        break;
                                    }
                                }
                            }
                            if(isSomeValuesFrom==false) {
                                validation+=("For class " + keySplit[0] + " and property " + keySplit[1] + " at least one value must be from the dropdown.\n");
                            }
                        } else if(restriction.isAllValuesFromRestriction()) {
                            boolean isAllValuesFrom=true;
                            for(String individual : individuals) {
                                boolean found=false;
                                for(String allowedIndividual : ontologyClassRestriction.getIndividuals()) {
                                    if(individual.equals(allowedIndividual)) {
                                        found=true;
                                        break;
                                    }
                                }
                                if(found==false) {
                                    isAllValuesFrom=false;
                                    break;
                                }
                            }
                            if(isAllValuesFrom==false) {
                                validation+=("For class " + keySplit[0] + " and property " + keySplit[1] + " all values must be from the dropdown.\n");
                            }
                        }
                    }
                }
            }
        }
        return validation;
    }
}


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
package de.unibonn.model;

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Restriction;

import java.util.*;

public class OntologyClassRestriction {
    Restriction restriction;
    OntProperty ontProperty;
    String ontPropertyString;
    List<String> individuals;
    String description;
    QualifiedCardinalityRestriction qualifiedCardinalityRestriction;

    public String getOntPropertyString() {
        return ontPropertyString;
    }

    public void setOntPropertyString(String ontPropertyString) {
        this.ontPropertyString = ontPropertyString;
    }

    public QualifiedCardinalityRestriction getQualifiedCardinalityRestriction() {
        return qualifiedCardinalityRestriction;
    }

    public void setQualifiedCardinalityRestriction(QualifiedCardinalityRestriction qualifiedCardinalityRestriction) {
        this.qualifiedCardinalityRestriction = qualifiedCardinalityRestriction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    public OntProperty getOntProperty() {
        return ontProperty;
    }

    public void setOntProperty(OntProperty ontProperty) {
        this.ontProperty = ontProperty;
    }

    public List<String> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<String> individuals) {
        this.individuals = individuals;
    }
}
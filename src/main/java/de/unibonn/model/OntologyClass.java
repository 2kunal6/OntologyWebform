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

import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;

import de.unibonn.util.OntologyStatementUtil;

import java.util.*;

public class OntologyClass implements Comparable<OntologyClass> {
    OntClass ontclass;
    List<Triple> triples = new ArrayList<>();
    Set<String> individuals = new HashSet<>();
    Set<OntologyClassRestriction> restrictions = new HashSet<>();
    List<OntProperty> properties = new ArrayList<>();
    OntologyStatementUtil ontologyStatementUtil = new OntologyStatementUtil();

    public String getBase_uri() {
        return base_uri;
    }

    public void setBase_uri(String base_uri) {
        this.base_uri = base_uri;
    }

    String base_uri;

    public List<OntProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<OntProperty> properties) {
        this.properties = properties;
    }

    public Set<OntologyClassRestriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Set<OntologyClassRestriction> restrictions) {
        this.restrictions = restrictions;
    }

    public Set<String> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(Set<String> individuals) {
        this.individuals = individuals;
    }

    public OntClass getOntclass() {
        return ontclass;
    }

    public void setOntclass(OntClass ontclass) {
        this.ontclass = ontclass;
    }

    public List<Triple> getTriples() {
        return triples;
    }

    public void setTriples(List<Triple> triples) {
        this.triples = triples;
    }

    public int compareTo(OntologyClass other) {
        Integer otherPos = ontologyStatementUtil.getPos(other.ontclass.listProperties().toList());
        Integer ownPos = ontologyStatementUtil.getPos(this.ontclass.listProperties().toList());

        if(otherPos == null)return -1;
        if(ownPos == null)return 1;
        if(ownPos < otherPos)return -1;
        return 1;
    }
}


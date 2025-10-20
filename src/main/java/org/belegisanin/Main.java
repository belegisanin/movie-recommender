package org.belegisanin;

import org.belegisanin.utils.OntologyLoader;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
    }

    public static void sparqlRecommender() {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OntologyLoader loader = new OntologyLoader(manager);

    }

}
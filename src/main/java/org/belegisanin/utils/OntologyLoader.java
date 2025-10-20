package org.belegisanin.utils;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import java.io.File;

public class OntologyLoader {

    public static void main(String[] args) {
        try {
            // Create OWL ontology manager
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            // Method 1: Load with automatic import resolution
            loadWithAutoImport(manager);

            // Method 2: Load with manual IRI mapping (if imports don't resolve automatically)
            // loadWithManualMapping(manager);

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    public static void loadWithAutoImport(OWLOntologyManager manager) throws OWLOntologyCreationException {
        File ontologyDirectory = new File("ontology");
        manager.getIRIMappers().add(new AutoIRIMapper(ontologyDirectory, true));

        File schemaFile = new File("ontology/movie-ontology-schema.rdf");
        OWLOntology schemaOntology = manager.loadOntologyFromOntologyDocument(schemaFile);

        File instancesFile = new File("ontology/movie-ontology-instances.rdf");
        OWLOntology instancesOntology = manager.loadOntologyFromOntologyDocument(instancesFile);

        System.out.println("\nAll loaded ontologies:");
        for (OWLOntology ont : manager.getOntologies()) {
            System.out.println("  - " + ont.getOntologyID());
        }
    }

}

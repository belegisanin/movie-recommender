package org.belegisanin.utils;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import java.io.File;

public class OntologyLoader {

    private OWLOntologyManager manager;

    public OntologyLoader(OWLOntologyManager manager) {
        this.manager = manager;
    }

    public void loadWithAutoImport() throws OWLOntologyCreationException {
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

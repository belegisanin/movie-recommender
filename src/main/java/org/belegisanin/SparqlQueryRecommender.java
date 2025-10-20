package org.belegisanin;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

import java.util.Scanner;

public class SparqlQueryRecommender {

    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, "ontology/movie-ontology-schema.rdf");
        RDFDataMgr.read(model, "ontology/movie-ontology-instances.rdf");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Genre: ");
        String genreInput = scanner.nextLine().trim();
        System.out.print("Actor: ");
        String actorInput = scanner.nextLine().trim();
        System.out.print("Director: ");
        String directorInput = scanner.nextLine().trim();
        System.out.print("Year: ");
        String yearInput = scanner.nextLine().trim();

        ParameterizedSparqlString pss = getParameterizedSparqlString();

        pss.setIri("genre", "http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#" + genreInput.replace(" ", "_"));
        pss.setIri("actor", "http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#" + actorInput.replace(" ", "_"));
        pss.setIri("director", "http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#" + directorInput.replace(" ", "_"));
        pss.setIri("year", "http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#" + yearInput);

        Query query = QueryFactory.create(pss.asQuery());
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
    }

    private static ParameterizedSparqlString getParameterizedSparqlString() {
        String queryTemplate = """
            PREFIX ins: <http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#>
            PREFIX ont: <http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender#>

            SELECT ?movie
            WHERE {
                ?movie a ont:Movie ;
                       ont:hasGenre ?genre ;
                       ont:hasActor ?actor ;
                       ont:isDirectedBy ?director ;
                       ont:hasReleasedIn ?year .
            }
            ORDER BY ?movie
        """;

        ParameterizedSparqlString pss = new ParameterizedSparqlString(queryTemplate);
        return pss;
    }

}

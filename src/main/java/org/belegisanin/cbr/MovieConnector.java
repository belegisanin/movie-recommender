package org.belegisanin.cbr;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.net.URL;
import java.util.*;

public class MovieConnector implements Connector {

    private final Map<String, MovieDescription> movies = new HashMap<>();
    private final Collection<CBRCase> cases = new ArrayList<>();

    public MovieConnector() {
        loadMoviesFromRdf();
    }

    public void loadMoviesFromRdf() {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, "ontology/movie-ontology-schema.rdf");
        RDFDataMgr.read(model, "ontology/movie-ontology-instances.rdf");

        ParameterizedSparqlString pss = getParameterizedSparqlString();
        Query query = QueryFactory.create(pss.asQuery());

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                addMovieDescription(sol);
            }

            movies.forEach((_, md) -> {
                CBRCase c = new CBRCase();
                c.setDescription(md);
                cases.add(c);
            });
        }
    }

    private void addMovieDescription(QuerySolution sol) {
        String movieURI = sol.getResource("movie").getURI();
        String movie = movieURI.substring(movieURI.lastIndexOf('#') + 1);

        String directorURI = sol.getResource("director").getURI();
        String director = directorURI.substring(directorURI.lastIndexOf('#') + 1);

        String genreURI = sol.getResource("genre").getURI();
        String genre = genreURI.substring(genreURI.lastIndexOf('#') + 1);

        String actorURI = sol.getResource("actor").getURI();
        String actor = actorURI.substring(actorURI.lastIndexOf('#') + 1);

        String yearURI = sol.getResource("year").getURI();
        int year = Integer.parseInt(yearURI.substring(yearURI.lastIndexOf('#') + 1));

        MovieDescription md = movies.getOrDefault(movie, new MovieDescription(movie));
        md.setDirector(director);
        md.addGenre(genre);
        md.addActor(actor);
        md.setYear(year);

        movies.put(movie, md);
    }

    @Override
    public void initFromXMLfile(URL url) {}

    @Override
    public void close() {}

    @Override
    public void storeCases(Collection<CBRCase> collection) {}

    @Override
    public void deleteCases(Collection<CBRCase> collection) {}

    @Override
    public Collection<CBRCase> retrieveAllCases() {
        return cases;
    }

    @Override
    public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter caseBaseFilter) {
        return List.of();
    }

    private static ParameterizedSparqlString getParameterizedSparqlString() {
        String queryTemplate = """
        PREFIX mr: <http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender#>
        PREFIX mrins: <http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

        SELECT ?movie ?director ?genre ?actor ?year
        WHERE {
            ?movie rdf:type mr:Movie .
            OPTIONAL { ?movie mr:isDirectedBy ?director . }
            OPTIONAL { ?movie mr:hasGenre ?genre . }
            OPTIONAL { ?movie mr:hasActor ?actor . }
            OPTIONAL { ?movie mr:hasReleasedIn ?year . }
        }
        """;

        return new ParameterizedSparqlString(queryTemplate);
    }
}

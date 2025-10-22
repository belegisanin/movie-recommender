package org.belegisanin;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public class FuzzyMovieRating {

    public static void main(String[] args) {
        String filename = "rulesets/movie_quality.fcl";

        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, "ontology/movie-ontology-schema.rdf");
        RDFDataMgr.read(model, "ontology/movie-ontology-instances.rdf");

        ParameterizedSparqlString pss = getParameterizedSparqlString();
        Query query = QueryFactory.create(pss.asQuery());

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();

                String movieURI = sol.getResource("movie").getURI();
                String movieName = movieURI.substring(movieURI.lastIndexOf('#') + 1);

                double direction = sol.contains("direction") ? sol.getLiteral("direction").getDouble() : 0.0;
                double acting = sol.contains("acting") ? sol.getLiteral("acting").getDouble() : 0.0;
                double scenario = sol.contains("scenario") ? sol.getLiteral("scenario").getDouble() : 0.0;
                double vfx = sol.contains("vfx") ? sol.getLiteral("vfx").getDouble() : 0.0;

                FIS fis = FIS.load(filename, true);

                fis.setVariable("direction", direction);
                fis.setVariable("acting", acting);
                fis.setVariable("scenario", scenario);
                fis.setVariable("vfx", vfx);

                fis.evaluate();

                Variable qualityVar = fis.getVariable("quality");
                double quality = qualityVar.getValue();
                String qualityTerm = getLinguisticTerm(qualityVar);

                System.out.printf("%-25s -> Quality: %s (%.2f) (D: %.1f, A: %.1f, S: %.1f, V: %.1f)%n",
                        movieName, qualityTerm, quality, direction, acting, scenario, vfx);
            }
        }
    }

    private static ParameterizedSparqlString getParameterizedSparqlString() {
        String queryTemplate = """
            PREFIX mr: <http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender#>
            PREFIX mrins: <http://www.semanticweb.org/beleg/ontologies/2025/8/movie-recommender-ins#>
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            SELECT ?movie ?direction ?acting ?scenario ?vfx
            WHERE {
                ?movie rdf:type mr:Movie .
                OPTIONAL { ?movie mrins:hasDirectionRating ?direction . }
                OPTIONAL { ?movie mrins:hasActingRating ?acting . }
                OPTIONAL { ?movie mrins:hasScenarioRating ?scenario . }
                OPTIONAL { ?movie mrins:hasVfxRating ?vfx . }
            }
            ORDER BY ?movie
        """;

        return new ParameterizedSparqlString(queryTemplate);
    }

    private static String getLinguisticTerm(Variable variable) {
        String term = "";
        double maxMembership = 0.0;

        for (String termName : variable.getLinguisticTerms().keySet()) {
            double membership = variable.getMembership(termName);
            if (membership > maxMembership) {
                maxMembership = membership;
                term = termName;
            }
        }

        return  term;
    }
}

package org.belegisanin;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import org.belegisanin.cbr.*;

import java.util.Collection;
import java.util.Comparator;

public class CbrMovieSimilarity {

    public static void main(String[] args) {
        NNConfig simConfig = MovieSimilarityConfig.getConfig();

        MovieConnector connector = new MovieConnector();
        Collection<CBRCase> allCases = connector.retrieveAllCases();

        MovieDescription queryDesc = new MovieDescription();
        queryDesc.addGenre("ScienceFiction");
        queryDesc.setDirector("Christopher_Nolan");
        queryDesc.addActor("Tom_Hardy");
        queryDesc.setYear(2012);

        CBRQuery query = new CBRQuery();
        query.setDescription(queryDesc);

        Collection<RetrievalResult> results = NNScoringMethod.evaluateSimilarity(allCases, query, simConfig);

        results.stream()
                .sorted(Comparator.comparingDouble(RetrievalResult::getEval).reversed())
                .limit(5)
                .forEach(r -> {
                    MovieDescription d = (MovieDescription) r.get_case().getDescription();
                    System.out.printf("%.2f - %s (%s)\n", r.getEval(), d.getTitle(), d.getGenres());
                });
    }

}

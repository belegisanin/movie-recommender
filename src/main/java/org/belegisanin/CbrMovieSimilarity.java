package org.belegisanin;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import org.belegisanin.cbr.*;

import java.util.*;
import java.util.stream.Collectors;

public class CbrMovieSimilarity {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter genres: ");
        List<String> genres = parseListInput(scanner.nextLine());

        System.out.println("Enter director: ");
        String director = scanner.nextLine().trim();

        System.out.println("Enter actors: ");
        List<String> actors = parseListInput(scanner.nextLine());

        System.out.println("Enter year: ");
        int year = readInt(scanner);

        MovieDescription queryDesc = new MovieDescription();
        queryDesc.setGenres(genres);
        queryDesc.setDirector(director);
        queryDesc.setActors(actors);
        queryDesc.setYear(year);

        NNConfig simConfig = MovieSimilarityConfig.getConfig();
        MovieConnector connector = new MovieConnector();
        Collection<CBRCase> allCases = connector.retrieveAllCases();

        CBRQuery query = new CBRQuery();
        query.setDescription(queryDesc);

        Collection<RetrievalResult> results = NNScoringMethod.evaluateSimilarity(allCases, query, simConfig);

        System.out.println("\nTop 5 similar movies:");
        results.stream()
                .sorted(Comparator.comparingDouble(RetrievalResult::getEval).reversed())
                .limit(5)
                .forEach(r -> {
                    MovieDescription d = (MovieDescription) r.get_case().getDescription();
                    System.out.printf("%.2f - %s (%d) %s\n", r.getEval(), d.getTitle(), d.getYear(), d.getGenres());
                });
    }

    private static List<String> parseListInput(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }
}

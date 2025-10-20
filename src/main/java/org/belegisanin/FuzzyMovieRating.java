package org.belegisanin;

import net.sourceforge.jFuzzyLogic.FIS;

public class FuzzyMovieRating {

    public static void main(String[] args) {
        String filename = "rulesets/movie_quality.fcl";
        FIS fis = FIS.load(filename, true);

        if (fis == null) {
            System.err.println("Unable to load .fcl file: " + filename);
            return;
        }

        fis.setVariable("direction", 4);
        fis.setVariable("acting", 10);
        fis.setVariable("scenario", 8);
        fis.setVariable("vfx", 2);

        fis.evaluate();

        double q = fis.getVariable("quality").getValue();
        System.out.println("movie quality: " + q);
    }
}

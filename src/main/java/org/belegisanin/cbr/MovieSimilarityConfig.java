package org.belegisanin.cbr;

import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.*;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.*;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.*;
import es.ucm.fdi.gaia.jcolibri.cbrcore.*;

public class MovieSimilarityConfig {

    public static NNConfig getConfig() {

        NNConfig simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        simConfig.addMapping(new Attribute("genres", MovieDescription.class), new Equal());
        simConfig.addMapping(new Attribute("director", MovieDescription.class), new Equal());
        simConfig.addMapping(new Attribute("actors", MovieDescription.class), new Interval(0.0));
        simConfig.addMapping(new Attribute("year", MovieDescription.class), new Interval(10));

        return simConfig;
    }

}

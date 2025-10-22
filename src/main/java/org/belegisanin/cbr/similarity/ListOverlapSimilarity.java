package org.belegisanin.cbr.similarity;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListOverlapSimilarity implements LocalSimilarityFunction {

    @Override
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
        if (caseObject == null || queryObject == null)
            return 0.0;

        if (!(caseObject instanceof List) || !(queryObject instanceof List))
            throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());

        Set<String> caseSet = new HashSet<>((List<String>) caseObject);
        Set<String> querySet = new HashSet<>((List<String>) queryObject);

        if (caseSet.isEmpty() && querySet.isEmpty())
            return 1.0;

        Set<String> intersection = new HashSet<>(caseSet);
        intersection.retainAll(querySet);

        Set<String> union = new HashSet<>(caseSet);
        union.addAll(querySet);

        return (double) intersection.size() / (double) union.size();
    }

    @Override
    public boolean isApplicable(Object caseObject, Object queryObject) {
        return caseObject instanceof List && queryObject instanceof List;
    }

}

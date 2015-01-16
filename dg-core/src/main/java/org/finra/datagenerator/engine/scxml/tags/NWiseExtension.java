package org.finra.datagenerator.engine.scxml.tags;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Marshall Peters
 * Date: 1/14/15
 */
public class NWiseExtension implements CustomTagExtension<NWiseExtension.NWiseAction> {

    public Class<NWiseAction> getTagActionClass() {
        return NWiseAction.class;
    }

    public String getTagName() {
        return "nwise";
    }

    public String getTagNameSpace() {
        return "org.finra.datagenerator";
    }

    private void makeNWiseTuplesHelper(List<Set<String>> resultTuples, String[] variables,
                                       int startVariable, Set<String> preGivenVariables, int nWise) {
        if (nWise <= 0) {
            Set<String> result = new HashSet<>(preGivenVariables);
            resultTuples.add(result);
            return;
        }

        for (int varIndex = startVariable; varIndex <= variables.length - nWise; varIndex++) {
            preGivenVariables.add(variables[varIndex]);
            makeNWiseTuplesHelper(resultTuples, variables, varIndex + 1, preGivenVariables, nWise - 1);
            preGivenVariables.remove(variables[varIndex]);
        }
    }

    public List<Set<String>> makeNWiseTuples(String[] variables, int nWise) {
        List<Set<String>> completeTuples = new ArrayList<>();
        makeNWiseTuplesHelper(completeTuples, variables, 0, new HashSet<String>(), nWise);

        return completeTuples;
    }

    private void expandTupleHelper(List<Map<String, String>> resultExpansions, String[] variables,
                                   Map<String, String[]> variableDomains, int nextVariable,
                                   Map<String, String> partialAssignment) {
        if (nextVariable >= variables.length) {
            Map<String, String> resultAssignment = new HashMap<>(partialAssignment);
            resultExpansions.add(resultAssignment);
            return;
        }

        String variable = variables[nextVariable];
        for (String domainValue : variableDomains.get(variable)) {
            partialAssignment.put(variable, domainValue);
            expandTupleHelper(resultExpansions, variables, variableDomains, nextVariable + 1, partialAssignment);
        }
    }

    public List<Map<String, String>> expandTupleIntoTestCases(Set<String> tuple, Map<String, String[]> variableDomains) {
        List<Map<String, String>> expandedTestCases = new ArrayList<>();
        String[] variables = tuple.toArray(new String[tuple.size()]);
        expandTupleHelper(expandedTestCases, variables, variableDomains, 0, new HashMap<String, String>());

        return expandedTestCases;
    }

    public List<Map<String, String>> produceNWise(int nWise, String[] coVariables, Map<String, String[]> variableDomains) {
        List<Set<String>> tuples = makeNWiseTuples(coVariables, nWise);

        List<Map<String, String>> testCases = new ArrayList<>();
        for (Set<String> tuple : tuples) {
            testCases.addAll(expandTupleIntoTestCases(tuple, variableDomains));
        }

        return testCases;
    }

    public List<Map<String, String>> pipelinePossibleStates(NWiseAction action, List<Map<String, String>> possibleStateList) {
        String[] coVariables = action.getCoVariables().split(",");
        int n = Integer.valueOf(action.getN());

        List<Map<String, String>> newPossibleStateList = new ArrayList<>();

        for (Map<String, String> possibleState : possibleStateList) {
            Map<String, String[]> variableDomains = new HashMap<>();
            Map<String, String> defaultVariableValues = new HashMap<>();
            for (String variable : coVariables) {
                String variableMetaInfo = possibleState.get(variable);
                String[] variableDomain = variableMetaInfo.split(",");
                variableDomains.put(variable, variableDomain);
                defaultVariableValues.put(variable, variableDomain[0]);
            }

            List<Map<String, String>> nWiseCombinations = produceNWise(n, coVariables, variableDomains);
            for (Map<String, String> nWiseCombination : nWiseCombinations) {
                Map<String, String> newPossibleState = new HashMap<>(possibleState);
                newPossibleState.putAll(defaultVariableValues);
                newPossibleState.putAll(nWiseCombination);
                newPossibleStateList.add(newPossibleState);
            }
        }

        return newPossibleStateList;
    }

    public static class NWiseAction extends Action {
        private String coVariables;
        private String n;

        public String getCoVariables() {
            return coVariables;
        }

        public void setCoVariables(String coVariables) {
            this.coVariables = coVariables;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public void execute(EventDispatcher eventDispatcher, ErrorReporter errorReporter, SCInstance scInstance,
                            Log log, Collection collection) throws ModelException, SCXMLExpressionException {

        }
    }
}

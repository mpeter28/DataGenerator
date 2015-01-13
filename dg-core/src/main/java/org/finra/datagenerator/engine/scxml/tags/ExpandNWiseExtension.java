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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Marshall Peters
 * Date: 1/12/15
 */
public class ExpandNWiseExtension implements CustomTagExtension<ExpandNWiseExtension.ExpandNWiseAction> {

    public Class<ExpandNWiseAction> getTagActionClass() {
        return ExpandNWiseAction.class;
    }

    public String getTagName() {
        return "expandNWise";
    }

    public String getTagNameSpace() {
        return "org.finra.datagenerator";
    }

    public Set<String> extractVariableDomain(String variable, List<Map<String, String>> possibleStateList) {
        Set<String> variableDomain = new HashSet<>();

        for (Map<String, String> possibleState : possibleStateList) {
            if (possibleState.containsKey(variable)) {
                variableDomain.add(possibleState.get(variable));
            }
        }

        return variableDomain;
    }

    public List<Set<String>> makeNWiseTuples(Set<String> variables, int nWise) {
        Set<String> emptySet = new HashSet<>();
        List<Set<String>> nTuples = new ArrayList<>();
        findNtuples(variables, nWise, emptySet, nTuples);
        return nTuples;
    }

    private void findNtuples(Set<String> vars, int n, Set<String> temp, List<Set<String>> product) {
        if (temp.size() == n) {
            product.add(temp);
        }
        else {
            for (String next : vars) {
                if (!temp.contains(next)) {
                    Set<String> newTemp = new HashSet<String>(temp);
                    newTemp.add(next);
                    findNtuples(vars, n, newTemp, product);
                }
            }
        }
    }

    public List<Map<String, String>> expandTupleIntoTestCases(Set<String> tuple, Map<String, Set<String>> variableDomains) {
        List<Map<String, String>> expandedTestCases = new ArrayList<>();
        expandedTestCases.add(new HashMap<String, String>());

        for (String variable : tuple) {
            List<Map<String, String>> tempProduct = new ArrayList<>();
            for (Map<String, String> incompleteTestCase : expandedTestCases) {
                for (String domainValue : variableDomains.get(variable)) {
                    Map<String, String> moreCompleteTestCase = new HashMap<>(incompleteTestCase);
                    moreCompleteTestCase.put(variable, domainValue);
                    tempProduct.add(moreCompleteTestCase);
                }
            }
            expandedTestCases = tempProduct;
        }

        return expandedTestCases;
    }

    public List<Map<String, String>> removeCoveredTestCases(List<Map<String, String>> testCases, Map<String, String> testRow) {
        List<Map<String, String>> coveredTestCases = new ArrayList<>();

        for (Map<String, String> testCase : testCases) {
            boolean covered = true;

            for (String variable : testCase.keySet()) {
                if (!testCase.get(variable).equals(testRow.get(variable))) {
                    covered = false;
                    break;
                }
            }

            if (covered) {
                coveredTestCases.add(testCase);
            }
        }

        for (Map<String, String> coveredTestCase : coveredTestCases) {
            testCases.remove(coveredTestCase);
        }

        return testCases;
    }

    public List<Map<String, String>> pipelinePossibleStates(ExpandNWiseAction action, List<Map<String, String>> possibleStateList) {
        String variableToExpand = action.getName();

        Set<String> expansionDomain = new HashSet<>();
        Collections.addAll(expansionDomain, action.getSet().split(","));

        Set<String> coVariables = new HashSet<>();
        Collections.addAll(coVariables, action.getCoVariables().split(","));

        Map<String, Set<String>> variableDomains = new HashMap<>();
        for (String variable : coVariables) {
            variableDomains.put(variable, extractVariableDomain(variable, possibleStateList));
        }
        variableDomains.put(variableToExpand, expansionDomain);

        int n = Integer.valueOf(action.getN());
        List<Set<String>> nWiseTuples = makeNWiseTuples(coVariables, n - 1);
        List<Map<String, String>> newTestCases = new ArrayList<>();
        for (Set<String> nWiseTuple : nWiseTuples) {
            nWiseTuple.add(variableToExpand);
            newTestCases.addAll(expandTupleIntoTestCases(nWiseTuple, variableDomains));
        }

        List<Map<String, String>> newPossibleStates = new ArrayList<>();
        for (Map<String, String> oldPossibleState : possibleStateList) {
            boolean contributed = false;

            for (String expansionDomainElement : expansionDomain) {
                Map<String, String> newPossibleState = new HashMap<>(oldPossibleState);
                newPossibleState.put(variableToExpand, expansionDomainElement);

                int remainingTestCases = newTestCases.size();
                newTestCases = removeCoveredTestCases(newTestCases, newPossibleState);
                if (remainingTestCases > newTestCases.size()) {
                    contributed = true;
                    newPossibleStates.add(newPossibleState);
                }
            }

            if (!contributed) {
                Map<String, String> newPossibleState = new HashMap<>(oldPossibleState);
                newPossibleState.put(variableToExpand, expansionDomain.iterator().next());
                newPossibleStates.add(newPossibleState);
            }
        }

        return newPossibleStates;
    }

    public static class ExpandNWiseAction extends Action {
        private String name;
        private String set;
        private String coVariables;
        private String n;

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSet() {
            return set;
        }

        public void setSet(String set) {
            this.set = set;
        }

        public String getCoVariables() {
            return coVariables;
        }

        public void setCoVariables(String coVariables) {
            this.coVariables = coVariables;
        }

        public void execute(EventDispatcher eventDispatcher, ErrorReporter errorReporter, SCInstance scInstance,
                            Log log, Collection collection) throws ModelException, SCXMLExpressionException {

        }
    }
}

package org.finra.datagenerator;

import org.finra.datagenerator.engine.scxml.tags.NWiseExtension;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Marshall Peters
 * Date: 1/13/15
 */
public class NWiseSmokeTest {

    public static void main(String[] args) {
        List<Map<String, String>> possibleState = new LinkedList<>();
        Map<String, String> pairWiseParameters = new HashMap<>();
        pairWiseParameters.put("var1", "A,B,C,D,E");
        pairWiseParameters.put("var2", "A,B,C,D,E");
        pairWiseParameters.put("var3", "A,B,C,D,E");
        pairWiseParameters.put("var4", "A,B,C,D,E");
        pairWiseParameters.put("var5", "A,B,C,D,E");
        possibleState.add(pairWiseParameters);

        NWiseExtension.NWiseAction nWiseAction = new NWiseExtension.NWiseAction();
        nWiseAction.setN("2");
        nWiseAction.setCoVariables("var1,var2,var3,var4");

        NWiseExtension nWiseExtension = new NWiseExtension();
        List<Map<String, String>> result = nWiseExtension.pipelinePossibleStates(nWiseAction, possibleState);

        for (Map<String, String> resultMap : result) {
            System.out.println(resultMap);
        }
    }
}

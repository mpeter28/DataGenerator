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
        pairWiseParameters.put("var6", "A,B,C,D,E");
        pairWiseParameters.put("var7", "A,B,C,D,E");
        pairWiseParameters.put("var8", "A,B,C,D,E");
        pairWiseParameters.put("var9", "A,B,C,D,E");
        pairWiseParameters.put("var10", "A,B,C,D,E");
        pairWiseParameters.put("var11", "A,B,C,D,E");
        pairWiseParameters.put("var12", "A,B,C,D,E");
        pairWiseParameters.put("var13", "A,B,C,D,E");
        pairWiseParameters.put("var14", "A,B,C,D,E");
        pairWiseParameters.put("var15", "A,B,C,D,E");
        possibleState.add(pairWiseParameters);

        NWiseExtension.NWiseAction nWiseAction = new NWiseExtension.NWiseAction();
        nWiseAction.setN("5");
        nWiseAction.setCoVariables("var1,var2,var3,var4,var5,var6,var7,var8,var9,var10,var11,var12,var13,var14,var15");

        NWiseExtension nWiseExtension = new NWiseExtension();
        System.out.println(System.currentTimeMillis());
        List<Map<String, String>> result = nWiseExtension.pipelinePossibleStates(nWiseAction, possibleState);
        System.out.println(System.currentTimeMillis());
        System.out.println(result.size());
    }
}

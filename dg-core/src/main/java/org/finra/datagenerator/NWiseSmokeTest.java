package org.finra.datagenerator;

import org.finra.datagenerator.engine.scxml.tags.ExpandNWiseExtension;

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
        List<Map<String,String>> possibleStates = new LinkedList<>();
        for (int var1 = 1; var1 <= 5; var1++) {
            for (int var2 = 1; var2 <= 5; var2++) {
                for (int var3 = 1; var3 <= 5; var3++) {
                    HashMap<String, String> possibleState = new HashMap<>();
                    possibleState.put("var1", String.valueOf(var1));
                    possibleState.put("var2", String.valueOf(var2));
                    possibleState.put("var3", String.valueOf(var3));
                    possibleStates.add(possibleState);
                }
            }
        }

        ExpandNWiseExtension expandNWiseExtension = new ExpandNWiseExtension();

        ExpandNWiseExtension.ExpandNWiseAction expandNWiseAction = new ExpandNWiseExtension.ExpandNWiseAction();
        expandNWiseAction.setN("3");
        expandNWiseAction.setName("var4");
        expandNWiseAction.setCoVariables("var1,var2,var3");
        expandNWiseAction.setSet("1,2,3,4,5");

        possibleStates = expandNWiseExtension.pipelinePossibleStates(expandNWiseAction, possibleStates);

        expandNWiseAction = new ExpandNWiseExtension.ExpandNWiseAction();
        expandNWiseAction.setN("3");
        expandNWiseAction.setName("var5");
        expandNWiseAction.setCoVariables("var1,var2,var3,var4");
        expandNWiseAction.setSet("1,2,3,4,5");

        possibleStates = expandNWiseExtension.pipelinePossibleStates(expandNWiseAction, possibleStates);

        expandNWiseAction = new ExpandNWiseExtension.ExpandNWiseAction();
        expandNWiseAction.setN("3");
        expandNWiseAction.setName("var6");
        expandNWiseAction.setCoVariables("var1,var2,var3,var4,var5");
        expandNWiseAction.setSet("1,2,3,4,5");

        possibleStates = expandNWiseExtension.pipelinePossibleStates(expandNWiseAction, possibleStates);

        expandNWiseAction = new ExpandNWiseExtension.ExpandNWiseAction();
        expandNWiseAction.setN("3");
        expandNWiseAction.setName("var7");
        expandNWiseAction.setCoVariables("var1,var2,var3,var4,var5,var6");
        expandNWiseAction.setSet("1,2,3,4,5");

        possibleStates = expandNWiseExtension.pipelinePossibleStates(expandNWiseAction, possibleStates);

        expandNWiseAction = new ExpandNWiseExtension.ExpandNWiseAction();
        expandNWiseAction.setN("3");
        expandNWiseAction.setName("var8");
        expandNWiseAction.setCoVariables("var1,var2,var3,var4,var5,var6,var7");
        expandNWiseAction.setSet("1,2,3,4,5");

        possibleStates = expandNWiseExtension.pipelinePossibleStates(expandNWiseAction, possibleStates);

        expandNWiseAction = new ExpandNWiseExtension.ExpandNWiseAction();
        expandNWiseAction.setN("3");
        expandNWiseAction.setName("var9");
        expandNWiseAction.setCoVariables("var1,var2,var3,var4,var5,var6,var7,var8");
        expandNWiseAction.setSet("1,2,3,4,5");

        possibleStates = expandNWiseExtension.pipelinePossibleStates(expandNWiseAction, possibleStates);

        int count = 0;
        for (Map<String,String> possibleState : possibleStates) {
            System.out.println(possibleState.toString());
            count++;
        }
        System.out.println(count);
    }
}

package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Advent6 {

    File file = new File("src/files/advent6.txt");

    public void run() {
        System.err.println(totalQuestionsAnswered());
    }


    // Two star
    public int totalQuestionsAnswered() {
        int totalSum = 0;
        int groupCount = 0;
        int questionsAnswered = 0;
        BufferedReader br;
        HashMap<Character, Integer> groupSet = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(file));
            String nextLine = br.readLine();
            String currentLine = "";
            while(nextLine != null){
                currentLine = nextLine;
                for (int i = 0; i < currentLine.length(); i++) {
                    int count = groupSet.get(currentLine.charAt(i)) != null ? groupSet.get(currentLine.charAt(i)) : 0;
                    groupSet.put(currentLine.charAt(i), count + 1);
                }
                groupCount++;
                nextLine = br.readLine();
                if(nextLine == null || nextLine.isEmpty() || currentLine.isEmpty()){
                    for (Integer value : groupSet.values()) {
                        if(value == groupCount){
                            questionsAnswered++;
                        }
                    }
                    totalSum += questionsAnswered;
                    groupCount = 0;
                    questionsAnswered = 0;
                    groupSet.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalSum;
    }
}

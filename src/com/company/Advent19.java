package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Advent19 implements Runnable {

	File file = new File("src/files/advent19.txt");
	HashMap<Integer, Rule> ruleMap = new HashMap<>();
	ArrayList<String> messagesToValidate = new ArrayList<>();
	ArrayList<String> allValidMessages = new ArrayList<>();
	int longestString;

	public class Rule {
		int ruleId;
		String stringToMatch = "";
		ArrayList<ArrayList<Rule>> rulesToMatch = new ArrayList<ArrayList<Rule>>();

		public Rule(int ruleId) {
			this.ruleId = ruleId;
		}
	}



	@Override public void run() {
		long timer = System.nanoTime();
		readFile();
//		printRules();
		StringBuilder starterRule = new StringBuilder();
		ArrayList<StringBuilder> fullRules = new ArrayList<>();
		fullRules.add(starterRule);
		longestString = messagesToValidate.stream().max(Comparator.comparingInt(String::length)).get().length();
		System.err.println(longestString + " LONGEST");
		System.err.println(longestString);
		recursiveRules(ruleMap.get(0),  fullRules, 0);
		for (StringBuilder fullRule : fullRules) {
			allValidMessages.add(fullRule.toString());
		}
		int messageCounter = 0;
		for (String s : messagesToValidate) {
			if(allValidMessages.contains(s)) messageCounter++;
		}
		long end = System.nanoTime();
		System.err.println("Time: " + (double)(end - timer) / 1000000 + " ms");
		System.err.println(messageCounter);
	}

	private ArrayList<StringBuilder> recursiveRules(Rule rule, ArrayList<StringBuilder> buildingRules, int recursionLimit) {
		if(rule.stringToMatch.equals("")){
			if(rule.rulesToMatch.size() > 1){
				ArrayList<StringBuilder> totalRules = new ArrayList<>();
				for (ArrayList<Rule> rulesToMatch : rule.rulesToMatch) {
					ArrayList<StringBuilder> tempList = deepClone(buildingRules);
					for (Rule toMatch : rulesToMatch) {
						tempList = recursiveRules(toMatch, tempList, recursionLimit);
					}
					totalRules.addAll(tempList);
				}
				buildingRules.clear();
				buildingRules.addAll(totalRules);
			} else if(rule.rulesToMatch.size() == 1){
				for (Rule nextRule : rule.rulesToMatch.get(0)) {
					buildingRules = recursiveRules(nextRule, buildingRules, recursionLimit);
				}
			}
		} else {
			for (StringBuilder buildingRule : buildingRules) {
				buildingRule.append(rule.stringToMatch);
			}
		}

		return buildingRules;
	}

	public ArrayList<StringBuilder> deepClone(ArrayList<StringBuilder> prevList){
		ArrayList<StringBuilder> stringBuilders = new ArrayList<>();
		for (StringBuilder stringBuilder : prevList) {
			StringBuilder newStringBuilder = new StringBuilder();
			newStringBuilder.append(stringBuilder);
			stringBuilders.add(newStringBuilder);
		}
		return stringBuilders;
	}

	public void printRules(){
		for (Rule value : ruleMap.values()) {
			System.err.println("id:" + value.ruleId);
			for (ArrayList<Rule> rulesToMatch : value.rulesToMatch) {
				System.err.print(" | ");
				for (Rule toMatch : rulesToMatch) {
					System.err.print(toMatch.ruleId + " ");
				}
			}
			System.err.println();
		}
	}

	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			boolean readRules = true;
			while ((line = br.readLine()) != null) {
				if(line.isEmpty()){
					readRules = false;
					continue;
				}
				if(readRules) {
					String[] ruleSplit = line.split(":");
					String[] rulesToFollow = ruleSplit[1].trim().split(" ");
					Rule newRule = ruleMap.get(Integer.parseInt(ruleSplit[0]));
					if(newRule == null){
						newRule = new Rule(Integer.parseInt(ruleSplit[0]));
						ruleMap.put(newRule.ruleId, newRule);
					}
					ArrayList<Rule> rulesToAdd = null;
					for (int i = 0; i < rulesToFollow.length; i++) {
						if (rulesToFollow[i].matches("\"[a-z]*\"")) {
							rulesToFollow[i] = rulesToFollow[i].replace("\"", "");
							newRule.stringToMatch = rulesToFollow[i];
						} else if (rulesToFollow[i].equals("|")) {
							rulesToAdd = new ArrayList<>();
							newRule.rulesToMatch.add(rulesToAdd);
						} else {
							if (rulesToAdd == null) {
								rulesToAdd = new ArrayList<>();
								newRule.rulesToMatch.add(rulesToAdd);
							}
							Rule ruleToFollow = ruleMap.get(Integer.parseInt(rulesToFollow[i]));
							if (ruleToFollow == null) {
								ruleToFollow = new Rule(Integer.parseInt(rulesToFollow[i]));
								ruleMap.put(ruleToFollow.ruleId, ruleToFollow);
							}
							rulesToAdd.add(ruleToFollow);
						}
					}
				} else {
					messagesToValidate.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

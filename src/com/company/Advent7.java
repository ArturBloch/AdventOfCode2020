package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Bag{

	String color;
	public HashMap<Bag, Integer> bagContainer = new HashMap<>();

	public Bag(String color) {
		this.color = color;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bag bag = (Bag) o;
		return Objects.equals(color, bag.color);
	}

	@Override public int hashCode() {
		return Objects.hash(color);
	}

	@Override public String toString() {
		return "Bag{" + "color='" + color + '\'' + '}';
	}
}


public class Advent7 {
	File file = new File("src/files/advent7.txt");

	HashMap<String, Bag> bagMap = new HashMap<>();

	public void run() {
		createBagRules();
		System.err.println(howManyBagsCanFit(new Bag("shiny gold")));
		Bag wantedBag = bagMap.get("shiny gold");
		int result = recursiveSum(bagMap.get("shiny gold"), 1);
		System.err.println(result);
	}

	// one star
	public int howManyBagsCanFit(Bag wantedBag){
		ArrayList<Bag> bagsVisited = new ArrayList<>(20);
		ArrayDeque<Bag> bagsToVisit = new ArrayDeque<>(20);
		int wantedBagHolders = 0;
		for (Bag bag : bagMap.values()) {
			if(bag.equals(wantedBag)) continue;;
			bagsVisited.add(bag);
			bagsToVisit.add(bag);
			while(!bagsToVisit.isEmpty()){
				Bag nextBag = bagsToVisit.pollFirst();
				for (Bag containedBag: nextBag.bagContainer.keySet()) {
					if(!bagsVisited.contains(containedBag)){
						bagsVisited.add(containedBag);
						bagsToVisit.add(containedBag);
					}
				}
				if(nextBag.equals(wantedBag)) {
					wantedBagHolders++;
					break;
				}
			}
			bagsToVisit.clear();
			bagsVisited.clear();
		}

		return wantedBagHolders;
	}

	//two star
	public int recursiveSum(Bag bag, int multiplier){
		System.err.println(bag);
		int amountOfBags = 0;
		int localSum = 0;
		for (Map.Entry<Bag, Integer> entry: bag.bagContainer.entrySet()) {
				amountOfBags += recursiveSum(entry.getKey(), multiplier * entry.getValue());
				localSum = localSum + (multiplier * entry.getValue());

		}
		return amountOfBags + localSum;
	}

	public void createBagRules(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String nextLine = br.readLine();
			String currentLine = "";
			while(nextLine != null){
				currentLine = nextLine;
				nextLine = br.readLine();
				String[] rule = currentLine.split(" ");
				String baseBagName = rule[0] + " " + rule[1];
				Bag baseBag = bagMap.get(baseBagName);
				if(baseBag == null){
					baseBag = new Bag(baseBagName);
					bagMap.put(baseBag.color, baseBag);
				}
				for (int i = 4; i < rule.length; i+=4) {
					if(rule[i].equals("no")) break;
					int numberOfBags = Integer.parseInt(rule[i]);
					String bagColor = rule[i+1] + " " + rule[i+2];
					Bag containerBag = bagMap.get(bagColor);
					if(containerBag == null) {
						containerBag = new Bag(bagColor);
						bagMap.put(bagColor, containerBag);
					}
					if(numberOfBags != 0) {
						baseBag.bagContainer.put(containerBag, numberOfBags);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

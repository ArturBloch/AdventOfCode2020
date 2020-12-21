package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Advent21 implements Runnable{

	File file = new File("src/files/advent21.txt");
	HashMap<String, Integer> ingredientCount = new HashMap<>();
	HashMap<String, ArrayList<String>> possibleAllergens = new HashMap<>();

	@Override public void run() {
		readFile();
		for (Map.Entry<String, ArrayList<String>> stringHashSetEntry : possibleAllergens.entrySet()) {
			System.err.println(stringHashSetEntry.getKey() + " : " + stringHashSetEntry.getValue().toString());
		}
		solve();
	}

	private void solve() {
		HashMap<String, String> knownAllergens = new HashMap<>();
		while(true){
			boolean found = false;
			for (Map.Entry<String, ArrayList<String>> stringHashSetEntry : possibleAllergens.entrySet()) {
				if(stringHashSetEntry.getValue().size() == 1 && !knownAllergens.containsKey(stringHashSetEntry.getKey())){
					found = true;
					knownAllergens.put(stringHashSetEntry.getKey(), stringHashSetEntry.getValue().get(0));
					for (ArrayList<String> otherIngredients : possibleAllergens.values()) {
						if(otherIngredients.size() > 1) {
							otherIngredients.removeIf(e -> e.equals(stringHashSetEntry.getValue().get(0)));
						}
					}
				}
			}
			if(!found) break;
		}
		int counter = 0;
		for (String s : ingredientCount.keySet()) {
			if(!knownAllergens.containsValue(s)) counter += ingredientCount.get(s);
		}
		System.err.println(counter);
		StringBuilder finalList = new StringBuilder();
		knownAllergens.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e -> finalList.append(e.getValue()).append(","));
		System.err.println(finalList);
	}

	public void readFile() {
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split(" \\(contains ");
				HashSet<String> ingredients = new HashSet<>();
				for (String ingredient : lineSplit[0].split(" ")) {
					ingredients.add(ingredient);
					ingredientCount.merge(ingredient, 1, (o,v) -> o + v);
				}
				String[] allergens = lineSplit[1].replace(")", "").split(", ");
				for (String allergen : allergens) {
					if(possibleAllergens.containsKey(allergen)){
						possibleAllergens.get(allergen).removeIf(e -> !ingredients.contains(e));
					} else {
						possibleAllergens.put(allergen, new ArrayList<>(ingredients));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

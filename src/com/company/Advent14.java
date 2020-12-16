package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class Advent14 implements Runnable {

	File file = new File("src/files/advent14.txt");
	HashMap<Long, Long> memoryLocations = new HashMap<>();

	public void readFileOneStar() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			String mask = "";
			while ((line = br.readLine()) != null) {
				String[] input = line.split(" ");
				if (input[0].equals("mask")) {
					mask = input[2];
				} else {
					long memoryLoc = Long.parseLong(input[0].substring(input[0].indexOf("[") + 1, input[0].indexOf("]")));
					BitSet value = BitSet.valueOf(new long[]{Long.parseLong(input[2])});
					System.err.println(memoryLoc);
					for (int i = 0; i < mask.length(); i++) {
						if (mask.charAt(i) == 'x') continue;
						if (mask.charAt(i) == '0') value.clear(mask.length() - 1 - i);
						if (mask.charAt(i) == '1') value.set(mask.length() - 1 - i);
					}
					long valueMasked = value.toLongArray()[0];
					System.err.println(valueMasked);
					memoryLocations.put(memoryLoc, valueMasked);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readFileTwoStar() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			String mask = "";
			while ((line = br.readLine()) != null) {
				String[] input = line.split(" ");
				if (input[0].equals("mask")) {
					mask = input[2];
				} else {
					ArrayDeque<BitSet> possibleMemLoc = new ArrayDeque<>(200);
					BitSet memoryLoc = BitSet.valueOf(
						new long[]{Long.parseLong(input[0].substring(input[0].indexOf("[") + 1, input[0].indexOf("]")))});
					for (int i = 0; i < mask.length(); i++) {
						if (mask.charAt(i) == '0') continue;
						if (mask.charAt(i) == '1') memoryLoc.set(mask.length() - 1 - i);
					}
					possibleMemLoc.add(memoryLoc);
					for (int i = 0; i < mask.length(); i++) {
						if (mask.charAt(i) == 'X') {
							ArrayList<BitSet> alternatives = new ArrayList<>();
							while(!possibleMemLoc.isEmpty()){
								BitSet nextBitSet = possibleMemLoc.pollFirst();
								BitSet alternative1 = (BitSet)nextBitSet.clone();
								BitSet alternative2 = (BitSet)nextBitSet.clone();
								alternative1.clear(mask.length() - 1 - i);
								alternative2.set(mask.length() - 1 - i);
								alternatives.add(alternative1);
								alternatives.add(alternative2);
							}
							possibleMemLoc.addAll(alternatives);
						}
					}
					Long value = Long.parseLong(input[2]);
					for (BitSet memLoc : possibleMemLoc) {
						memoryLocations.put(memLoc.toLongArray()[0], value);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override public void run() {
		readFileTwoStar();
		System.out.println(sumOfMemory());
	}

	private long sumOfMemory() {
		return memoryLocations.values().stream().reduce(0L, Long::sum);
	}


}

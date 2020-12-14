package com.company;

import java.awt.print.Pageable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Advent8 {

	private int accumulator = 0;
	File file = new File("src/files/advent8.txt");
	ArrayList<String> fileInput = new ArrayList<>();
	HashSet<Integer> commandsRead = new HashSet<>();
	public void run(){
		readFile();
		System.out.println(findAccumulatorBeforeInfiniteLoop()); // oneStar
		System.out.println(fixAccumulatorInfiniteLoop());
		System.out.println(tryAssembly(0, new HashSet<>(), new ArrayList<>(), 0, false)); // twoStar
	}

	// one star
	private int findAccumulatorBeforeInfiniteLoop() {
		ArrayList<Integer> triedFixing = new ArrayList<>();
		HashSet<Integer> commandsRead = new HashSet<>();
		for (int i = 0; i < fileInput.size(); i++) {
			System.err.println(fileInput.get(i));
			String[] commands = fileInput.get(i).split(" ");
			int value = Integer.parseInt(commands[1]);
			if(!commandsRead.add(i)){
				return accumulator;
			}
			if(commands[0].equals("acc")){
				accumulator += value;
			} else if(commands[0].equals("jmp")){
				i += (value - 1);

			} else if(commands[0].equals("nop")){

			}
		}
		return accumulator;
	}

	// two star alternative
	private int tryAssembly(int startinPosition, HashSet<Integer> visitedCommands, ArrayList<Integer> triedFixing, int accumulator,
	                              boolean tryingJump){
		HashSet<Integer> localVisitedCommands = new HashSet<>(visitedCommands);
		triedFixing = new ArrayList<>(triedFixing);
		for (int i = startinPosition; i < fileInput.size(); i++) {
			String[] commands = fileInput.get(i).split(" ");
			int value = Integer.parseInt(commands[1]);
			switch (commands[0]) {
				case "acc":
					accumulator += value;
					if(!localVisitedCommands.add(i)){
						return -1;
					}
					break;
				case "jmp":
					if (!triedFixing.contains(i) && !tryingJump) {
						triedFixing.add(i);
						int fixingResult = tryAssembly(i + 1, localVisitedCommands, triedFixing, accumulator, true);
						if(fixingResult != -1) {
							return fixingResult;
						}
					}
					if(!localVisitedCommands.add(i)){
						return -1;
					}
					i += (value - 1);
					break;
				case "nop":
					if(!localVisitedCommands.add(i)){
						return -1;
					}
					break;
			}
		}

		return accumulator;
	}


	//two star base
	private int fixAccumulatorInfiniteLoop() {
		ArrayList<Integer> triedFixing = new ArrayList<>();
		HashSet<Integer> commandsSnapshot = new HashSet<>();
		int lastCommand = -1;
		int tempAccumulator = -1;
		boolean tryingJump = false;
		for (int i = 0; i < fileInput.size(); i++) {
			String[] commands = fileInput.get(i).split(" ");
			int value = Integer.parseInt(commands[1]);
			switch (commands[0]) {
				case "acc":
					accumulator += value;
					if(!commandsRead.add(i)){
						commandsRead = commandsSnapshot;
						i = (lastCommand - 1);
						tryingJump = false;
						accumulator = tempAccumulator;
						continue;
					}
					break;
				case "jmp":
					if (!triedFixing.contains(i) && !tryingJump) {
						triedFixing.add(i);
						commandsSnapshot = new HashSet<>(commandsRead);
						lastCommand      = i;
						tempAccumulator = accumulator;
						tryingJump = true;
					} else {
						if(!commandsRead.add(i)){
							commandsRead = commandsSnapshot;
							i = (lastCommand - 1);
							tryingJump = false;
							accumulator = tempAccumulator;
							continue;
						}
						i += (value - 1);
					}
					break;
				case "nop":
					if(!commandsRead.add(i)){
						commandsRead = commandsSnapshot;
						i = (lastCommand - 1);
						tryingJump = false;
						accumulator = tempAccumulator;
						continue;
					}
					break;
			}
		}


		return accumulator;
	}


	private void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null){
				fileInput.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

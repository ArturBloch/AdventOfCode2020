package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Advent10 implements Runnable{

	File file = new File("src/files/advent10.txt");
	ArrayList<Long> adapters = new ArrayList<>();
	int diff1 = 0;
	int diff2 = 0;
	int diff3 = 0;


	@Override public void run() {
		readFile();
		adapters.add(0L);
		adapters.sort(Long::compareTo);
		adapters.add(adapters.get(adapters.size() - 1) + 3);
		findJoltageDifferences();
		long[] memory = new long[adapters.size()];
		long arrangements = countArrangements(0, memory);
		System.out.println("Arrangements: " + arrangements);
	}

	public void readFile(){
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null){
				adapters.add(Long.parseLong(line));

			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public long countArrangements(int currentIndex, long[] memory){
		if(memory[currentIndex] != 0) return memory[currentIndex];
		if(currentIndex == adapters.size() - 1) return 1;

		long possibleOptions = 0;
		for (int i = currentIndex + 1; i < adapters.size(); i++) {
			if(adapters.get(i) - adapters.get(currentIndex) <= 3){
				 possibleOptions += countArrangements(i, memory);
			}
		}
		memory[currentIndex] = possibleOptions;
		return possibleOptions;
	}

	public void findJoltageDifferences(){
		long prevAdapter = 0;
		for (Long adapter : adapters) {
			long diff = adapter - prevAdapter;
			if(diff == 1) diff1++;
			if(diff == 2) diff2++;
			if(diff == 3) diff3++;
			prevAdapter = adapter;
		}
		diff3++; // built in adapter
		System.out.println(diff1 * diff3);
	}
}

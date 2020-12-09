package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Advent3 {

	File file = new File("src/files/advent3.txt");
	ArrayList<String> mountain = new ArrayList<>(324);
	public void run(){
		readFile();
		twoStar();
	}

	private void readFile(){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null){
				mountain.add(line);
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void oneStar(){
		goDownHill(1, 3);
	}

	//important that goDownHill returns longs otherwise the multiplication on ints goes overflow
	private void twoStar(){
		long result = goDownHill(1,1)
		* goDownHill(3,1)
		* goDownHill(5,1)
		* goDownHill(7,1)
		* goDownHill(1, 2);
		System.out.println(result);
	}

	// one star
	private long goDownHill(int rightMove, int downMove) {
		int position = 0;
		long treeCounter = 0;
		int mountainWidth = mountain.get(0).length();
		for (int i = 0; i < mountain.size(); i += downMove) {
			String line = mountain.get(i);
			if(line.charAt(position) == '#') treeCounter++;
			position = (position + rightMove) % mountainWidth;
		}

		System.out.println(treeCounter);
		return treeCounter;
	}
}

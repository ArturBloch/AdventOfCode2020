package com.company;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class Advent15 implements Runnable {


	public ArrayList<Long> startingNumbers = new ArrayList<>();
	public File file = new File("src/files/advent15.txt");
	public HashMap<Long, Number> numberMap = new LinkedHashMap<>(300000);

	class Number {
		long number;
		long lastTurn;
		long secondToLastTurn;

		public Number(long number) {
			this.number           = number;
			this.lastTurn         = 0;
			this.secondToLastTurn = -1;
		}

		public Number(long number, long turn) {
			this.number           = number;
			this.lastTurn         = turn;
			this.secondToLastTurn = -1;
		}
	}

	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(",");
				for (String number : numbers) {
					long result = Long.parseLong(number);
					startingNumbers.add(result);
					numberMap.put(result, new Number(Integer.parseInt(number)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override public void run() {
		readFile();
		long timer = System.nanoTime();
		playGameOneStar();
		long end = System.nanoTime();
		System.err.println((double)(end - timer) / 1000000);
	}

	private void playGameOneStar() {

		long turn = 0;
		long lastNumber = -1;
		for (Number value : numberMap.values()) {
			value.lastTurn = turn;
			lastNumber     = value.number;
			turn++;
		}
		System.err.println("entering");
		while (turn < 30000000) {
			if (numberMap.get(lastNumber).secondToLastTurn == -1) {
				lastNumber = 0L;
				if(numberMap.containsKey(lastNumber)){
					Number newResult = numberMap.get(lastNumber);
					newResult.secondToLastTurn = newResult.lastTurn;
					newResult.lastTurn = turn;
				} else {
					numberMap.putIfAbsent(lastNumber, new Number(lastNumber, turn));
				}
			} else {
				Number lastResult = numberMap.get(lastNumber);
				lastNumber = lastResult.lastTurn - lastResult.secondToLastTurn;
				if(numberMap.containsKey(lastNumber)){
					Number newResult = numberMap.get(lastNumber);
					newResult.secondToLastTurn = newResult.lastTurn;
					newResult.lastTurn = turn;
				} else {
					numberMap.putIfAbsent(lastNumber, new Number(lastNumber, turn));
				}
			}

			turn++;
		}
		System.err.println(lastNumber);
	}
}

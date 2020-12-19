package com.company;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Advent15 implements Runnable {


	public ArrayList<Integer> startingNumbers = new ArrayList<>();
	public File file = new File("src/files/advent15.txt");
	public Number[] numbers = new Number[30000000];

	class Number {
		int number;
		int lastTurn;
		int secondToLastTurn;

		public Number(int number) {
			this.number           = number;
			this.lastTurn         = 0;
			this.secondToLastTurn = -1;
		}

		public Number(int number, int turn) {
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
				String[] input = line.split(",");
				for (String number : input) {
					int result = Integer.parseInt(number);
					startingNumbers.add(result);
					numbers[result] = new Number(Integer.parseInt(number));
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

		int turn = 0;
		int lastNumber = -1;
		for (int value : startingNumbers) {
			numbers[value].lastTurn = turn;
			lastNumber     = value;
			turn++;
		}
		System.err.println("entering");
		while (turn < 30000000) {
			if (numbers[lastNumber].secondToLastTurn == -1) {
				lastNumber = 0;
				if(numbers[lastNumber] != null){
					Number newResult = numbers[lastNumber];
					newResult.secondToLastTurn = newResult.lastTurn;
					newResult.lastTurn = turn;
				} else {
					numbers[lastNumber] = new Number(lastNumber, turn);
				}
			} else {
				Number lastResult = numbers[lastNumber];
				lastNumber = lastResult.lastTurn - lastResult.secondToLastTurn;
				if(numbers[lastNumber] != null){
					Number newResult = numbers[lastNumber];
					newResult.secondToLastTurn = newResult.lastTurn;
					newResult.lastTurn = turn;
				} else {
					numbers[lastNumber] = new Number(lastNumber, turn);
				}
			}

			turn++;
		}
		System.err.println(lastNumber);
	}
}

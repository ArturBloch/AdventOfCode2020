package com.company;

import java.io.*;
import java.util.*;

public class Advent23 implements Runnable {

	File file = new File("src/files/advent23.txt");
	int[] cupConnections = new int[1000001];
	int[] pickedUp = new int[3];
	int currentCup;
	int destinationCup;
	int minNumber;
	int maxNumber;
	public static final int NUMBER_OF_ITERATIONS = 100;
	public static final int MAX_NUMBER = 1000000;

	@Override public void run() {
		readFile();
		int counter = 0;

		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			pickedUp[0] = cupConnections[currentCup];
			pickedUp[1] = cupConnections[pickedUp[0]];
			pickedUp[2] = cupConnections[pickedUp[1]];
			cupConnections[currentCup] = cupConnections[pickedUp[2]];
			destinationCup = currentCup - 1;
			while (destinationCup == pickedUp[0] || destinationCup == pickedUp[1] || destinationCup == pickedUp[2] ||
			       destinationCup < minNumber) {
				destinationCup--;
				if (destinationCup < minNumber) {
					destinationCup = maxNumber;
				}
			}
			cupConnections[pickedUp[2]] = cupConnections[destinationCup];
			cupConnections[destinationCup] = pickedUp[0];
			currentCup = cupConnections[currentCup];
			int nextCup = 1;
			while (cupConnections[nextCup] != 1) {
				nextCup = cupConnections[nextCup];
			}

		}
		System.err.println("PRINT ");
		int nextCup = 1;
		System.err.print(" 1 ");
		while (cupConnections[nextCup] != 1) {
			System.err.print(cupConnections[nextCup] + " ");
			nextCup = cupConnections[nextCup];
		}
	}

	public void readFile() {
		int lastCup = -1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
					cupConnections = new int[line.length() + 1];
					for (int i = 0; i < line.length() - 1; i++) {
						int thisNumber = Integer.parseInt(line.charAt(i) + "");
						int nextNumber = Integer.parseInt((line.charAt(i + 1)) + "");
						if (i == 0) currentCup = thisNumber;
						cupConnections[thisNumber] = nextNumber;
						lastCup = nextNumber;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		cupConnections[lastCup] = currentCup;
		minNumber = Arrays.stream(cupConnections).filter(e -> e != 0).min().getAsInt();
		maxNumber = Arrays.stream(cupConnections).max().getAsInt();
		System.err.println(minNumber);
		System.err.println(maxNumber);
//		cupConnections[lastCup] = maxNumber + 1;
//		cupConnections[MAX_NUMBER] = currentCup;
//		for (int i = maxNumber + 1; i < MAX_NUMBER; i++) {
//			cupConnections[i] = i + 1;
//		}
//		maxNumber = MAX_NUMBER;

	}
}

package com.company;

import java.io.*;
import java.util.*;

public class Advent23 implements Runnable {

	File file = new File("src/files/advent23.txt");
	int[] cupConnections = new int[MAX_NUMBER + 1];
	int[] pickedUp = new int[3];
	int currentCup;
	int destinationCup;
	int minNumber;
	int maxNumber;
	public static final int NUMBER_OF_ITERATIONS = 10000000;
	public static final int MAX_NUMBER = 1000000;

	@Override public void run() {
		readFile();
		int counter = 0;
		long start = System.nanoTime();
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
		}
		long end = System.nanoTime();
		System.err.println((end - start) / 1000000 + " ms");

		System.err.println("PRINT ");
		System.err.print(" 1 ");
		long result = cupConnections[1];
		System.err.println("First following " + result);
		result = result * cupConnections[(int)result];
		System.err.println(result);
	}

	public void readFile() {
		int lastCup = -1;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
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
		cupConnections[MAX_NUMBER] = currentCup;
		minNumber = Arrays.stream(cupConnections).filter(e -> e != 0).min().getAsInt();
		maxNumber = Arrays.stream(cupConnections).max().getAsInt();
		cupConnections[lastCup] = maxNumber + 1;
		for (int i = maxNumber + 1; i < MAX_NUMBER; i++) {
			cupConnections[i] = i + 1;
		}
		maxNumber = MAX_NUMBER;

	}
}

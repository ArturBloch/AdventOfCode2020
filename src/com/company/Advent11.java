package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Advent11 implements Runnable {

	File file = new File("src/files/advent11.txt");
	ArrayList<String> seatMap = new ArrayList<>();

	@Override public void run() {
		readFile();
		int result = 1;
		while (result > 0) {
			result = cellularAutomataTwoStar();
		}
		int seatCounter = 0;
		for (int i = 0; i < seatMap.size(); i++) {
			for (int j = 0; j < seatMap.get(0).length(); j++) {
				if (seatMap.get(i).charAt(j) == '#') seatCounter++;
			}
		}
		System.out.println(seatCounter);
	}

	private boolean viableSeat(int y, int x) {
		return y >= 0 && x >= 0 && y < seatMap.size() && x < seatMap.get(0).length();

	}

	private int nearbyOccupiedSeats(int y, int x) {
		int counter = 0;
		for (int i = y - 1; i <= y + 1; i++) {
			for (int j = x - 1; j <= x + 1; j++) {
				if (!viableSeat(i, j)) continue;
				if (!(i == y && j == x) && seatMap.get(i).charAt(j) == '#') {
					counter++;
				}
			}
		}
		return counter;
	}

	private int nearbyStarOccupiedSeats(int y, int x) {
		int counter = 0;
		if (occupiedViableSeat(y, x, 1, 0)) counter++;
		if (occupiedViableSeat(y, x, 0, 1)) counter++;
		if (occupiedViableSeat(y, x, -1, 0)) counter++;
		if (occupiedViableSeat(y, x, 0, -1)) counter++;
		if (occupiedViableSeat(y, x, 1, 1)) counter++;
		if (occupiedViableSeat(y, x, -1, -1)) counter++;
		if (occupiedViableSeat(y, x, 1, -1)) counter++;
		if (occupiedViableSeat(y, x, -1, 1)) counter++;
		return counter;
	}

	private boolean occupiedViableSeat(int y, int x, int modifierY, int modifierX) {
		y += modifierY;
		x += modifierX;
		while (viableSeat(y, x)) {
			if (seatMap.get(y).charAt(x) == '#') return true;
			if (seatMap.get(y).charAt(x) == 'L') return false;
			y += modifierY;
			x += modifierX;
		}

		return false;
	}

	private int cellularAutomataOneStar() {
		ArrayList<String> newSeatMap = new ArrayList<>();
		int changed = 0;
		for (int i = 0; i < seatMap.size(); i++) {
			StringBuilder newSeatLine = new StringBuilder();
			for (int j = 0; j < seatMap.get(i).length(); j++) {
				int occupiedNearbySeats = nearbyOccupiedSeats(i, j);
				if (seatMap.get(i).charAt(j) == 'L' && occupiedNearbySeats == 0) {
					newSeatLine.append('#');
					changed++;
				} else if (seatMap.get(i).charAt(j) == '#' && occupiedNearbySeats >= 4) {
					newSeatLine.append('L');
					changed++;
				} else {
					newSeatLine.append(seatMap.get(i).charAt(j));
				}
			}
			newSeatMap.add(newSeatLine.toString());
		}

		seatMap = newSeatMap;
		return changed;
	}

	private int cellularAutomataTwoStar() {
		ArrayList<String> newSeatMap = new ArrayList<>();
		int changed = 0;
		for (int i = 0; i < seatMap.size(); i++) {
			StringBuilder newSeatLine = new StringBuilder();
			for (int j = 0; j < seatMap.get(i).length(); j++) {
				int occupiedNearbySeats = nearbyStarOccupiedSeats(i, j);
				if (seatMap.get(i).charAt(j) == 'L' && occupiedNearbySeats == 0) {
					newSeatLine.append('#');
					changed++;
				} else if (seatMap.get(i).charAt(j) == '#' && occupiedNearbySeats >= 5) {
					newSeatLine.append('L');
					changed++;
				} else {
					newSeatLine.append(seatMap.get(i).charAt(j));
				}
			}
			newSeatMap.add(newSeatLine.toString());
		}

		seatMap = newSeatMap;
		return changed;
	}

	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				seatMap.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}

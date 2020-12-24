package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Advent24 implements Runnable {

	File file = new File("src/files/advent24.txt");
	int[][] hexMap = new int[10000][10000];
	int startingY = hexMap.length / 2;
	int startingX = hexMap.length / 2;
	int automataCounter = 0;

	@Override public void run() {
//		initializeMap();
		readFile();
		solve();
	}

	private void solve() {
		int whiteCounter = 0;
		int blackCounter = 0;

		countBlackAndWhite(hexMap);

		for (int i = 0; i < 100; i++) {
			hexMap = tileAutomata(hexMap);
		}
	}

	private void countBlackAndWhite(int[][] map){
		int whiteCounter = 0;
		int blackCounter = 0;
		for (int i = 0; i < hexMap.length; i++) {
			for (int j = 0; j < hexMap.length; j++) {
				if (map[i][j] == 0) whiteCounter++;
				if(map[i][j] == 1) blackCounter++;
			}
		}

		System.err.println("Black " + blackCounter);
		System.err.println("White " + whiteCounter);
	}

	private int[][] tileAutomata(int[][] map) {
		automataCounter++;
		int[][] newTileMap = new int[map.length][map.length];
		for (int i = 1; i < hexMap.length - 1; i++) {
			for (int j = 1; j < hexMap.length - 1; j++) {
				int neighbours = countBlackNeighbours(i,j, map);
				if(map[i][j] == 1 && (neighbours == 0 || neighbours > 2)){
					newTileMap[i][j] = 0;
				} else if(map[i][j] == 0 && neighbours == 2){
					newTileMap[i][j] = 1;
				} else {
					newTileMap[i][j] = map[i][j];
				}
			}
		}
		System.err.println("Day " + automataCounter);
		countBlackAndWhite(newTileMap);
		return newTileMap;
	}

	private int countBlackNeighbours(int y, int x, int[][] map){
		int counter = 0;
		if(map[y][x - 1] == 1) counter++;
		if(map[y][x + 1] == 1) counter++;
		if(map[y + 1][x] == 1) counter++;
		if(map[y + 1][x - 1] == 1) counter++;
		if(map[y - 1][x] == 1) counter++;
		if(map[y - 1][x + 1] == 1) counter++;

		return counter;
	}


	private void initializeMap() {
		for (int i = 0; i < hexMap.length; i++) {
			for (int j = 0; j < hexMap.length; j++) {
				hexMap[i][j] = -1;
			}
		}
	}


	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) continue;
				flipTile(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void flipTile(String line) {
		int y = startingY;
		int x = startingX;
		for (int i = 0; i < line.length(); i++) {
			switch (line.charAt(i)) {
				case 'w' -> {
					x = x - 1;
				}
				case 'e' -> {
					x = x + 1;
				}
				case 'n' -> {
					i++;
					if (line.charAt(i) == 'w') {
						y = y - 1;
					} else if (line.charAt(i) == 'e') {
						x = x + 1;
						y = y - 1;
					}
				}
				case 's' -> {
					i++;
					if (line.charAt(i) == 'w') {
						y = y + 1;
						x = x - 1;
					} else if (line.charAt(i) == 'e') {
						y = y + 1;
					}
				}
			}
		}
		if(hexMap[y][x] == -1) hexMap[y][x] = 0;
		hexMap[y][x] = 1 - hexMap[y][x];
	}
}

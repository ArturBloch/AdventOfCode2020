package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Advent17 implements Runnable {

	char[][][][] hyperCube;

	File file = new File("src/files/advent17.txt");
	int cycles;

	ArrayList<String> cubeStarter;

	@Override public void run() {
		readFile();
		createCube();
		for (int i = 0; i < 6; i++) {
			hyperCube = cubeCellAutomata(hyperCube);
		}
		System.out.println(countAlive());
	}


	public int countAlive() {
		int aliveCounter = 0;
		for (int z = 0; z < hyperCube[0][0][0].length; z++) {
			for (int w = 0; w < hyperCube[0][0].length; w++) {
				for (int y = 0; y < hyperCube.length; y++) {
					for (int x = 0; x < hyperCube.length; x++) {
						if (hyperCube[y][x][w][z] == '#') aliveCounter++;
					}
				}
			}
		}
		return aliveCounter;
	}

	private void createCube() {
		hyperCube = new char[cubeStarter.size()][cubeStarter.size()][1][1];
		for (int i = 0; i < cubeStarter.size(); i++) {
			for (int j = 0; j < cubeStarter.get(i).length(); j++) {
				hyperCube[i][j][0][0] = cubeStarter.get(i).charAt(j);
			}
		}
	}

	private char[][][][] cubeCellAutomata(char[][][][] prevCubeHolder) {
		char[][][][] newCubeHolder = new char[prevCubeHolder.length + 2][prevCubeHolder[0].length + 2][prevCubeHolder[0][0].length + 2][
			                             prevCubeHolder[0][0][0].length + 2];
		char[][][][] tempCubeHolder = new char[prevCubeHolder.length + 2][prevCubeHolder[0].length + 2][prevCubeHolder[0][0].length + 2][
			                              prevCubeHolder[0][0][0].length + 2];
		for (int z = 0; z < tempCubeHolder[0][0][0].length; z++) {
			for (int w = 0; w < tempCubeHolder[0][0].length; w++) {
				for (int y = 0; y < tempCubeHolder.length; y++) {
					for (int x = 0; x < tempCubeHolder.length; x++) {
						if ((z > 0 && z < tempCubeHolder[0][0][0].length - 1) && (w > 0 && w < tempCubeHolder[0][0].length - 1) &&
						    (y > 0 && y < tempCubeHolder.length - 1) && (x > 0 && x < tempCubeHolder.length - 1)) {
							tempCubeHolder[y][x][w][z] = prevCubeHolder[y - 1][x - 1][w - 1][z - 1];
						} else {
							tempCubeHolder[y][x][w][z] = '.';
						}
					}
				}
			}
		}

		for (int z = 0; z < tempCubeHolder[0][0][0].length; z++) {
			for (int w = 0; w < tempCubeHolder[0][0].length; w++) {
				for (int y = 0; y < tempCubeHolder.length; y++) {
					for (int x = 0; x < tempCubeHolder.length; x++) {
						int activeNeighbours = countNeighbours(y, x, w, z, prevCubeHolder);
						if (tempCubeHolder[y][x][w][z] == '#') {
							if (activeNeighbours == 2 || activeNeighbours == 3) {
								newCubeHolder[y][x][w][z] = '#';
							} else {
								newCubeHolder[y][x][w][z] = '.';
							}
						} else if (tempCubeHolder[y][x][w][z] == '.') {
							if (activeNeighbours == 3) {
								newCubeHolder[y][x][w][z] = '#';
							} else {
								newCubeHolder[y][x][w][z] = '.';
							}
						}
					}
				}
			}
		}
		printCube(newCubeHolder);
		return newCubeHolder;
	}

	private int countNeighbours(int y, int x, int w, int z, char[][][][] prevCubeHolder) {
		int activeNeighbour = 0;
		int neighboursCounted = 0;
		for (int i = -1; i <= 1; i++) {
			for (int c = -1; c <= 1; c++) {
				for (int j = -1; j <= 1; j++) {
					for (int k = -1; k <= 1; k++) {
						int resultZ = z + i - 1;
						int resultY = y + j - 1;
						int resultX = x + k - 1;
						int resultW = w + c - 1;
						if (resultZ + 1 == z && resultY + 1 == y && resultX + 1 == x && resultW + 1 == w) continue;
						if ((resultZ < 0 || resultZ >= prevCubeHolder[0][0][0].length) ||
						    (resultW < 0 || resultW >= prevCubeHolder[0][0].length) || (resultY < 0 || resultY >= prevCubeHolder.length) ||
						    (resultX < 0 || resultX >= prevCubeHolder.length)) continue;
						if (prevCubeHolder[resultY][resultX][resultW][resultZ] == '#') activeNeighbour++;
						neighboursCounted++;
					}
				}
			}
		}
		return activeNeighbour;
	}

	public void printCube(char[][][][] cubeToPrint) {
		System.err.println();
		for (int z = 0; z < cubeToPrint[0][0][0].length; z++) {
			for (int w = 0; w < cubeToPrint[0][0].length; w++) {
				System.err.println("Dimension " + (z - 1));
				for (int y = 0; y < cubeToPrint.length; y++) {
					System.err.println();
					for (int x = 0; x < cubeToPrint.length; x++) {
						System.err.print(cubeToPrint[y][x][z]);
					}
				}
				System.err.println();
			}
		}
	}

	public void readFile() {
		BufferedReader br;
		cubeStarter = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				cubeStarter.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
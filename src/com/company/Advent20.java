package com.company;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Advent20 implements Runnable {

	public HashMap<Integer, Tile> tiles = new HashMap<>();
	File file = new File("src/files/advent20.txt");
	public int borderLength = 10;
	char[][] image;

	class Tile {
		int id;
		Tile leftNeighbour;
		Tile rightNeighbour;
		Tile southNeighbour;
		Tile northNeighbour;
		char[][] pixels = new char[borderLength][borderLength];

		public Tile(int id) {
			this.id        = id;
			leftNeighbour  = null;
			rightNeighbour = null;
			southNeighbour = null;
			northNeighbour = null;
		}

		public void printTile() {
			System.err.println("TILE ID: " + id);
			for (int i = 0; i < borderLength; i++) {
				if (i != 0) System.err.println();
				for (int j = 0; j < borderLength; j++) {
					System.err.print(pixels[i][j]);
				}
			}
			System.err.println();
		}

		public boolean compareAndAssignBorders(Tile tile) {
			if (leftNeighbour == null && tile.rightNeighbour == null) {
				if (compareBorder(tile, 0, 0, 1, 0)) {
					leftNeighbour       = tile;
					tile.rightNeighbour = this;
					System.err.println(this.id + " found left " + tile.id);
					return true;
				}
			}
			if (rightNeighbour == null && tile.leftNeighbour == null) {
				if (compareBorder(tile, 0, borderLength - 1, 1, 0)) {
					rightNeighbour     = tile;
					tile.leftNeighbour = this;
					System.err.println(this.id + " found right " + tile.id);
					return true;
				}
			}
			if (southNeighbour == null && tile.northNeighbour == null) {
				if (compareBorder(tile, borderLength - 1, 0, 0, 1)) {
					southNeighbour      = tile;
					tile.northNeighbour = this;
					System.err.println(this.id + " found south " + tile.id);
					return true;
				}
			}
			if (northNeighbour == null && tile.southNeighbour == null) {
				if (compareBorder(tile, 0, 0, 0, 1)) {
					northNeighbour      = tile;
					tile.southNeighbour = this;
					System.err.println(this.id + " found north " + tile.id);
					return true;
				}
			}
			return false;
		}

		public int neighbourCount() {
			int neighbours = 0;
			if (southNeighbour != null) neighbours++;
			if (leftNeighbour != null) neighbours++;
			if (rightNeighbour != null) neighbours++;
			if (northNeighbour != null) neighbours++;
			return neighbours;
		}

		public boolean compareBorder(Tile tile, int y, int x, int yDelta, int xDelta) {
			boolean accepted = true;
			if (yDelta != 0 && xDelta != 0) {
				System.err.println("ERROR");
				return false;
			}

			for (int i = 0; i < borderLength; i++) {
				if (yDelta != 0) {
					if (this.pixels[y + i][x] != tile.pixels[y + i][borderLength - 1 - x]) accepted = false;
				} else if (xDelta != 0) {
					if (this.pixels[y][x + i] != tile.pixels[borderLength - 1 - y][x + i]) accepted = false;
				}
			}

			return accepted;
		}

		public void rotate() {
			char[][] result = new char[borderLength][borderLength];
			for (int i = 0; i < borderLength; ++i) {
				for (int j = 0; j < borderLength; ++j) {
					result[i][j] = pixels[borderLength - j - 1][i];
				}
			}
			pixels = result;
		}

		public void resetNeighbours() {
			if (northNeighbour != null) {
				northNeighbour.southNeighbour = null;
				northNeighbour                = null;
			}

			if (southNeighbour != null) {
				southNeighbour.northNeighbour = null;
				southNeighbour                = null;
			}

			if (leftNeighbour != null) {
				leftNeighbour.rightNeighbour = null;
				leftNeighbour                = null;
			}

			if (rightNeighbour != null) {
				rightNeighbour.leftNeighbour = null;
				rightNeighbour               = null;
			}

		}

		public void flip() {
			Advent20.this.flip(pixels);
		}

		public void mirror() {
			Advent20.this.mirror(pixels);
		}
	}

	@Override public void run() {
		readFile();
		createTileMap();
		// ONE STAR
		long sumOfCorners = multiplyCornersOfPicture();
		//		System.out.println(sumOfCorners);
		assert (correctPicture());
		// TWO STAR
		assemblePicture();
		System.out.println(findSeaMonsters());
		System.out.println(calculateRougness());
	}

	private int calculateRougness() {
		int counter = 0;
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image.length; j++) {
				if(image[i][j] == '#') counter++;
			}
		}
		return counter;
	}

	private int findSeaMonsters() {
		int countMonsters = 0;
		boolean searching = true;
		while (searching) {
			int monsters = countMonsters();
			if (monsters == 0) {
				flip(image);
				System.err.println("flipped");
				monsters = countMonsters();
				if (monsters == 0) {
					mirror(image);
					System.err.println("flipped and mirrored");
					monsters = countMonsters();
					if (monsters == 0) {
						mirror(image);
						flip(image);
						mirror(image);
						System.err.println("just mirrored");
						monsters = countMonsters();
						if (monsters == 0) {
							mirror(image);
							System.err.println("rotated");
							image = rotate(image);
							printImage();
						}
					}
				}
			}
			if (monsters != 0) {
				return monsters;
			}
		}
		return countMonsters;
	}

	private int countMonsters() {
		int monsters = 0;
		for (int y = 0; y < image.length; y++) {
			for (int x = 0; x < image.length; x++) {
				if (validCell(y + 1, x + 16) && validCell(y - 1, x + 18) && validCell(y, x + 19)) {
					boolean isDragon =
						image[y][x] == '#' && image[y + 1][x + 1] == '#' && image[y + 1][x + 4] == '#' && image[y][x + 5] == '#' &&
						image[y][x + 6] == '#' && image[y + 1][x + 7] == '#' && image[y + 1][x + 10] == '#' && image[y][x + 11] == '#' &&
						image[y][x + 12] == '#' && image[y + 1][x + 13] == '#' && image[y + 1][x + 16] == '#' && image[y][x + 17] == '#' &&
						image[y][x + 18] == '#' && image[y][x + 19] == '#' && image[y - 1][x + 18] == '#';
					if (isDragon) {
						image[y][x]          = 'd';
						image[y + 1][x + 1]  = 'd';
						image[y + 1][x + 4]  = 'd';
						image[y][x + 5]      = 'd';
						image[y][x + 6]      = 'd';
						image[y + 1][x + 7]  = 'd';
						image[y + 1][x + 10] = 'd';
						image[y][x + 11]     = 'd';
						image[y][x + 12]     = 'd';
						image[y + 1][x + 13] = 'd';
						image[y + 1][x + 16] = 'd';
						image[y][x + 17]     = 'd';
						image[y][x + 18]     = 'd';
						image[y][x + 19]     = 'd';
						image[y - 1][x + 18] = 'd';
						monsters++;
					}
				}
			}
		}
		return monsters;
	}


	private boolean validCell(int y, int x) {
		return y < image.length && y > 0 && x > 0 && x < image.length;
	}


	private void assemblePicture() {
		Tile topLeftTile = null;
		for (Tile value : tiles.values()) {
			if (value.northNeighbour == null && value.leftNeighbour == null) {
				topLeftTile = value;
				break;
			}
		}
		assert topLeftTile != null;
		int imageSize = (int) ((Math.sqrt(tiles.size())) * (topLeftTile.pixels.length - 2) + 1);
		System.err.println(imageSize + " imageSize");
		image = new char[imageSize][imageSize];
		System.err.println(topLeftTile.id + " TOP LEFT STARTER");
		topLeftTile.printTile();
		Tile rowTile = topLeftTile;
		int y = 0;
		int x = 0;
		while (rowTile != null) {
			Tile columnTile = rowTile;
			int counter = 1;
			while (columnTile != null) {
				System.err.println(columnTile.id);
				for (int i = 1; i < columnTile.pixels.length - 1; i++) {
					for (int j = 1; j < columnTile.pixels.length - 1; j++) {
						image[y + i - 1][x + j - 1] = columnTile.pixels[i][j];

					}
				}
				x = (borderLength - 2) * counter;
				counter++;
				columnTile = columnTile.rightNeighbour;
			}

			y += (borderLength - 2);
			x       = 0;
			rowTile = rowTile.southNeighbour;
		}

		printImage();
	}

	private void printImage() {
		System.err.println("IMADŻ");
		System.err.println(image.length + " LENGTH");
		for (int i = 0; i < image.length; i++) {
			System.err.println();
			for (int j = 0; j < image.length; j++) {
				System.err.print(image[i][j]);
				if ((j + 1) % borderLength == 0 && j != 0) System.err.print(" ");
			}
			if ((i + 1) % borderLength == 0 && i != 0) System.err.println();
		}
		System.err.println("END IMADŻ");
	}

	private long multiplyCornersOfPicture() {
		long multiplied = 1;
		for (Tile value : tiles.values()) {
			int neighbourCount = 0;
			if (value.southNeighbour != null) neighbourCount++;
			if (value.leftNeighbour != null) neighbourCount++;
			if (value.rightNeighbour != null) neighbourCount++;
			if (value.northNeighbour != null) neighbourCount++;
			if (neighbourCount == 2) {
				multiplied *= value.id;
			}
		}
		return multiplied;
	}

	private void createTileMap() {
		ArrayDeque<Tile> tilesToProcess = new ArrayDeque<>();
		ArrayList<Tile> tilesProcessed = new ArrayList<>();
		Tile starterTile = (Tile) tiles.values().toArray()[new Random().nextInt(tiles.size())];
		System.err.println("starter tile " + starterTile.id);
		tilesToProcess.add(starterTile);
		while (!tilesToProcess.isEmpty()) {
			Tile firstTile = tilesToProcess.removeFirst();
			int counter = 0;
			System.err.println(firstTile.id);
			for (Tile secondTile : tiles.values()) {
				if (firstTile.neighbourCount() == 4) break;
				if (firstTile.id == secondTile.id) continue;
				for (int i = 0; i < 4; i++) {
					boolean found = firstTile.compareAndAssignBorders(secondTile);
					if (found) {
						if (!tilesProcessed.contains(secondTile)) {
							tilesToProcess.add(secondTile);
							tilesProcessed.add(secondTile);
						}
						counter++;
						break;
					}
					if (tilesProcessed.contains(secondTile)) break;
					secondTile.flip();
					found = firstTile.compareAndAssignBorders(secondTile);
					if (found) {
						if (!tilesProcessed.contains(secondTile)) {
							tilesToProcess.add(secondTile);
							tilesProcessed.add(secondTile);
						}
						counter++;
						break;
					}
					secondTile.flip();


					secondTile.mirror();
					found = firstTile.compareAndAssignBorders(secondTile);
					if (found) {
						if (!tilesProcessed.contains(secondTile)) {
							tilesToProcess.add(secondTile);
							tilesProcessed.add(secondTile);
						}
						counter++;
						break;
					}
					secondTile.mirror();

					secondTile.rotate();
					found = firstTile.compareAndAssignBorders(secondTile);
					if (found) {
						if (!tilesProcessed.contains(secondTile)) {
							tilesToProcess.add(secondTile);
							tilesProcessed.add(secondTile);
						}
						counter++;
						break;
					}
				}
			}
		}
	}

	public boolean correctPicture() {
		int corners = 0;
		int rest = 0;
		for (Tile value : tiles.values()) {
			int neighbours = value.neighbourCount();
			if (neighbours == 2) corners++;
			if (neighbours > 2) rest++;
		}
		return corners == 4 && (corners + rest) == tiles.size();
	}

	public void readFile() {
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			Tile currentTile = null;
			int row = 0;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) continue;
				if (line.contains("Tile")) {
					currentTile = new Tile(Integer.parseInt(line.split(" ")[1].replace(":", "")));
					tiles.put(currentTile.id, currentTile);
					row = 0;
					continue;
				}
				if (currentTile != null) {
					for (int i = 0; i < line.length(); i++) {
						currentTile.pixels[row][i] = line.charAt(i);
					}

					row++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public char[][] rotate(char[][] arrayToRotate) {
		char[][] result = new char[arrayToRotate.length][arrayToRotate.length];
		for (int i = 0; i < arrayToRotate.length; ++i) {
			for (int j = 0; j < arrayToRotate.length; ++j) {
				result[i][j] = arrayToRotate[arrayToRotate.length - j - 1][i];
			}
		}
		return result;
	}

	public void flip(char[][] arrayToFlip) {
		for (int i = 0; i < (arrayToFlip.length / 2); i++) {
			char[] temp = arrayToFlip[i];
			arrayToFlip[i]                          = arrayToFlip[arrayToFlip.length - i - 1];
			arrayToFlip[arrayToFlip.length - i - 1] = temp;
		}
	}

	public void mirror(char[][] arrayToRotate) {
		for (int i = 0; i < (arrayToRotate.length / 2); i++) {
			char[] temp = arrayToRotate[i];
			arrayToRotate[i]                            = arrayToRotate[arrayToRotate.length - i - 1];
			arrayToRotate[arrayToRotate.length - i - 1] = temp;
		}
	}
}
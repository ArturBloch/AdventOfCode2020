package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Advent12 implements Runnable{

	File file = new File("src/files/advent12.txt");
	ArrayList<String> commands = new ArrayList<>();
	Direction waypointDirection = Direction.E;
	int waypointY = 1;
	int waypointX = 10;
	int posY, posX;

	enum Direction{
		N(0),E(1),S(2),W(3);

		int value;

		Direction(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Direction valueOf(int number){
			for (Direction d : values()) {
				if (d.value == number) {
					return d;
				}
			}
			return null;
		}
	}
	

	@Override public void run() {
		readFile();
//		parseShipMovementOneStar();
		parseShipMovementTwoStar();
		System.out.println(posY + " " + posX);
		System.out.println("Result" + (Math.abs(posY) + Math.abs(posX)));
	}

	private void parseShipMovementOneStar() {
		for (String command : commands) {
			switch (command.charAt(0)){
				case 'N' -> posY += Integer.parseInt(command.split("N")[1]);
				case 'S' -> posY -= Integer.parseInt(command.split("S")[1]);
				case 'E' -> posX += Integer.parseInt(command.split("E")[1]);
				case 'W' -> posX -= Integer.parseInt(command.split("W")[1]);
				case 'L' -> {
					String[] result = command.split("L");
					int degrees = Integer.parseInt(result[1]);
					int newShipDirection = waypointDirection.getValue() - degrees / 90;
					newShipDirection  = newShipDirection < 0 ? 4 + newShipDirection : newShipDirection;
					waypointDirection = Direction.valueOf(newShipDirection);
				}
				case 'R' -> {
					String[] result = command.split("R");
					int degrees = Integer.parseInt(result[1]);
					int newShipDirection = waypointDirection.getValue() + degrees / 90;
					newShipDirection  = newShipDirection > 3 ? newShipDirection - 4 : newShipDirection;
					waypointDirection = Direction.valueOf(newShipDirection);
				}
				case 'F' -> {
					switch(Objects.requireNonNull(waypointDirection)){
						case E -> posX += Integer.parseInt(command.split("F")[1]);
						case W -> posX -= Integer.parseInt(command.split("F")[1]);
						case N -> posY += Integer.parseInt(command.split("F")[1]);
						case S -> posY -= Integer.parseInt(command.split("F")[1]);
					}
				}
			}
		}
	}

	private void parseShipMovementTwoStar() {
		for (String command : commands) {
			switch (command.charAt(0)){
				case 'N' -> waypointY += Integer.parseInt(command.split("N")[1]);
				case 'S' -> waypointY -= Integer.parseInt(command.split("S")[1]);
				case 'E' -> waypointX += Integer.parseInt(command.split("E")[1]);
				case 'W' -> waypointX -= Integer.parseInt(command.split("W")[1]);
				case 'L' -> {
					String[] result = command.split("L");
					int degrees = Integer.parseInt(result[1]);
					int howManyTimes = degrees / 90;
					for (int i = 0; i < howManyTimes; i++) {
						int temp = waypointY;
						waypointY = waypointX;
						waypointX = -temp;
					}
				}
				case 'R' -> {
					String[] result = command.split("R");
					int degrees = Integer.parseInt(result[1]);
					int howManyTimes = degrees / 90;
					System.err.println("rotated" + howManyTimes);
					for (int i = 0; i < howManyTimes; i++) {
						int temp = waypointX;
						waypointX = waypointY;
						waypointY = -temp;
					}
				}
				case 'F' -> {
					int multiplier = Integer.parseInt(command.split("F")[1]);
					posY += multiplier * waypointY;
					posX += multiplier * waypointX;
				}
			}
		}
	}

	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				commands.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}


}

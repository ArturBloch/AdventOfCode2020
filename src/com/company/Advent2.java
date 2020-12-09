package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//https://adventofcode.com/2020/day/2
public class Advent2 {

	File file = new File("src/files/advent2.txt");


	public void run(){
		findWrongPasswordTwoStar();
	}


	// 1 star
	public void findWrongPasswordOneStar(){
		int validCounter = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(" ");
				String[] constraints = data[0].split("-");
				int from = Integer.parseInt(constraints[0]);
				int to = Integer.parseInt(constraints[1]);
				char letter = data[1].charAt(0);
				String password = data[2];
				int counter = 0;
				for(int i = 0; i < password.length(); i++)
				{
					if(password.charAt(i) == letter){
						counter++;
					}
				}
				if(counter >= from && counter <= to){
					validCounter++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(validCounter);
	}

	// 2 star
	public void findWrongPasswordTwoStar(){
		int validCounter = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(" ");
				String[] constraints = data[0].split("-");
				int firstPos = Integer.parseInt(constraints[0]) - 1;
				int secondPos = Integer.parseInt(constraints[1]) - 1;
				char letter = data[1].charAt(0);
				String password = data[2];
				int counter = 0;
				if(password.charAt(firstPos) == letter ^ password.charAt(secondPos) == letter){
					validCounter++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(validCounter);
	}


}

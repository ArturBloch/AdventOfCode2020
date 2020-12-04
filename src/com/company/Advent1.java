package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//https://adventofcode.com/2020/day/1
public class Advent1 {

	File file = new File("src/files/advent1.txt");

	public void run(){
		findTwoNumbersAndMultiply();
//		findThreeNumbersAndMultiply();
	}

  public ArrayList<Integer> readFile(File file) throws IOException {
    ArrayList<Integer> numbers = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null) {
        numbers.add(Integer.parseInt(line));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return numbers;
  }

  public void findTwoNumbersAndMultiply() {
    ArrayList<Integer> numbers = null;
    try {
      numbers = readFile(file);
		for (int i = 0; i < numbers.size(); i++) {
			for (int j = i + 1; j < numbers.size(); j++) {
				for (int k = j + 1; k < numbers.size(); k++) {
					if (numbers.get(i) + numbers.get(j) + numbers.get(k) == 2020) {
						System.out.println(numbers.get(i) * numbers.get(j) * numbers.get(k));
					}
				}
			}
		}
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 2 STAR exercise
  public void findThreeNumbersAndMultiply() {
    ArrayList<Integer> numbers = null;
    try {
      numbers = readFile(file);
		for (int i = 0; i < numbers.size(); i++) {
			for (int j = i + 1; j < numbers.size(); j++) {
				if (numbers.get(i) + numbers.get(j) == 2020) {
					System.out.println(numbers.get(i) * numbers.get(j));
				}
			}
		}
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

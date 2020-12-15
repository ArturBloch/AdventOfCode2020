package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Advent9 implements Runnable{

	File file = new File("src/files/advent9.txt");
	ArrayList<Long> fileInput = new ArrayList<>();


	@Override public void run() {
		readFile();
		long result = findWrongPreamble(25);
		System.out.println(result); // one star
		encryptionWeakness(result);
	}

	//one star
	private long findWrongPreamble(int preambleLength) {
		for (int j = preambleLength; j < fileInput.size(); j++) {
			boolean correct = false;
			for (int i = j - preambleLength; i < j - 1; i++) {
				for (int k = i + 1; k < j; k++) {
					if(fileInput.get(j) == fileInput.get(i) + fileInput.get(k) && !fileInput.get(i).equals(fileInput.get(k))) correct = true;
				}
			}
			if(!correct) return fileInput.get(j);
		}
		return -1;
	}

	//two star
	private void encryptionWeakness(long weaknessFound){
		ArrayList<Long> encryptionChain = new ArrayList<>();
		for (int i = 0; i < fileInput.size(); i++) {
			long sum = 0;
			for (int j = i + 1; j < fileInput.size(); j++) {
				sum += fileInput.get(j);
				if(sum == weaknessFound){
					for (int k = i; k <= j; k++) {
						encryptionChain.add(fileInput.get(k));
					}
					long max = encryptionChain.stream().max(Long::compare).get();
					long min = encryptionChain.stream().min(Long::compare).get();
					System.out.println(max+min);
					return;
				}
			}
		}
	}

	private void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String input;
			while((input = br.readLine()) != null){
				fileInput.add(Long.parseLong(input));
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


}

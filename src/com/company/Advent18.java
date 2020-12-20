package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



// https://adventofcode.com/2020/day/18
public class Advent18 implements Runnable{

	File file = new File("src/files/advent18.txt");
	ArrayList<String> expressions  = new ArrayList<>();

	@Override public void run() {
		readFile();
		System.out.println(solve());
	}

	private long solve() {
		long start = System.nanoTime();
		long sum = 0;
		for (String expression : expressions) {
			sum += solveExpression(expression);
		}
		long end = System.nanoTime();
		System.err.println((double)(end - start) / 1000000 + " ms");
		return sum;
	}

	private long solveExpression(String expression) {
		int startingIndex = expression.indexOf("(");
		while(startingIndex != -1){
			int movingIndex = startingIndex + 1;
			int openingParenthesis = 0;
			int closingParenthesis = 0;
			while (true) {
				if (expression.charAt(movingIndex) == ')') closingParenthesis++;
				if (expression.charAt(movingIndex) == '(') openingParenthesis++;
				if (closingParenthesis == openingParenthesis + 1) break;
				movingIndex++;
			}
			long result = solveExpression(expression.substring(startingIndex + 1, movingIndex));
			expression = expression.replace(expression.substring(startingIndex, movingIndex + 1), result + "");
			startingIndex = expression.indexOf("(");
		}
		String[] splitExpr = expression.split(" ");
		ArrayList<Long> multiplicationsLeft = new ArrayList<>();
		for (int i = 1; i < splitExpr.length; i+=2) {
			if(splitExpr[i].equals("+")){
				long sum = Long.parseLong(splitExpr[i-1]);
				while(i < splitExpr.length && splitExpr[i].equals("+")){
					sum += Long.parseLong(splitExpr[i + 1]);
					i += 2;
				}
				multiplicationsLeft.add(sum);
			} else {
				multiplicationsLeft.add(Long.parseLong(splitExpr[i-1]));
			}
		}
		if(splitExpr[splitExpr.length-2].equals("*")){
			multiplicationsLeft.add(Long.parseLong(splitExpr[splitExpr.length - 1]));
		}
		long currentResult = 1;
		for (Long aLong : multiplicationsLeft) {
			currentResult *= aLong;
		}
		return currentResult;
	}


	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				expressions.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

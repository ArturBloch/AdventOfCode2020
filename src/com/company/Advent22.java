package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;

public class Advent22 implements Runnable{

	File file = new File("src/files/advent22.txt");
	int gameCounter = 0;
	ArrayDeque<Integer> player1StarterDeck = new ArrayDeque<>();
	ArrayDeque<Integer> player2StarterDeck = new ArrayDeque<>();
	ArrayDeque<Integer> finalWinner = new ArrayDeque<>();


	@Override public void run() {
		readFile();
		long start = System.nanoTime();
		oneStarPlayWar(player1StarterDeck, player2StarterDeck);
		int result = 0;
		int counter = 0;

		finalWinner.forEach(e -> System.err.print(e + " "));
		System.err.println();
		Iterator<Integer> itr = finalWinner.descendingIterator();
		while(itr.hasNext()) {
			counter++;
			int next = itr.next();
			result += (counter * next);
		}
		System.err.println(result);
		long end = System.nanoTime();
		System.err.println((end-start) / 1000000 + " ms");
	}

	public ArrayDeque<Integer> copyUntilIndex(int howMany, ArrayDeque<Integer> queueToCopy){
		ArrayDeque<Integer> newQueue = new ArrayDeque<>();
		Iterator<Integer> queueIt = queueToCopy.iterator();
		int counter = 0;
		while(counter < howMany && queueIt.hasNext()){
			int next = queueIt.next();
			newQueue.addLast(next);
			counter++;
		}
		return newQueue;
	}

	//boolean 1st player win
	// false 2nd player win
	private boolean oneStarPlayWar(ArrayDeque<Integer> player1Deck, ArrayDeque<Integer> player2Deck) {
		if(player1Deck.stream().max(Integer::compareTo).get() > player2Deck.stream().max(Integer::compareTo).get()) return true;
		gameCounter++;
		HashSet<String> stateHash = new HashSet<>();
		HashSet<String> secondHash = new HashSet<>();
		while(!player1Deck.isEmpty() && !player2Deck.isEmpty()){
			StringBuilder stringBuilder = new StringBuilder();
			player1Deck.forEach(stringBuilder::append);
			StringBuilder stringBuilder2 = new StringBuilder();
			player2Deck.forEach(stringBuilder2::append);
			if(!stateHash.add(stringBuilder.toString()) && !secondHash.add(stringBuilder2.toString())) {
				return true;
			}
			int player1Card = player1Deck.removeFirst();
			int player2Card = player2Deck.removeFirst();
			if(player1Deck.size() >= player1Card && player2Deck.size() >= player2Card){
				if(oneStarPlayWar(copyUntilIndex(player1Card, player1Deck), copyUntilIndex(player2Card, player2Deck))){
					player1Deck.addLast(player1Card);
					player1Deck.addLast(player2Card);
				} else {
					player2Deck.addLast(player2Card);
					player2Deck.addLast(player1Card);
				}

			} else {
				if (player1Card > player2Card) {
					player1Deck.addLast(player1Card);
					player1Deck.addLast(player2Card);
				} else {
					player2Deck.addLast(player2Card);
					player2Deck.addLast(player1Card);
				}
			}
		}

		finalWinner = player1Deck.isEmpty() ? player2Deck : player1Deck;
		player1Deck.forEach(e -> System.err.println(e + " "));
		return !player1Deck.isEmpty();
	}

	ArrayDeque<Integer> currentPlayer = player1StarterDeck;
	public void readFile() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if(line.isEmpty()) continue;
				if(line.contains("Player 1")){
					currentPlayer = player1StarterDeck;
					continue;
				} else if(line.contains("Player 2")){
					currentPlayer = player2StarterDeck;
					continue;
				}
				currentPlayer.addLast(Integer.parseInt(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Advent16 implements Runnable {


	File file = new File("src/files/advent16.txt");
	ArrayList<TicketField> ticketFields = new ArrayList<>();
	int[] myTicket;
	ArrayList<int[]> nearbyTickets;
	TicketField[] realTicket;
	int totalValidTickets = 0;
	ArrayList<ArrayList<TicketField>> possibleFields = new ArrayList<>();

	@Override public void run() {
		nearbyTickets = new ArrayList<>();
		readFile();
		//		checkValidTicketsOneStar();
		solveTwoStar();
	}


	public class Range {
		int from = Integer.MAX_VALUE;
		int to = Integer.MIN_VALUE;

		boolean valueInRange(int value) {
			return (value >= from && value <= to);
		}

		public void addValue(int value) {
			if (value < from) this.from = value;
			if (value > to) this.to = value;
		}
	}

	public class TicketField {
		String fieldName;
		Range range1;
		Range range2;

		TicketField() {
			range1 = new Range();
			range2 = new Range();
		}

		void addRange1(int from, int to) {
			range1.from = from;
			range1.to   = to;
		}

		void addRange2(int from, int to) {
			range2.from = from;
			range2.to   = to;
		}
	}

	public void solveTwoStar() {
		for (int i = 0; i < ticketFields.size(); i++) {
			ArrayList<TicketField> newTicketFieldList = new ArrayList<>(ticketFields);
			possibleFields.add(newTicketFieldList);
		}
		realTicket = new TicketField[ticketFields.size()];
		checkValidTicketsOneStar();
		ArrayList<Integer> fieldsFound = new ArrayList<>();
		for (int j = 0; j < ticketFields.size(); j++) {
			for (int i = 0; i < ticketFields.size(); i++) {
				ArrayList<TicketField> currentFieldList = possibleFields.get(i);
				if (currentFieldList.size() == 1 && !fieldsFound.contains(i)) {
					fieldsFound.add(i);
					for (int k = 0; k < ticketFields.size(); k++) {
						if(k != i){
							possibleFields.get(k).removeAll(currentFieldList);
						}
					}
				}
			}
		}
		long multipliedResult = 1;
		for (int i = 0; i < possibleFields.size(); i++) {
			if(possibleFields.get(i).get(0).fieldName.contains("departure")){
				multipliedResult *= myTicket[i];
			}
		}
		System.err.println("RESULT 2 STAR " + multipliedResult);
	}

	public void readFile() {
		BufferedReader br;
		boolean addMyTicket = true;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) continue;
				if (!line.contains(",")) {
					String[] lineSplit = line.split(":");
					TicketField ticketField = new TicketField();
					ticketField.fieldName = lineSplit[0];
					if (lineSplit.length <= 1) continue;
					String[] ticketRanges = lineSplit[1].trim().split(" ");
					String[] range1 = ticketRanges[0].split("-");
					String[] range2 = ticketRanges[2].split("-");
					ticketField.addRange1(Integer.parseInt(range1[0]), Integer.parseInt(range1[1]));
					ticketField.addRange2(Integer.parseInt(range2[0]), Integer.parseInt(range2[1]));
					ticketFields.add(ticketField);
				} else {
					String[] fields = line.split(",");
					if (fields.length < 3) continue;
					int[] ticket = new int[ticketFields.size()];
					for (int i = 0; i < fields.length; i++) {
						ticket[i] = Integer.parseInt(fields[i]);
					}
					if (addMyTicket) {
						myTicket    = ticket;
						addMyTicket = false;
					} else {
						nearbyTickets.add(ticket);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkValidTicketsOneStar() {
		int ticketScanningErrorRate = 0;
		for (int[] nearbyTicket : nearbyTickets) {
			boolean validTicket = true;
			for (int i = 0; i < nearbyTicket.length; i++) {
				boolean validField = false;
				for (int j = 0; j < ticketFields.size(); j++) {
					TicketField ticketField = ticketFields.get(j);
					if (ticketField.range1.valueInRange(nearbyTicket[i]) || ticketField.range2.valueInRange(nearbyTicket[i])) {
						validField = true;
					}
				}
				if (!validField) {
					validTicket = false;
					ticketScanningErrorRate += nearbyTicket[i];
					break;
				}
			}
			if (validTicket) {
				for (int i = 0; i < nearbyTicket.length; i++) {
					int finalI = i;
					possibleFields.get(i)
					              .removeIf(
						              e -> !e.range1.valueInRange(nearbyTicket[finalI]) && !e.range2.valueInRange(nearbyTicket[finalI]));
				}
				totalValidTickets++;
			}
		}
		System.out.println("valid tickets: " + totalValidTickets);
		System.out.println(ticketScanningErrorRate);
	}
}

package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Advent5 {

    File file = new File("src/files/advent5.txt");

    public void run() {
        ArrayList<Integer> seats = generateSeats();
        int mySeat = findMissingSeat(seats);
        if(mySeat != -1){
            System.out.println("My missing seat is " + mySeat);
        }
        System.out.printf("Biggest seat ID is: %d", seats.stream().max(Integer::compare).get());
    }

    public int findMissingSeat(List<Integer> seats){
        int min = seats.stream().min(Integer::compare).get();
        int max =seats.stream().max(Integer::compare).get();
        for (int i = min; i < max; i++) {
            if(!seats.contains(i) && seats.contains((i-1)) && seats.contains(((i + 1)))){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Integer> generateSeats(){
        ArrayList<Integer> seats = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int validFields = 0;
            while ((line = reader.readLine()) != null) {
               seats.add(findSeat(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public int findSeat(String inputLine){
        double rowLow = 0;
        double rowHigh = 127;
        double columnLow = 0;
        double columnHigh = 7;
        for (int i = 0; i < inputLine.length(); i++) {
            System.err.println(inputLine.charAt(i) + " " + rowHigh + " " + rowLow);
            if(inputLine.charAt(i) == 'F'){
                if(rowHigh == rowLow + 1) {
                    rowHigh = rowLow;
                } else {
                    rowHigh = Math.floor(rowHigh -  (rowHigh - rowLow) / 2);
                }
            } else if(inputLine.charAt(i) == 'B'){
                if(rowHigh == rowLow + 1) {
                } else {
                    rowLow = Math.floor(rowHigh - (rowHigh - rowLow) / 2);
                }
            } else if(inputLine.charAt(i) == 'L'){
                if(columnHigh == columnLow + 1) {
                    columnHigh = columnLow;
                } else {
                    columnHigh = Math.floor(columnHigh -  (columnHigh - columnLow) / 2);
                }
            } else if(inputLine.charAt(i) == 'R'){
                columnLow = Math.floor(columnHigh -  (columnHigh - columnLow) / 2);
            }
        }

        return (int)(rowHigh * 8 + columnHigh);
    }

}

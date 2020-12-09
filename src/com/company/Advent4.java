package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Advent4 {

    File file = new File("src/files/advent4.txt");


    public void run() {
        System.out.println(countValidPassports());
    }

    String[] requiredFields = {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
    String[] eclValidation = {"amb", "blu", "brn", "gry", "grn", "hzl", "oth"};

    public int validatePasswordLine(String passportLine) {
        String[] fields = passportLine.split(" ");
        int validCounter = 0;
        for (String field : fields) {
            String[] data = field.split(":");
            for (int i = 0; i < data.length; i += 2) {
                switch (data[i]) {
                    case "byr" -> {
                        int year = Integer.parseInt(data[i + 1]);
                        if (year >= 1920 && year <= 2002) validCounter++;
                    }
                    case "iyr" -> {
                        int year = Integer.parseInt(data[i + 1]);
                        if (year >= 2010 && year <= 2020) validCounter++;
                    }
                    case "eyr" -> {
                        int year = Integer.parseInt(data[i + 1]);
                        if (year >= 2020 && year <= 2030) validCounter++;
                    }
                    case "hgt" -> {
                        if(data[i+1].contains("cm")){
                            int height = Integer.parseInt(data[i+1].split("cm")[0]);
                            if(height >= 150 && height <= 193) validCounter++;
                        } else {
                            int height = Integer.parseInt(data[i+1].split("in")[0]);
                            if(height >= 59 && height <= 76) validCounter++;
                        }
                    }
                    case "hcl" -> {
                        if (data[i + 1].matches("#[a-f0-9]{6}")) validCounter++;
                    }
                    case "ecl" -> {
                        if(Arrays.asList(eclValidation).contains(data[i + 1])) validCounter++;
                    }
                    case "pid" -> {
                        if (data[i + 1].matches("\\d{9}")) validCounter++;
                    }
                }
            }
        }
        return validCounter;
    }

    public int countValidPassports() {
        int validPassports = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int validFields = 0;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (validFields >= requiredFields.length) {
                        validPassports++;
                    }
                    validFields = 0;
                }
                validFields += validatePasswordLine(line);
            }
            if (validFields >= requiredFields.length) {
                validPassports++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return validPassports;
    }
}

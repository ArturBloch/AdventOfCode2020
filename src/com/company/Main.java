package com.company;

public class Main {

  public static void main(String[] args) {
    Advent11 advent11 = new Advent11();
    Thread t = new Thread(advent11);
    t.start();
  }
}

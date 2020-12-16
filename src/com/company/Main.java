package com.company;

public class Main {

  public static void main(String[] args) {
    Thread t = new Thread(new Advent15());
    t.start();
  }
}

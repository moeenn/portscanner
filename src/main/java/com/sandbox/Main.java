package com.sandbox;

public class Main {

    private static void run() throws Exception {
        System.out.println("hello world");
    }

    public static void main(String[] args) {
        try {
            Main.run();
        } catch (Exception e) {
            System.err.printf("error: %s.\n", e.getMessage());
        }
    }
}

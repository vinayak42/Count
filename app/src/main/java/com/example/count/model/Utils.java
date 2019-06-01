package com.example.count.model;

public class Utils {

    private String email;
    private String name;
    private String id;
    private String token;

    private static final Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }
}

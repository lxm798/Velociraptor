package com.xm.velociraptor.utils;

public class Asserts {
    public static final <T> void assertEqual(T expect, T actual) {
        if (expect != actual) {
            throw new RuntimeException(expect + "!=" + actual);
        }
    }
}

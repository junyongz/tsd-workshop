package com.tsd.workshop;

import java.security.SecureRandom;

public class PhpSessionIdGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final int DEFAULT_LENGTH = 26;

    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    public static String generate(int length) {
        return random.ints(length, 0, CHARACTERS.length())
                .mapToObj(i -> String.valueOf(CHARACTERS.charAt(i)))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}

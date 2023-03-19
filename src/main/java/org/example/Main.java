package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    public static AtomicInteger counter3 = new AtomicInteger(0);
    public static AtomicInteger counter4 = new AtomicInteger(0);
    public static AtomicInteger counter5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        threads.add(new Thread(() -> {
            for (String text : texts) {
                StringBuilder sb = new StringBuilder(text);
                if (text.equals(sb.reverse().toString()) && !text.chars().allMatch(c -> text.charAt(0) == c)) {
                    countersIncrementator(text);
                }
            }
        }
        ));

        threads.add(new Thread(() -> {
            for (String text : texts) {
                if (text.chars().allMatch(c -> text.charAt(0) == c)) {
                    countersIncrementator(text);
                }
            }
        }
        ));

        threads.add(new Thread(() -> {
            for (String text : texts) {
                String sortText = text.chars()
                        .sorted()
                        .collect(StringBuilder::new,
                                StringBuilder::appendCodePoint,
                                StringBuilder::append)
                        .toString();
                if (text.equals(sortText)) {
                    countersIncrementator(text);
                }
            }
        }
        ));

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Красивых слов с длиной 3: " + counter3 + " шт.");
        System.out.println("Красивых слов с длиной 4: " + counter4 + " шт.");
        System.out.println("Красивых слов с длиной 5: " + counter5 + " шт.");

    }

    public static void countersIncrementator(String text) {
        switch (text.length()) {
            case 3:
                counter3.incrementAndGet();
                break;
            case 4:
                counter4.incrementAndGet();
                break;
            case 5:
                counter5.incrementAndGet();
                break;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
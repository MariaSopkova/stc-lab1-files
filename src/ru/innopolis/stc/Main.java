package ru.innopolis.stc;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String path = "file:///C:\\Users\\Maria\\Desktop\\Бжехва Ян. Академия пана Кляксы - royallib.ru.txt";
        String hpPath = "http://www.glozman.com/TextPages/Harry%20Potter%207%20-%20Deathly%20Hollows.txt";
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> queueWithPatterns = new ConcurrentLinkedQueue<>();

        ExecutorService fileReaders = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 7; ++i) {
            fileReaders.submit(new ReadFileFromURL(hpPath, queue, Integer.valueOf(i).toString()));
        }

        String[] pattern = {"Harry"};

        ExecutorService patternFinders = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 7; ++i) {
            patternFinders.submit(new PatternFinder(pattern, queue, queueWithPatterns));
        }

        Thread writer = new Thread(new QueueWriter(queueWithPatterns, "result.txt"));
        writer.start();
    }
}

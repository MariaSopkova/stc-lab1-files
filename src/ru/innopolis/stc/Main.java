package ru.innopolis.stc;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println(System.currentTimeMillis());
        long start = System.currentTimeMillis();
        String path = "file:///C:\\Users\\Maria\\Desktop\\Бжехва Ян. Академия пана Кляксы - royallib.ru.txt";
        String hpPath = "http://www.glozman.com/TextPages/Harry%20Potter%207%20-%20Deathly%20Hollows.txt";
        String big_file = "file:///D:\\innopolis\\test_set\\aa1bed85-aca5-4fff-8b4b-a133ea4e0ca9.txt";
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> queueWithPatterns = new ConcurrentLinkedQueue<>();

        String[] str = {big_file, hpPath};
        FilesReaderThread readerThread = new FilesReaderThread(queue, str);
        readerThread.start();
        String[] patter = {"well"};
        PatternFinderThread finderThread = new PatternFinderThread(queue, queueWithPatterns, patter);
        finderThread.start();
        QueueWriterThread writer = new QueueWriterThread(queueWithPatterns, "result.txt");
        writer.start();

        readerThread.join();
        System.out.println("readerThread joined");
        System.out.println(queue.size());
        finderThread.setAllFilesWasRead(true);
        while (!queue.isEmpty()) {
            System.out.println(queue.size());
        }
        finderThread.interrupt();
        finderThread.join();
        writer.setAllFilesWasProcessed(true);
        writer.join();
        System.out.println("work time: " + (System.currentTimeMillis() - start));
        System.out.println(System.currentTimeMillis());
    }
}

package ru.innopolis.stc;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PatternFinderThread extends Thread {
    private static int THREAD_POOL_SIZE = 7;
    private ConcurrentLinkedQueue<String> queueFromFile;
    private ConcurrentLinkedQueue<String> queueToWrite;
    private boolean allFilesWasRead;
    private String[] patterns;

    public PatternFinderThread(ConcurrentLinkedQueue<String> queueFromFile, ConcurrentLinkedQueue<String> queueToWrite,
                               String[] patterns) {
        this.queueFromFile = queueFromFile;
        this.queueToWrite = queueToWrite;
        this.patterns = patterns;
        allFilesWasRead = false;
    }

    public void setAllFilesWasRead(boolean state) {
        allFilesWasRead = state;
        System.out.println("state set");
    }

    @Override
    public void run() {
        ExecutorService patternFinders = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; ++i) {
            patternFinders.submit(new PatternFinder(patterns, queueFromFile, queueToWrite));
        }

        while (!Thread.currentThread().isInterrupted()) {

        }
        patternFinders.shutdownNow();
    }
}

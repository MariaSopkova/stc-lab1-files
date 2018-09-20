package ru.innopolis.stc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
            String[] files = getFilesPath(args[0]);
            String[] pattrns = patters(args[1]);
            getOccurencies(files, pattrns, args[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getOccurencies(String[] sources, String[] words, String res) throws InterruptedException {
        long start = System.currentTimeMillis();
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> queueWithPatterns = new ConcurrentLinkedQueue<>();

        FilesReaderThread readerThread = new FilesReaderThread(queueWithPatterns, sources, words);
        readerThread.start();

        //PatternFinderThread finderThread = new PatternFinderThread(queue, queueWithPatterns, words);
        //finderThread.start();
        QueueWriterThread writer = new QueueWriterThread(queueWithPatterns, "result.txt");
        writer.start();

        readerThread.join();
        System.out.println("readerThread joined");
        System.out.println(queue.size());
        //finderThread.setAllFilesWasRead(true);
        //finderThread.interrupt();
        //finderThread.join();
        writer.setAllFilesWasProcessed(true);
        writer.join();
        System.out.println("work time: " + (System.currentTimeMillis() - start));
    }

    public static String[] getFilesPath(String fileInfoPath) throws IOException {
        List<String> files = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileInfoPath)));
        String directory = reader.readLine();
        File folder = new File(directory);
        File[] filesInDirectory = folder.listFiles();
        for (File entry : filesInDirectory) {
            if (!entry.isDirectory()) {
                files.add("file:///" + entry.getPath());
            }
        }
        String filePath;
        while ((filePath = reader.readLine()) != null) {
            files.add(filePath);
        }

        //return files.toArray();
        String[] result = {};
        String[] returnResult = files.toArray(result);
        return returnResult;
    }

    public static String[] patters(String fileName) throws IOException {
        BufferedReader pattrnsReader = new BufferedReader(new FileReader(new File(fileName)));
        String pattern;
        List<String> patterns = new ArrayList<>();
        while ((pattern = pattrnsReader.readLine()) != null) {
            patterns.add(pattern);
        }
        String[] result = {};
        String[] returnResult = patterns.toArray(result);
        return returnResult;
    }
}

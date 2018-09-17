package ru.innopolis.stc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FilesReaderThread extends Thread {
    private static int THREAD_POOL_SIZE = 1;
    private ConcurrentLinkedQueue<String> queue;
    private String[] filesName;

    public FilesReaderThread(ConcurrentLinkedQueue<String> queue, String[] filesName) {
        this.queue = queue;
        this.filesName = filesName;
    }

    @Override
    public void run() {
        System.out.println("FilesReader started");
        ExecutorService fileReaders = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        List<Future<String>> futures = new ArrayList<>();
        for (String fileName : filesName) {
            futures.add(CompletableFuture.supplyAsync(() -> new ReadFileFromURL(fileName, queue).read(), fileReaders));
        }

        for (Future<String> future : futures) {
            try {
                System.out.println(future.get() + " was read");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("FilesReader stopped");
        fileReaders.shutdown();
    }
}

package ru.innopolis.stc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FilesReaderThread extends Thread {
    private static int THREAD_POOL_SIZE = 10;
    private ConcurrentLinkedQueue<String> queue;
    private String[] filesName;
    private String[] patterns;

    public FilesReaderThread(ConcurrentLinkedQueue<String> queue, String[] filesName, String[] patterns) {
        this.queue = queue;
        this.filesName = filesName;
        this.patterns = patterns;
    }

    @Override
    public void run() {
        System.out.println("FilesReader started");
        for (String fileName : filesName) {
            System.out.println(fileName);
        }
        ExecutorService fileReaders = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        List<Future<String>> futures = new ArrayList<>();
        for (String fileName : filesName) {
            futures.add(CompletableFuture.supplyAsync(() -> new ReadFileFromURL(fileName, queue, patterns).read(), fileReaders));
        }

        int turn = 0;
        int size = filesName.length;
        futures.forEach((future) -> {
            try {
                System.out.println(turn + "/" + size + future.get());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        System.out.println("FilesReader stopped");
        fileReaders.shutdown();
    }
}

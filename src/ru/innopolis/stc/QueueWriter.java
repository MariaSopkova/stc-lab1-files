package ru.innopolis.stc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * класс для записи предложений с шаблонами в файл
 */
public class QueueWriter implements Runnable {
    private ConcurrentLinkedQueue<String> queueToWrite;
    private String fileName;

    public QueueWriter(ConcurrentLinkedQueue<String> queueToWrite, String fileName) {
        this.queueToWrite = queueToWrite;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            while (!Thread.currentThread().isInterrupted()) {
                String stringFromPull = queueToWrite.poll();
                if (stringFromPull != null) {
                    writer.write(stringFromPull);
                    writer.write("\n");
                }
            }
            writer.close();
            System.out.println("stop writer");
            return;
        } catch (IOException ex) {
            ex.getStackTrace();
            return;
        }
    }
}

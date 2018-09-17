package ru.innopolis.stc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * класс для записи предложений с шаблонами в файл
 */
public class QueueWriterThread extends Thread {
    private ConcurrentLinkedQueue<String> queueToWrite;
    private String fileName;
    private boolean allFilesWasProcessed;

    public QueueWriterThread(ConcurrentLinkedQueue<String> queueToWrite, String fileName) {
        this.queueToWrite = queueToWrite;
        this.fileName = fileName;
        allFilesWasProcessed = false;
    }

    public void setAllFilesWasProcessed(boolean state) {
        allFilesWasProcessed = state;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            boolean continueLoop = true;
            while (!allFilesWasProcessed || !queueToWrite.isEmpty()) {
                String stringFromPull = queueToWrite.poll();
                while (stringFromPull != null) {
                    writer.write(stringFromPull);
                    writer.write("\n");
                    stringFromPull = queueToWrite.poll();
                }
            }
        } catch (IOException ex) {
            ex.getStackTrace();
            return;
        }
    }
}

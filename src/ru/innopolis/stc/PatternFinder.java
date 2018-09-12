package ru.innopolis.stc;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Класс для поиска слов-шаблонов в предложениях
 */
public class PatternFinder implements Runnable {
    private String[] pattern;
    private ConcurrentLinkedQueue<String> readableQueue;
    private ConcurrentLinkedQueue<String> writableQueueWithPatterns;
    private boolean allFilesWasReaded;

    public PatternFinder(String[] pattern, ConcurrentLinkedQueue readableQueue, ConcurrentLinkedQueue writableQueueWithPatterns) {
        this.pattern = pattern;
        this.readableQueue = readableQueue;
        this.writableQueueWithPatterns = writableQueueWithPatterns;
        allFilesWasReaded = false;
    }

    @Override
    public void run() {
        while (true) {
            String stringToCheck = writableQueueWithPatterns.poll();
            if (stringToCheck != null && findPatternInString(stringToCheck)) {
                writableQueueWithPatterns.add(stringToCheck);
            } else if (stringToCheck == null && allFilesWasReaded) {
                System.out.println("patternFinder finished");
                return;
            }
        }
    }

    public void setAllFilesWasReadedr(boolean allFilesWasReaded) {
        this.allFilesWasReaded = allFilesWasReaded;
    }

    /**
     * Поиск одного из слов-шаблонов в строке
     *
     * @param string строка, в которой происходит поис
     * @return содержит ли строка шаблон
     */
    private boolean findPatternInString(String string) {
        return Arrays.stream(pattern).parallel().anyMatch(string::contains);
    }
}

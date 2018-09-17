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

    public PatternFinder(String[] pattern, ConcurrentLinkedQueue readableQueue,
                         ConcurrentLinkedQueue writableQueueWithPatterns) {
        this.pattern = pattern;
        this.readableQueue = readableQueue;
        this.writableQueueWithPatterns = writableQueueWithPatterns;
    }

    @Override
    public void run() {
        while (true) {
            String stringToCheck = readableQueue.poll();
            if (stringToCheck != null) {
                if (findPatternInString(stringToCheck)) {
                    writableQueueWithPatterns.add(stringToCheck);
                }
            }
        }
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

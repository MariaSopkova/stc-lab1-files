package ru.innopolis.stc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Класс для чтения файлов по url адресу
 */
public class ReadFileFromURL {
    private String fileURL;
    private ConcurrentLinkedQueue<String> queue;
    private String[] pattern;

    public ReadFileFromURL(String fileURL, ConcurrentLinkedQueue<String> queue, String[] pattern) {
        this.fileURL = fileURL;
        this.queue = queue;
        this.pattern = pattern;
    }

    public String read() {
        System.out.println(Thread.currentThread().getName() + " started.File " + fileURL);
        try (BufferedReader urlReader = new BufferedReader(new InputStreamReader(new URL(fileURL).openStream()))) {
            System.out.println(urlReader);
            addTextToQueue(urlReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " finished");
        return fileURL;
    }

    private void addTextToQueue(BufferedReader urlReader) throws IOException {
        String inputString;
        while ((inputString = readLineFromURLPath(urlReader)) != null && !inputString.isEmpty()) {
            if (checkStringOnPatterns(inputString))
                queue.add(inputString);
        }
    }

    private String readLineFromURLPath(BufferedReader urlReader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int ch;
        while ((ch = urlReader.read()) != -1 && ch != '.' && ch != '!' && ch != '?') {
            builder.append((char) ch);
        }
        if (ch != -1) {
            builder.append((char) ch);
            // символ должен быть либо пустой строкой, либо переводом на новую строку
            urlReader.read();
        }
        return builder.toString();
    }

    private boolean checkStringOnPatterns(String string) {
        return Arrays.stream(pattern).parallel().anyMatch(string::contains);
    }
}

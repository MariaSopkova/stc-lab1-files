package ru.innopolis.stc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

/**
 * Класс для чтения файлов по url адресу
 */
public class ReadFileFromURL {
    private String fileURL;
    private ConcurrentLinkedQueue<String> queue;

    public ReadFileFromURL(String fileURL, ConcurrentLinkedQueue<String> queue) {
        this.fileURL = fileURL;
        this.queue = queue;
    }

    public String read() {
        System.out.println(Thread.currentThread().getName() + " started");
        try (BufferedReader urlReader = new BufferedReader(new InputStreamReader(new URL(fileURL).openStream()))) {
            //readTextFromURLPath(urlReader);
            readTextTromURLPath2(urlReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " finished");
        return fileURL;
    }

    /**
     * Прочитать текстовый файл по URl
     *
     * @param urlReader поток с файлом для чтения
     * @throws IOException ошибка при чтении файла
     */
    private void readTextFromURLPath(BufferedReader urlReader) throws IOException {
        // Чтение происходит по строчно, после этого прочитанный абзац разбивается на предложения
        String inputString;

        Pattern stringEnding = Pattern.compile("([.!?][\\d ])");
        while ((inputString = urlReader.readLine()) != null) {
            String[] sentences = inputString.split(stringEnding.pattern());
            queue.addAll(Arrays.asList(sentences));
        }
    }

    private void readTextTromURLPath2(BufferedReader urlReader) throws IOException {
        String inputString;
        while ((inputString = readLineFromURLPath(urlReader)) != null && !inputString.isEmpty()) {
            //System.out.println(inputString);
            queue.add(inputString);
        }
    }

    private String readLineFromURLPath(BufferedReader urlReader) throws IOException {
        StringBuilder builder = new StringBuilder();
        try {
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
        } catch (OutOfMemoryError ex) {
            queue.size();
            builder.toString();
            ex.getStackTrace();
            return null;
        }
    }
}

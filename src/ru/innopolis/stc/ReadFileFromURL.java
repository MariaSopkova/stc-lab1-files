package ru.innopolis.stc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Класс для чтения файлов по url адресу
 */
public class ReadFileFromURL implements Runnable {
    String name;
    private String fileURL;
    private ConcurrentLinkedQueue queue;

    public ReadFileFromURL(String fileURL, ConcurrentLinkedQueue queue, String name) {
        this.fileURL = fileURL;
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " started");
        try (BufferedReader urlReader = new BufferedReader(new InputStreamReader(new URL(fileURL).openStream()))) {
            readTextFromURLPath(urlReader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(name + " finished");

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
        while ((inputString = urlReader.readLine()) != null) {
            String[] sentences = inputString.split("([.!?]{1}[\\d ])");
            Arrays.stream(sentences).forEach(value -> queue.add(value));
        }
    }
}

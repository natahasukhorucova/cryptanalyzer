import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileManager {

    /**
     * Чтение файла с использованием NIO
     * @param filePath путь к файлу
     * @return содержимое файла или null при ошибке
     */
    public String readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
    }

    /**
     * Запись в файл с использованием NIO
     * @param content содержимое для записи
     * @param filePath путь для сохранения
     */
    public void writeFile(String content, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    /**
     * Чтение больших файлов построчно
     * @param filePath путь к файлу
     * @return содержимое файла
     */
    public String readLargeFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
        return content.toString();
    }

    /**
     * Запись больших файлов с буферизацией
     */
    public void writeLargeFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
        }
    }
}
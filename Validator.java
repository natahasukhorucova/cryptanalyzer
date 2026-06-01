import java.nio.file.*;

public class Validator {

    /**
     * Проверка существования файла
     */
    public boolean isFileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * Проверка допустимости ключа
     */
    public boolean isValidKey(int key, char[] alphabet) {
        return key >= 0 && key < alphabet.length;
    }

    /**
     * Проверка пути для сохранения файла
     */
    public boolean isValidOutputPath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        // Проверка на системные файлы
        String dangerousPaths = "hosts|passwd|shadow|.bash|.zsh|.profile";
        if (filePath.toLowerCase().matches(".*(" + dangerousPaths + ").*")) {
            return false;
        }

        try {
            Path path = Paths.get(filePath);
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверка, является ли текст читаемым (содержит слова на русском)
     */
    public boolean isReadableText(String text) {
        if (text == null || text.length() < 10) return false;

        // Проверка наличия русских букв и пробелов
        int russianLetters = 0;
        int spaces = 0;

        for (char c : text.toLowerCase().toCharArray()) {
            if (c >= 'а' && c <= 'я') russianLetters++;
            if (c == ' ') spaces++;
        }

        // Примерный критерий: >30% букв и >5% пробелов
        return russianLetters > text.length() * 0.3 && spaces > text.length() * 0.05;
    }
}
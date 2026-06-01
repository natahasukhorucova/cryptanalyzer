import java.util.*;

public class BruteForce {
    private final char[] alphabet;
    private final Cipher cipher;
    private final Validator validator;
    private double Similarity;

    public BruteForce(char[] alphabet) {
        this.alphabet = alphabet;
        this.cipher = new Cipher(alphabet);
        this.validator = new Validator();
    }

    /**
     * Расшифровка методом перебора всех ключей
     * @param encryptedText зашифрованный текст
     * @param sampleFilePath путь к файлу-образцу (опционально)
     * @return наиболее вероятный расшифрованный текст
     */
    public String decryptByBruteForce(String encryptedText, String sampleFilePath) {
        String sampleText = null;
        if (sampleFilePath != null && validator.isFileExists(sampleFilePath)) {
            FileManager fm = new FileManager();
            sampleText = fm.readFile(sampleFilePath);
        }

        int bestShift = 0;
        double bestScore = -1;
        String bestDecrypted = null;

        // Перебор всех возможных ключей
        for (int shift = 1; shift < alphabet.length; shift++) {
            String decrypted = cipher.decrypt(encryptedText, shift);
            double score = evaluateText(decrypted, sampleText);

            if (score > bestScore) {
                bestScore = score;
                bestShift = shift;
                bestDecrypted = decrypted;
            }

            // Вывод прогресса
            if (shift % 5 == 0) {
                System.out.println("Проверен ключ " + shift + "/" + (alphabet.length - 1));
            }
        }

        System.out.println("Найден наиболее вероятный ключ: " + bestShift);
        System.out.println("Оценка качества: " + bestScore);

        return bestDecrypted;
    }

    /**
     * Оценка качества расшифрованного текста
     */
    private double evaluateText(String text, String sampleText) {
        if (text == null || text.length() < 10) return 0;

        double score = 0;

        // Критерий 1: наличие русских букв (30%)
        int russianLetters = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (c >= 'а' && c <= 'я') russianLetters++;
        }
        double letterRatio = (double) russianLetters / text.length();
        if (letterRatio > 0.3) score += 30;
        else if (letterRatio > 0.2) score += 15;

        // Критерий 2: наличие пробелов (20%)
        int spaces = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') spaces++;
        }
        double spaceRatio = (double) spaces / text.length();
        if (spaceRatio > 0.1 && spaceRatio < 0.3) score += 20;
        else if (spaceRatio > 0.05) score += 10;

        // Критерий 3: наличие пунктуации (10%)
        String punctuation = ".,!?-:;\"'";
        int punctCount = 0;
        for (char c : text.toCharArray()) {
            if (punctuation.indexOf(c) != -1) punctCount++;
        }
        if (punctCount > 0) score += Math.min(10, punctCount * 2);

        // Критерий 4: наличие слов разной длины (20%)
        String[] words = text.split("\\s+");
        Set<Integer> wordLengths = new HashSet<>();
        for (String word : words) {
            if (word.length() > 0 && word.length() < 30) {
                wordLengths.add(word.length());
            }
        }
        score += Math.min(20, wordLengths.size());

        // Критерий 5: сравнение с образцом (20%)
        if (sampleText != null && sampleText.length() > 100) {
            double similarity = calculateSimilarity(text, sampleText);
            score += similarity * 20;
        }

        return score;
    }

    /**
     * Расчет схожести текстов на основе частот букв
     */
    private double calculateSimilarity(String text1, String text2) {
        Map<Character, Double> freq1 = getLetterFrequency(text1);
        Map<Character, Double> freq2 = getLetterFrequency(text2);

        double sim;


        
        for (char c = 'а'; c <= 'я'; c++) {
            double f1 = freq1.


                    getOrDefault(c, 0.0);
            double f2 = freq2.getOrDefault(c, 0.0);
            Similarity += 1 - Math.abs(f1 - f2);
        }

        return Similarity / 33; // Нормализация
    }

    /**
     * Получение частот букв в тексте
     */
    private Map<Character, Double> getLetterFrequency(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        int totalLetters = 0;

        for (char c : text.toLowerCase().toCharArray()) {
            if (c >= 'а' && c <= 'я') {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
                totalLetters++;
            }
        }

        Map<Character, Double> frequencies = new HashMap<>();
        if (totalLetters > 0) {
            for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
                frequencies.put(entry.getKey(), (double) entry.getValue() / totalLetters);
            }
        }

        return frequencies;
    }
}
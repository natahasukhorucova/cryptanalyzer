import java.util.*;

public class StatisticalAnalyzer {
    private final char[] alphabet;
    private final Validator validator;

    // Статистика частот букв в русском языке (на 1000 символов)
    private static final Map<Character, Double> RUSSIAN_FREQUENCIES = new HashMap<>();

    static {
        // Частоты букв русского языка (приблизительные значения)
        double[] freqs = {
                0.062, 0.014, 0.038, 0.013, 0.025, 0.072, 0.007, 0.016,  // а,б,в,г,д,е,ж,з
                0.062, 0.028, 0.035, 0.035, 0.053, 0.090, 0.038, 0.038,  // и,й,к,л,м,н,о,п
                0.040, 0.045, 0.042, 0.009, 0.014, 0.006, 0.015, 0.021,  // р,с,т,у,ф,х,ц,ч
                0.014, 0.003, 0.016, 0.014, 0.016, 0.002, 0.018, 0.020   // ш,щ,ъ,ы,ь,э,ю,я
        };

        char[] letters = "абвгдежзийклмнопрстуфхцчшщъыьэюя".toCharArray();
        for (int i = 0; i < letters.length && i < freqs.length; i++) {
            RUSSIAN_FREQUENCIES.put(letters[i], freqs[i]);
        }
    }

    public StatisticalAnalyzer(char[] alphabet) {
        this.alphabet = alphabet;
        this.validator = new Validator();
    }

    /**
     * Нахождение наиболее вероятного сдвига
     * @param encryptedText зашифрованный текст
     * @param sampleFilePath путь к файлу-образцу
     * @return наиболее вероятный ключ
     */
    public int findMostLikelyShift(String encryptedText, String sampleFilePath) {
        // Получение частот зашифрованного текста
        Map<Character, Double> encryptedFreq = getCharacterFrequency(encryptedText);

        // Если есть файл-образец, используем его
        Map<Character, Double> referenceFreq;
        if (sampleFilePath != null && validator.isFileExists(sampleFilePath)) {
            FileManager fm = new FileManager();
            String sampleText = fm.readFile(sampleFilePath);
            referenceFreq = getCharacterFrequency(sampleText);
            System.out.println("Используется файл-образец для анализа");
        } else {
            referenceFreq = RUSSIAN_FREQUENCIES;
            System.out.println("Используется стандартная статистика русского языка");
        }

        // Поиск сдвига с минимальным отклонением
        int bestShift = 0;
        double minDeviation = Double.MAX_VALUE;

        for (int shift = 0; shift < alphabet.length; shift++) {
            double deviation = calculateDeviation(encryptedFreq, referenceFreq, shift);

            if (deviation < minDeviation) {
                minDeviation = deviation;
                bestShift = shift;
            }

            if (shift % 10 == 0) {
                System.out.println("Проверен сдвиг " + shift + "/" + alphabet.length);
            }
        }

        System.out.println("Минимальное отклонение: " + minDeviation);
        return bestShift;
    }

    /**
     * Получение частот символов в тексте
     */
    private Map<Character, Double> getCharacterFrequency(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        int totalChars = 0;

        for (char c : text.toLowerCase().toCharArray()) {
            // Учитываем только символы из алфавита
            if (isInAlphabet(c)) {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
                totalChars++;
            }
        }

        Map<Character, Double> frequencies = new HashMap<>();
        if (totalChars > 0) {
            for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
                frequencies.put(entry.getKey(), (double) entry.getValue() / totalChars);
            }
        }

        return frequencies;
    }

    /**
     * Расчет отклонения между частотами
     */
    private double calculateDeviation(Map<Character, Double> encryptedFreq,
                                      Map<Character, Double> referenceFreq,
                                      int shift) {
        double deviation = 0;
        int matchedChars


                = 0;

        for (Map.Entry<Character, Double> refEntry : referenceFreq.entrySet()) {


            char refChar = refEntry.getKey();
            double refValue = refEntry.getValue();

            // Находим соответствующий зашифрованный символ
            char encryptedChar = shiftChar(refChar, shift);
            double encValue = encryptedFreq.getOrDefault(encryptedChar, 0.0);

            // Используем квадрат разности
            double diff = refValue - encValue;
            deviation += diff * diff;
            matchedChars++;
        }

        // Нормализация
        return matchedChars > 0 ? deviation / matchedChars : Double.MAX_VALUE;
    }

    /**
     * Сдвиг символа (для приведения частот к одному ключу)
     */
    private char shiftChar(char c, int shift) {
        int index = findCharIndex(c);
        if (index == -1) return c;

        int newIndex = (index + shift) % alphabet.length;
        return alphabet[newIndex];
    }

    /**
     * Проверка наличия символа в алфавите
     */
    private boolean isInAlphabet(char c) {
        return findCharIndex(c) != -1;
    }

    /**
     * Поиск индекса символа
     */
    private int findCharIndex(char c) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
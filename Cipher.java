public class Cipher {
    private final char[] alphabet;
    private final int alphabetSize;

    public Cipher(char[] alphabet) {
        this.alphabet = alphabet;
        this.alphabetSize = alphabet.length;
    }

    /**
     * Шифрование текста
     * @param text исходный текст
     * @param shift ключ шифрования (сдвиг)
     * @return зашифрованный текст
     */
    public String encrypt(String text, int shift) {
        shift = normalizeShift(shift);
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = findCharIndex(c);
            if (index != -1) {
                int newIndex = (index + shift) % alphabetSize;
                result.append(alphabet[newIndex]);
            } else {
                result.append(c); // Символ не из алфавита - оставляем как есть
            }
        }

        return result.toString();
    }

    /**
     * Расшифровка текста
     * @param encryptedText зашифрованный текст
     * @param shift ключ расшифровки
     * @return расшифрованный текст
     */
    public String decrypt(String encryptedText, int shift) {
        shift = normalizeShift(shift);
        // Для расшифровки сдвигаем в обратную сторону
        return encrypt(encryptedText, alphabetSize - shift);
    }

    /**
     * Нормализация ключа (приведение к диапазону 0..alphabetSize-1)
     */
    private int normalizeShift(int shift) {
        shift = shift % alphabetSize;
        if (shift < 0) shift += alphabetSize;
        return shift;
    }

    /**
     * Поиск индекса символа в алфавите
     * @param c символ для поиска
     * @return индекс символа или -1 если не найден
     */
    private int findCharIndex(char c) {
        // Оптимизация: линейный поиск для небольшого алфавита
        for (int i = 0; i < alphabetSize; i++) {
            if (alphabet[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
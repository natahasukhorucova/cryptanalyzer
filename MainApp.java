import java.util.Scanner;

public class MainApp {
    private static final String ALPHABET = "абвгдежзийклмнопрстуфхцчшщъыьэюя.,«»\"':!? ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileManager fileManager = new FileManager();
        Validator validator = new Validator();
        Cipher cipher = new Cipher(ALPHABET.toCharArray());
        BruteForce bruteForce = new BruteForce(ALPHABET.toCharArray());
        StatisticalAnalyzer statisticalAnalyzer = new StatisticalAnalyzer(ALPHABET.toCharArray());

        while (true) {
            System.out.println("\n=== КРИПТОАНАЛИЗАТОР - ШИФР ЦЕЗАРЯ ===");
            System.out.println("1. Шифрование текста");
            System.out.println("2. Расшифровка с известным ключом");
            System.out.println("3. Brute Force (перебор всех ключей)");
            System.out.println("4. Статистический анализ");
            System.out.println("0. Выход");
            System.out.print("Выберите режим: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    encryptMode(scanner, fileManager, validator, cipher);
                    break;
                case 2:
                    decryptMode(scanner, fileManager, validator, cipher);
                    break;
                case 3:
                    bruteForceMode(scanner, fileManager, validator, bruteForce);
                    break;
                case 4:
                    statisticalMode(scanner, fileManager, validator, statisticalAnalyzer, cipher);
                    break;
                case 0:
                    System.out.println("До свидания!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private static void encryptMode(Scanner scanner, FileManager fileManager,
                                    Validator validator, Cipher cipher) {
        System.out.print("Введите путь к исходному файлу: ");
        String inputFile = scanner.nextLine();

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не существует!");
            return;
        }

        System.out.print("Введите путь для сохранения результата: ");
        String outputFile = scanner.nextLine();

        if (!validator.isValidOutputPath(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для сохранения!");
            return;
        }

        System.out.print("Введите ключ шифрования (сдвиг): ");
        int key = getIntInput(scanner);

        String text = fileManager.readFile(inputFile);
        if (text == null) return;

        String encrypted = cipher.encrypt(text, key);
        fileManager.writeFile(encrypted, outputFile);
        System.out.println("Шифрование завершено! Результат сохранен в: " + outputFile);
    }

    private static void decryptMode(Scanner scanner, FileManager fileManager,
                                    Validator validator, Cipher cipher) {
        System.out.print("Введите путь к зашифрованному файлу: ");
        String inputFile = scanner.nextLine();

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не существует!");
            return;
        }

        System.out.print("Введите путь для сохранения результата: ");
        String outputFile = scanner.nextLine();

        if (!validator.isValidOutputPath(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для сохранения!");
            return;
        }

        System.out.print("Введите ключ расшифровки: ");
        int key = getIntInput(scanner);

        String text = fileManager.readFile(inputFile);
        if (text == null)


            return;

        String decrypted = cipher.decrypt(text, key);
        fileManager.


                writeFile(decrypted, outputFile);
        System.out.println("Расшифровка завершена! Результат сохранен в: " + outputFile);
    }

    private static void bruteForceMode(Scanner scanner, FileManager fileManager,
                                       Validator validator, BruteForce bruteForce) {
        System.out.print("Введите путь к зашифрованному файлу: ");
        String inputFile = scanner.nextLine();

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не существует!");
            return;
        }

        System.out.print("Введите путь для сохранения результата: ");
        String outputFile = scanner.nextLine();

        if (!validator.isValidOutputPath(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для сохранения!");
            return;
        }

        System.out.print("Введите путь к файлу-образцу (Enter для пропуска): ");
        String sampleFile = scanner.nextLine();
        if (sampleFile.trim().isEmpty()) sampleFile = null;

        String encryptedText = fileManager.readFile(inputFile);
        if (encryptedText == null) return;

        String result = bruteForce.decryptByBruteForce(encryptedText, sampleFile);
        fileManager.writeFile(result, outputFile);
        System.out.println("Brute Force завершен! Результат сохранен в: " + outputFile);
    }

    private static void statisticalMode(Scanner scanner, FileManager fileManager,
                                        Validator validator, StatisticalAnalyzer analyzer,
                                        Cipher cipher) {
        System.out.print("Введите путь к зашифрованному файлу: ");
        String inputFile = scanner.nextLine();

        if (!validator.isFileExists(inputFile)) {
            System.out.println("Ошибка: файл не существует!");
            return;
        }

        System.out.print("Введите путь для сохранения результата: ");
        String outputFile = scanner.nextLine();

        if (!validator.isValidOutputPath(outputFile)) {
            System.out.println("Ошибка: недопустимый путь для сохранения!");
            return;
        }

        System.out.print("Введите путь к файлу-образцу (Enter для пропуска): ");
        String sampleFile = scanner.nextLine();
        if (sampleFile.trim().isEmpty()) sampleFile = null;

        String encryptedText = fileManager.readFile(inputFile);
        if (encryptedText == null) return;

        int bestShift = analyzer.findMostLikelyShift(encryptedText, sampleFile);
        System.out.println("Наиболее вероятный ключ: " + bestShift);

        String decrypted = cipher.decrypt(encryptedText, bestShift);
        fileManager.writeFile(decrypted, outputFile);
        System.out.println("Статистический анализ завершен! Результат сохранен в: " + outputFile);
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Введите корректное число: ");
            }
        }
    }
}



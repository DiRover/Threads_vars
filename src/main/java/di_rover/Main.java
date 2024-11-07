package di_rover;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger countTree = new AtomicInteger();
    static AtomicInteger countFour = new AtomicInteger();
    static AtomicInteger countFive = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread threadTree = new Thread(() -> {
            for (String nick : texts) {
                if (nick.length() == 3 && commonCheck(nick)) {
                    countTree.incrementAndGet();
                }
            }
        });

        Thread threadFour = new Thread(() -> {
            for (String nick : texts) {
                if (nick.length() == 4 && commonCheck(nick)) {
                    countFour.incrementAndGet();
                }
            }
        });

        Thread threadFive = new Thread(() -> {
            for (String nick : texts) {
                if (nick.length() == 5 && commonCheck(nick)) {
                    countFive.incrementAndGet();
                }
            }
        });

        threadTree.start();
        threadFour.start();
        threadFive.start();

        threadTree.join();
        threadFour.join();
        threadFive.join();

        System.out.println("Tree: " + countTree.get());
        System.out.println("Four: " + countFour.get());
        System.out.println("Five: " + countFive.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean commonCheck(String nick) {
        return checkPolidrom(nick, 0, nick.length() - 1) || checkMadeOfOneLetter(nick) || checkAscending(nick);
    }

    public static boolean checkPolidrom(String word, int left, int right) {
        // Базовый случай: если указатели встретились или пересеклись
        if (left >= right) {
            return true;
        }

        // Проверка текущих символов
        if (word.charAt(left) != word.charAt(right)) {
            return false;
        }

        // Рекурсивный вызов для следующей пары символов
        return checkPolidrom(word, left + 1, right - 1);
    }

    public static boolean checkMadeOfOneLetter(String word) {
        // Базовый случай: если длина слова 1, все символы одинаковы
        if (word.length() == 1) {
            return true;
        }

        // Проверяем, совпадает ли первый символ со вторым
        if (word.charAt(0) != word.charAt(1)) {
            return false;
        }

        // Рекурсивный вызов, проверка оставшейся части строки
        return checkMadeOfOneLetter(word.substring(1));
    }

    public static boolean checkAscending(String word) {
        // Базовый случай: если остался один символ, все символы ранее были по возрастанию
        if (word.length() == 1) {
            return true;
        }

        // Сравнение первого символа со следующим
        if (word.charAt(0) >= word.charAt(1)) {
            return false;
        }

        // Рекурсивный вызов для проверки оставшейся части строки
        return checkAscending(word.substring(1));
    }
}
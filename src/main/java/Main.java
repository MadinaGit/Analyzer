import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {
    protected static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    protected static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    protected static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);


    public static void main(String[] args) throws InterruptedException {

        String[] texts = new String[10000];

        Thread thread = new Thread(() -> {

            try {
                for (int i = 0; i < texts.length; i++) {

                    String text = generateText("abc", 100000);
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();


        Thread threadA = new Thread(() -> maxCountLetters(queue1, 'a', texts.length));

        Thread threadB = new Thread(() -> maxCountLetters(queue2, 'b', texts.length));

        Thread threadC = new Thread(() -> maxCountLetters(queue3, 'c', texts.length));

        threadA.start();
        threadB.start();
        threadC.start();

        thread.join();

        threadA.join();
        threadB.join();
        threadC.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countLetters(char ch, String text) {

        int count = 0;
        for (char currentLetter : text.toCharArray()) {
            if (currentLetter == ch) {
                count++;
            }
        }
        return count;
    }

    public static void maxCountLetters(BlockingQueue<String> queue, char ch, int allText) {
        int maxCount = 0;
        try {
            for (int i = 0; i < allText; i++) {
                String text = queue.take();
                int currentCount = countLetters(ch, text);
                if (currentCount > maxCount) {
                    maxCount = currentCount;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Максимальное количество букв '" + ch + "': " + maxCount);
        }
    }
}

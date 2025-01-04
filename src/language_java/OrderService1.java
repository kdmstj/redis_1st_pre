package language_java;

import java.util.HashMap;
import java.util.Map;

public class OrderService1 {

    private final Map<String, Integer> productDatabase = new HashMap<>();
    private final Map<String, OrderInfo> latestOrderDatabase = new HashMap<>();

    public OrderService1() {
        productDatabase.put("apple", 100);
        productDatabase.put("banana", 50);
        productDatabase.put("orange", 75);
    }

    public synchronized void orderUsingSynchronized(String productName, int amount) {
        Integer currentStock = getStock(productName);

        simulateDelay();

        if (currentStock >= amount) {
            System.out.println("Current Thread : " + Thread.currentThread().getName() +
                    " - CurrentStock : " + currentStock + " - Order : " + amount);
            productDatabase.put(productName, currentStock - amount);
            latestOrderDatabase.put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
        }
    }

    public void orderUsingSynchronized2(String productName, int amount) {
        synchronized (this) {
            Integer currentStock = getStock(productName);

            simulateDelay();

            if (currentStock >= amount) {
                System.out.println("Current Thread : " + Thread.currentThread().getName() +
                        " - CurrentStock : " + currentStock + " - Order : " + amount);
                productDatabase.put(productName, currentStock - amount);
                latestOrderDatabase.put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
            }
        }
    }

    public Integer getStock(String productName) {
        return productDatabase.getOrDefault(productName, 0);
    }

    private static void simulateDelay() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

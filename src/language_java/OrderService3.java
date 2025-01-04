package language_java;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService3 {

    private final Map<String, Integer> productDatabase = new ConcurrentHashMap<>();
    private final Map<String, OrderInfo> latestOrderDatabase = new ConcurrentHashMap<>();

    public OrderService3() {
        productDatabase.put("apple", 100);
        productDatabase.put("banana", 50);
        productDatabase.put("orange", 75);
    }

    public void order(String productName, int amount) {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        productDatabase.compute(productName, (key, currentStock) -> {
            if (currentStock == null || currentStock < amount) {
                return currentStock;
            }
            System.out.println("Current Thread : " + Thread.currentThread().getName() +
                    " - CurrentStock : " + currentStock + " - Order : " + amount);
            latestOrderDatabase.put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
            return currentStock - amount;
        });
    }

    public int getStock(String productName) {
        return productDatabase.getOrDefault(productName, 0);
    }
}

package language_java;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderService2 {

    private final Map<String, AtomicInteger> productDatabase = new HashMap<>();
    private final Map<String, OrderInfo> latestOrderDatabase = new HashMap<>();

    public OrderService2() {
        productDatabase.put("apple", new AtomicInteger(100));
        productDatabase.put("banana", new AtomicInteger(50));
        productDatabase.put("orange", new AtomicInteger(75));
    }

    public void order(String productName, int amount) {
        AtomicInteger currentStock = getStock(productName);

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        while (true) {
            System.out.println("Current Thread : " + Thread.currentThread().getName() +
                    " - currentStock : " + currentStock + " - Order : " + amount);
            int current = currentStock.get();
            if (current < amount) {
                return;
            }

            boolean success = currentStock.compareAndSet(current, current - amount);
            if (success) {
                System.out.println("Current Thread : " + Thread.currentThread().getName() +
                        " - Stock before order: " + current + " - Order: " + amount +
                        " - Stock after order: " + currentStock.get());
                latestOrderDatabase.put(productName, new OrderInfo(productName, amount, System.currentTimeMillis()));
                break;
            }
        }
    }

    public AtomicInteger getStock(String productName) {
        return productDatabase.getOrDefault(productName, new AtomicInteger(0));
    }
}

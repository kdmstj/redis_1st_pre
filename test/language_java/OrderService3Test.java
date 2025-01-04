package language_java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("여러 스레드가 동시에 주문을 요청할 때")
public class OrderService3Test {

    private final OrderService3 service = new OrderService3();

    @Test
    @DisplayName("ConcurrentHashMap 을 사용하면, 재고가 일치한다.")
    void givenMultipleThreads_whenUsingConcurrentHashMap_thenEqualsExpectedStock() throws InterruptedException {
        String productName = "apple";
        int initialStock = service.getStock(productName);

        int orderAmount = 8;
        int threadCount = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    service.order(productName, orderAmount);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        int expectedStock = initialStock % orderAmount;
        int actualStock = service.getStock(productName);

        System.out.println("Expected Stock : " + expectedStock + ", Actual Stock : " + actualStock);

        assertEquals(expectedStock, actualStock);
    }
}
package language_java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("여러 스레드가 동시에 주문을 요청할 때")
class BeforeOrderServiceTest {

    private final BeforeOrderService service = new BeforeOrderService();

    @Test
    @DisplayName("동시성 이슈로 재고 불일치가 발생한다.")
    void givenMultipleThreads_whenConcurrentOrders_thenNotEqualsExpectedStock() throws InterruptedException {
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

        assertNotEquals(expectedStock, actualStock);
    }
}

package language_java;

public class OrderInfo {
    String productName;
    Integer amount;
    Long timestamp;

    OrderInfo(String productName, Integer amount, Long timestamp) {
        this.productName = productName;
        this.amount = amount;
        this.timestamp = timestamp;
    }

}

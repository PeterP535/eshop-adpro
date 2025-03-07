package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class PaymentTest {

    private Order order;

    @BeforeEach
    void setUp() {
        // Prepare a dummy product and order (Payment requires a non-empty order)
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("dummy-product-id");
        product.setProductName("Dummy Product");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("dummy-order-id", products, 1708560000L, "TestAuthor");
    }

    @Test
    void testPaymentBuilderAndGetters() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = Payment.builder()
                .id("payment-id-123")
                .order(order)
                .method("Voucher Code")
                .status("SUCCESS")
                .paymentData(paymentData)
                .build();

        assertEquals("payment-id-123", payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testSetStatus() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Test Street");
        paymentData.put("deliveryFee", "5000");

        Payment payment = Payment.builder()
                .id("payment-id-456")
                .order(order)
                .method("Cash On Delivery")
                .status("WAITING_PAYMENT")
                .paymentData(paymentData)
                .build();

        // Change the status using setter
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }
}

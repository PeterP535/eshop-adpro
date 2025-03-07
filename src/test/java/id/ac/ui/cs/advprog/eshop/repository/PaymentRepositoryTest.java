package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        // Prepare a dummy order for Payment objects
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("dummy-product-id");
        product.setProductName("Dummy Product");
        product.setProductQuantity(1);
        products.add(product);
        order = new Order("dummy-order-id", products, 1708560000L, "TestAuthor");
    }

    @Test
    void testSaveCreate() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = Payment.builder()
                .id("payment-id-1")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.SUCCESS.getValue())
                .paymentData(paymentData)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        Payment retrievedPayment = paymentRepository.findById("payment-id-1");

        assertNotNull(retrievedPayment);
        assertEquals(payment.getId(), retrievedPayment.getId());
        assertEquals(payment.getMethod(), retrievedPayment.getMethod());
        assertEquals(payment.getStatus(), retrievedPayment.getStatus());
        assertEquals(payment.getPaymentData(), retrievedPayment.getPaymentData());
        assertEquals(payment.getOrder(), retrievedPayment.getOrder());
    }

    @Test
    void testSaveUpdate() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = Payment.builder()
                .id("payment-id-2")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData)
                .build();

        paymentRepository.save(payment);

        // Update the payment status
        Payment updatedPayment = Payment.builder()
                .id("payment-id-2")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.SUCCESS.getValue())
                .paymentData(paymentData)
                .build();

        Payment result = paymentRepository.save(updatedPayment);
        Payment retrievedPayment = paymentRepository.findById("payment-id-2");

        assertNotNull(retrievedPayment);
        assertEquals(PaymentStatus.SUCCESS.getValue(), retrievedPayment.getStatus());
        assertEquals(updatedPayment.getId(), retrievedPayment.getId());
    }

    @Test
    void testFindByIdIfFound() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Test Street");
        paymentData.put("deliveryFee", "5000");

        Payment payment = Payment.builder()
                .id("payment-id-3")
                .order(order)
                .method("Cash On Delivery")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData)
                .build();

        paymentRepository.save(payment);
        Payment foundPayment = paymentRepository.findById("payment-id-3");
        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
    }

    @Test
    void testFindByIdIfNotFound() {
        Payment foundPayment = paymentRepository.findById("non-existing-id");
        assertNull(foundPayment);
    }

    @Test
    void testFindAll() {
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = Payment.builder()
                .id("payment-id-4")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.SUCCESS.getValue())
                .paymentData(paymentData1)
                .build();

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("address", "123 Test Street");
        paymentData2.put("deliveryFee", "5000");
        Payment payment2 = Payment.builder()
                .id("payment-id-5")
                .order(order)
                .method("Cash On Delivery")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData2)
                .build();

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
    }
}

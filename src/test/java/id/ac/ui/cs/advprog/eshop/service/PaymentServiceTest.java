package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        // Prepare a dummy order with one product for Payment tests
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("dummy-product-id");
        product.setProductName("Dummy Product");
        product.setProductQuantity(1);
        products.add(product);
        order = new Order("dummy-order-id", products, 1708560000L, "TestAuthor");
    }

    @Test
    void testAddPaymentVoucherCodeValid() {
        Map<String, String> paymentData = new HashMap<>();
        // Valid voucher code: 16 characters, starts with "ESHOP", exactly 8 digits
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        when(paymentRepository.save(ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Voucher Code", paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals("Voucher Code", payment.getMethod());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testAddPaymentVoucherCodeInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        // Invalid voucher code (wrong length)
        paymentData.put("voucherCode", "INVALIDCODE");

        when(paymentRepository.save(ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Voucher Code", paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testAddPaymentCashOnDeliveryValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Test Street");
        paymentData.put("deliveryFee", "5000");

        when(paymentRepository.save(ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Cash On Delivery", paymentData);
        // For valid Cash On Delivery, the default status "WAITING_PAYMENT" remains
        assertEquals(PaymentStatus.WAITING_PAYMENT.getValue(), payment.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testAddPaymentCashOnDeliveryInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        // Missing address should cause rejection
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "5000");

        when(paymentRepository.save(ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "Cash On Delivery", paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = Payment.builder()
                .id("payment-id-test")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData)
                .build();

        when(paymentRepository.save(ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Update payment status to SUCCESS
        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), updatedPayment.getStatus());
        // Verify that the related order's status is updated to SUCCESS via OrderService
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
    }

    @Test
    void testSetStatusRejected() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Test Street");
        paymentData.put("deliveryFee", "5000");
        Payment payment = Payment.builder()
                .id("payment-id-test2")
                .order(order)
                .method("Cash On Delivery")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData)
                .build();

        when(paymentRepository.save(ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), updatedPayment.getStatus());
        // Verify that the related order's status is updated to FAILED via OrderService
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.FAILED.getValue());
    }

    @Test
    void testSetStatusInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = Payment.builder()
                .id("payment-id-test3")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData)
                .build();

        // Attempting to set an invalid status should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () ->
                paymentService.setStatus(payment, "INVALID_STATUS")
        );
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetPaymentFound() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = Payment.builder()
                .id("payment-id-get")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.SUCCESS.getValue())
                .paymentData(paymentData)
                .build();

        when(paymentRepository.findById("payment-id-get")).thenReturn(payment);
        Payment foundPayment = paymentService.getPayment("payment-id-get");
        assertNotNull(foundPayment);
        assertEquals("payment-id-get", foundPayment.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        when(paymentRepository.findById("non-existent")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () ->
                paymentService.getPayment("non-existent")
        );
    }

    @Test
    void testGetAllPayments() {
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = Payment.builder()
                .id("payment-id-1")
                .order(order)
                .method("Voucher Code")
                .status(PaymentStatus.SUCCESS.getValue())
                .paymentData(paymentData1)
                .build();

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("address", "123 Test Street");
        paymentData2.put("deliveryFee", "5000");
        Payment payment2 = Payment.builder()
                .id("payment-id-2")
                .order(order)
                .method("Cash On Delivery")
                .status(PaymentStatus.WAITING_PAYMENT.getValue())
                .paymentData(paymentData2)
                .build();

        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
        assertEquals("payment-id-1", result.get(0).getId());
        assertEquals("payment-id-2", result.get(1).getId());
    }
}

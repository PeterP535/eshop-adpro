package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = generatePaymentId();
        // Use PaymentStatus for the default status.
        String status = PaymentStatus.WAITING_PAYMENT.getValue();

        if ("Voucher Code".equalsIgnoreCase(method)) {
            String voucherCode = paymentData.get("voucherCode");
            // Validate voucher code:
            // - Must be 16 characters long.
            // - Must start with "ESHOP".
            // - Must contain exactly 8 numerical characters.
            if (voucherCode != null
                    && voucherCode.length() == 16
                    && voucherCode.startsWith("ESHOP")
                    && countDigits(voucherCode) == 8) {
                status = PaymentStatus.SUCCESS.getValue();
            } else {
                status = PaymentStatus.REJECTED.getValue();
            }
        } else if ("Cash On Delivery".equalsIgnoreCase(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            // Validate that neither address nor delivery fee is empty.
            if (address == null || address.trim().isEmpty()
                    || deliveryFee == null || deliveryFee.trim().isEmpty()) {
                status = PaymentStatus.REJECTED.getValue();
            }
        }

        Payment payment = Payment.builder()
                .id(paymentId)
                .order(order)
                .method(method)
                .status(status)
                .paymentData(paymentData)
                .build();

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        // Validate status using the PaymentStatus enum.
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        payment.setStatus(status);

        // When payment status is updated, also update the related order's status:
        // - If payment status is "SUCCESS", update order status to "SUCCESS".
        // - If payment status is "REJECTED", update order status to "FAILED".
        Order order = payment.getOrder();
        if (order != null) {
            if (PaymentStatus.SUCCESS.getValue().equalsIgnoreCase(status)) {
                orderService.updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
            } else if (PaymentStatus.REJECTED.getValue().equalsIgnoreCase(status)) {
                orderService.updateStatus(order.getId(), OrderStatus.FAILED.getValue());
            }
        }
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new NoSuchElementException("Payment not found with ID: " + paymentId);
        }
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Helper method to count the number of digits in a string.
    private int countDigits(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                count++;
            }
        }
        return count;
    }

    // Helper method to generate a unique payment ID.
    private String generatePaymentId() {
        return UUID.randomUUID().toString();
    }
}

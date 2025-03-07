package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class Payment {
    private String id;
    private String method;             // e.g. "Voucher Code" or "Cash On Delivery"
    private String status;             // e.g. "WAITING_PAYMENT", "SUCCESS", or "REJECTED"
    private Map<String, String> paymentData;
    private Order order;               // The related order
}

package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    private List<Product> products;

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId("c5b58f9f-1c39-460b-88e0-71afef63fdb6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);

        Product product2 = new Product();
        product2.setProductId("2c462328-63d7-4d59-a8c7-f32db82d5563");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);

        this.products.add(product1);
        this.products.add(product2);
    }

    @Test
    void testCreatorOrderEmptyProduct() {
        // Kosongkan list agar memicu IllegalArgumentException
        this.products.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            new Order(
                    "13525256-012a-4c07-b546-51e0b196d70b",
                    this.products,
                    1708560000L,
                    "Safira Sundajat"
            );
        });
    }

    @Test
    void testCreateOrderDefaultStatus() {
        // Tidak mengirim status -> default "WAITING_PAYMENT"
        Order order = new Order(
                "13525256-012a-4c07-b546-51e0b1396d70b",
                this.products,
                1708560001L,
                "Safira Sundajat"
        );

        assertSame(this.products, order.getProducts());
        assertEquals(2, order.getProducts().size());
        assertEquals("Sampo Cap Bambang", order.getProducts().get(0).getProductName());
        assertEquals("Sabun Cap Usep", order.getProducts().get(1).getProductName());

        assertEquals("13525256-012a-4c07-b546-51e0b1396d70b", order.getId());
        assertEquals(1708560001L, order.getOrderTime());
        assertEquals("Safira Sundajat", order.getAuthor());
        // Cek status default dari enum
        assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
    }

    @Test
    void testCreateOrderSuccessStatus() {
        // Mengirim status valid: "SUCCESS"
        Order order = new Order(
                "13525256-012a-4c07-b546-51e0b1396d70b",
                this.products,
                1708560001L,
                "Safira Sundajat",
                OrderStatus.SUCCESS.name()
        );

        assertEquals(OrderStatus.SUCCESS.name(), order.getStatus());
    }

    @Test
    void testCreateOrderInvalidStatus() {
        // Mengirim status string yang bukan bagian enum
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(
                    "13525256-012a-4c07-b546-51e0b1396d70b",
                    this.products,
                    1708560001L,
                    "Safira Sundajat",
                    "KEUN"
            );
        });
    }

    @Test
    void testSetStatusToCancelled() {
        // Buat order default
        Order order = new Order(
                "13525256-012a-4c07-b546-51e0b1396d70b",
                this.products,
                1708560001L,
                "Safira Sundajat"
        );

        // Set status valid: "CANCELLED"
        order.setStatus(OrderStatus.CANCELLED.name());
        assertEquals(OrderStatus.CANCELLED.name(), order.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        // Buat order default
        Order order = new Order(
                "13525256-012a-4c07-b546-51e0b1396d70b",
                this.products,
                1708560001L,
                "Safira Sundajat"
        );

        // Set status invalid
        assertThrows(IllegalArgumentException.class, () -> order.setStatus("MEUW"));
    }


}
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;


    @Mock
    private OrderRepository orderRepository;

    private List<Order> orders;

    @BeforeEach
    void setUp() {
        // Siapkan data contoh
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("c5b58f9f-1c39-460b-8860-71afef63fdb6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);

        products.add(product1);

        orders = new ArrayList<>();

        Order order1 = new Order(
                "eb5589ef-1c39-460b-8860-71afef63fdb6",
                products,
                1708560001L,
                "Safira Sundajat"
        );

        Order order2 = new Order(
                "7f99e15b-45b1-42f4-eebc-3c4b75ac79b8",
                products,
                1708570001L,
                "Safira Sundajat"
        );

        orders.add(order1);
        orders.add(order2);
    }


    @Test
    void testCreateOrder() {
        Order order = orders.get(0);

        // Mock repository agar saat save() dipanggil, mengembalikan order yang sama
        doReturn(order).when(orderRepository).save(order);

        // Panggil service
        Order result = orderService.createOrder(order);

        // Verifikasi metode save() terpanggil
        verify(orderRepository, times(1)).save(order);

        // Pastikan data hasil sama
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getAuthor(), result.getAuthor());
        assertEquals(order.getStatus(), result.getStatus());
    }


    @Test
    void testCreateOrderIfAlreadyExists() {
        Order order = orders.get(0);
        doReturn(order).when(orderRepository).findById(order.getId());
        Order result = orderService.createOrder(order);
        assertNull(result);
        verify(orderRepository, never()).save(any(Order.class));
    }


    @Test
    void testUpdateStatus() {
        Order order = orders.get(0);
        // Mock agar repository bisa menemukan order
        doReturn(order).when(orderRepository).findById(order.getId());
        // Mock agar save() mengembalikan order setelah di-update
        doReturn(order).when(orderRepository).save(any(Order.class));

        // Panggil service untuk update status
        Order result = orderService.updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());

        // Verifikasi save terpanggil sekali
        verify(orderRepository, times(1)).save(any(Order.class));
        // Cek status sudah berubah
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getStatus());
    }

    /**
     * Create unhappy path test:
     * Use updateStatus() to update status of an order using invalid status string.
     */
    @Test
    void testUpdateStatusInvalidStatus() {
        Order order = orders.get(0);
        doReturn(order).when(orderRepository).findById(order.getId());

        // Seharusnya lempar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () ->
                orderService.updateStatus(order.getId(), "MEOW")
        );

        // Verifikasi repository.save(...) tidak pernah terpanggil
        verify(orderRepository, never()).save(any(Order.class));
    }

    /**
     * Create an unhappy path test:
     * Use updateStatus() with an ID that is never used.
     */
    @Test
    void testUpdateStatusInvalidOrderId() {
        // ID "zzcc" tidak ada
        doReturn(null).when(orderRepository).findById("zzcc");

        // Seharusnya lempar NoSuchElementException (atau sesuai implementasi)
        assertThrows(NoSuchElementException.class, () ->
                orderService.updateStatus("zzcc", OrderStatus.SUCCESS.getValue())
        );

        verify(orderRepository, never()).save(any(Order.class));
    }

    /**
     * Create a happy path test:
     * Use findById() to find order using a valid ID.
     */
    @Test
    void testFindByIdIfFound() {
        Order order = orders.get(0);
        doReturn(order).when(orderRepository).findById(order.getId());

        Order result = orderService.findById(order.getId());
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
    }

    /**
     * Create an unhappy path test:
     * Use findById() to find order using an ID that is never used.
     */
    @Test
    void testFindByIdIfNotFound() {
        doReturn(null).when(orderRepository).findById("zzcc");

        Order result = orderService.findById("zzcc");
        assertNull(result);
    }

    /**
     * Create a happy path test:
     * Use findAllByAuthor() to find orders using an author that exists.
     */
    @Test
    void testFindAllByAuthorIfAuthorCorrect() {
        Order order = orders.get(0);

        // Mock repository agar findAllByAuthor() mengembalikan 2 data
        doReturn(orders).when(orderRepository).findAllByAuthor(order.getAuthor());

        List<Order> results = orderService.findAllByAuthor(order.getAuthor());
        // Pastikan isinya sesuai
        for (Order o : results) {
            assertEquals(order.getAuthor(), o.getAuthor());
        }
        assertEquals(2, results.size());
    }

    /**
     * Create an unhappy path test:
     * Use findAllByAuthor() to find orders using an author in all-lowercase
     * (asumsi implementasi repository case-sensitive).
     */
    @Test
    void testFindAllByAuthorIfAllLowercase() {
        Order order = orders.get(1);

        // Mock: bila dicari toLowerCase() -> kembalikan list kosong
        doReturn(new ArrayList<>()).when(orderRepository)
                .findAllByAuthor(order.getAuthor().toLowerCase());

        List<Order> results = orderService.findAllByAuthor(order.getAuthor().toLowerCase());
        assertTrue(results.isEmpty());
    }
}
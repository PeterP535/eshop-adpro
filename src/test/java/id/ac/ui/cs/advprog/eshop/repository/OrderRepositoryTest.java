package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class OrderRepositoryTest {

    OrderRepository orderRepository;
    List<Order> orders;

    @BeforeEach
    void setUp() {
        // Inisialisasi repository
        orderRepository = new OrderRepository();

        // Siapkan daftar produk
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("c5b58f9f-1c39-460b-88e0-71afef63fdb6");
        product1.setProductName("Sampo Cap Bambang");
        products.add(product1);

        // Siapkan daftar order
        orders = new ArrayList<>();

        Order order1 = new Order(
                "13625256-012a-4c07-b546-51e0b1396d70b",
                products,
                1708560001L,
                "Safira Sundajat"
        );
        orders.add(order1);

        Order order2 = new Order(
                "79e15bb-45b1-42f4-eebc-3c4b75ac7b19",
                products,
                1708560002L,
                "Safira Sundajat"
        );
        orders.add(order2);

        Order order3 = new Order(
                "33e4f8f2-6138-4eba-8ee7-7ecbfee",
                products,
                1708560003L,
                "Bambang Sudrajat"
        );
        orders.add(order3);
    }

    @Test
    void testSaveCreate() {
        // Ambil order kedua dari list (index 1)
        Order order = orders.get(1);

        // Simpan order ke repository
        Order result = orderRepository.save(order);

        // Cari kembali order yang baru disimpan
        Order findResult = orderRepository.findById(orders.get(1).getId());

        // Bandingkan semua field untuk memastikan data tersimpan benar
        assertEquals(order.getId(), findResult.getId());
        assertEquals(order.getOrderTime(), findResult.getOrderTime());
        assertEquals(order.getAuthor(), findResult.getAuthor());
        assertEquals(order.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Order order = orders.get(1);
        orderRepository.save(order);

        Order newOrder = new Order(
                order.getId(),
                order.getProducts(),
                order.getOrderTime(),
                order.getAuthor(),
                OrderStatus.SUCCESS.getValue()
        );
        Order result = orderRepository.save(newOrder);

        Order findResult = orderRepository.findById(orders.get(1).getId());
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getId(), findResult.getId());
        assertEquals(order.getOrderTime(), findResult.getOrderTime());
        assertEquals(order.getAuthor(), findResult.getAuthor());
        assertEquals(OrderStatus.SUCCESS.getValue(), findResult.getStatus());
    }


    @Test
    void testFindByIdIfFound() {
        // Simpan semua order ke repository
        for (Order order : orders) {
            orderRepository.save(order);
        }

        // Ambil order kedua (index 1) dari list orders
        Order findResult = orderRepository.findById(orders.get(1).getId());

        // Pastikan data ditemukan dan sesuai
        assertEquals(orders.get(1).getId(), findResult.getId());
        assertEquals(orders.get(1).getOrderTime(), findResult.getOrderTime());
        assertEquals(orders.get(1).getAuthor(), findResult.getAuthor());
        assertEquals(orders.get(1).getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfNotFound() {
        // Simpan semua order ke repository
        for (Order order : orders) {
            orderRepository.save(order);
        }

        // Cari ID yang tidak ada di repository
        Order findResult = orderRepository.findById("zzcc");
        assertNull(findResult);
    }

    @Test
    void testFindAllByAuthorIfAuthorCorrect() {
        // Simpan semua order ke repository
        for (Order order : orders) {
            orderRepository.save(order);
        }

        // Ambil author dari order kedua (index 1), lalu cari
        List<Order> orderList = orderRepository.findAllByAuthor(orders.get(1).getAuthor());
        assertEquals(2, orderList.size());
    }

    @Test
    void testFindAllByAuthorIfAllLowercase() {
        // Simpan hanya order kedua (index 1)
        orderRepository.save(orders.get(1));

        // Cari dengan author yang sudah di-lowercase
        List<Order> orderList = orderRepository.findAllByAuthor(orders.get(1).getAuthor().toLowerCase());
        assertTrue(orderList.isEmpty());
    }




}
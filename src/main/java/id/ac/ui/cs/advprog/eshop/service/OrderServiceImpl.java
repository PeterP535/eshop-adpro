package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        // Cek apakah order dengan ID ini sudah ada
        if (orderRepository.findById(order.getId()) == null) {
            // Jika belum ada, simpan
            return orderRepository.save(order);
        }
        // Jika sudah ada, kembalikan null (sesuai test "already existing Order")
        return null;
    }

    @Override
    public Order updateStatus(String orderId, String status) {
        // Cari order di repository
        Order existingOrder = orderRepository.findById(orderId);
        if (existingOrder == null) {
            // Jika tidak ditemukan, lempar NoSuchElementException
            throw new NoSuchElementException("Order not found with ID: " + orderId);
        }

        // Set status (jika status invalid, Order.setStatus() akan lempar IllegalArgumentException)
        existingOrder.setStatus(status);

        // Simpan perubahan
        orderRepository.save(existingOrder);
        return existingOrder;
    }

    @Override
    public Order findById(String orderId) {
        // Cari langsung di repository
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findAllByAuthor(String author) {
        // Delegasi ke repository
        return orderRepository.findAllByAuthor(author);
    }
}
package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductTest {
    Product product;

    @BeforeEach
    void setup() {
        this.product = new Product();
        this.product.setProductId("123e4567-e89b-12d3-a456-556642440000");
        this.product.setProductName("Product 1");
        this.product.setProductQuantity(100);
    }

    @Test
    public void testGetProductId() {
        assert(this.product.getProductId().equals("123e4567-e89b-12d3-a456-556642440000"));
    }

    @Test
    public void testGetProductName() {
        assert(this.product.getProductName().equals("Product 1"));
    }

    @Test
    public void testGetProductQuantity() {
        assert(this.product.getProductQuantity() == 100);
    }
}
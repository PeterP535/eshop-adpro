package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        productService = new ProductServiceImpl();
        // Inject the repository into the service via ReflectionTestUtils
        ReflectionTestUtils.setField(productService, "productRepository", productRepository);
    }

    @Test
    void testEditProduct() {
        // Create a product and add it to the repository
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Original Product");
        product.setProductQuantity(10);
        productService.create(product);

        // Update the product details
        product.setProductName("Updated Product");
        product.setProductQuantity(20);
        Product updatedProduct = productService.update(product);

        // Retrieve updated product and verify changes
        Product fetchedProduct = productService.getById("test-id");
        assertNotNull(fetchedProduct, "The product should be present after update.");
        assertEquals("Updated Product", fetchedProduct.getProductName(), "Product name should be updated.");
        assertEquals(20, fetchedProduct.getProductQuantity(), "Product quantity should be updated.");
    }

    @Test
    void testDeleteProduct() {
        // Create a product and add it to the repository
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Product to Delete");
        product.setProductQuantity(5);
        productService.create(product);

        // Delete the product
        productService.delete("test-id");

        // Verify the product is deleted
        Product fetchedProduct = productService.getById("test-id");
        assertNull(fetchedProduct, "The product should be null after deletion.");
    }
}
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository; // Mocked repository

    @InjectMocks
    private ProductServiceImpl productService; // Inject mock into service

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Initialize a sample product for testing
        testProduct = new Product();
        testProduct.setProductId("test-id");
        testProduct.setProductName("Original Product");
        testProduct.setProductQuantity(10);
    }

    @Test
    void testEditProduct() {
        // Remove the unnecessary stubbing for findById
        // when(productRepository.findById("test-id")).thenReturn(testProduct);

        // Stub the update method properly since it returns a Product
        when(productRepository.update(any(Product.class))).thenReturn(testProduct);

        // Modify product details
        testProduct.setProductName("Updated Product");
        testProduct.setProductQuantity(20);
        productService.update(testProduct);

        // Verify update logic
        assertEquals("Updated Product", testProduct.getProductName(), "Product name should be updated.");
        assertEquals(20, testProduct.getProductQuantity(), "Product quantity should be updated.");

        // Verify repository update method was called once
        verify(productRepository, times(1)).update(testProduct);
    }



    @Test
    void testDeleteProduct() {
        // Simulate finding product before deletion
        when(productRepository.findById("test-id")).thenReturn(testProduct);

        // Call delete method
        productService.delete("test-id");

        // Verify repository's delete method was called once
        verify(productRepository, times(1)).delete("test-id");

        // Simulate deleted product not found
        when(productRepository.findById("test-id")).thenReturn(null);

        // Verify the product is deleted
        Product fetchedProduct = productService.getById("test-id");
        assertNull(fetchedProduct, "The product should be null after deletion.");
    }
}

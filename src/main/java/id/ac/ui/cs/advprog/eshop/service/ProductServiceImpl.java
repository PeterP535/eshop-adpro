package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class ProductServiceImpl implements ProductService {

    private final IProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(java.util.UUID.randomUUID().toString());
        }
        return productRepository.create(product);
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product update(Product product) {
        productRepository.update(product);
        return product;
    }

    @Override
    public void delete(String id) {
        productRepository.delete(id);
    }
}

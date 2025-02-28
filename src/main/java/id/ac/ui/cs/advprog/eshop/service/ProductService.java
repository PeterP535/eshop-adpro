package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;

public interface ProductService extends ReadOnlyProductService {
    Product create(Product product);
    Product update(Product product);
    void delete(String id);
}

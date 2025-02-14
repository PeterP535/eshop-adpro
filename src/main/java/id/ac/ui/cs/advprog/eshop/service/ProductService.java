// ProductService.java
package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

public interface ProductService {
    Product create(Product product);
    List<Product> findAll();
    // New methods for edit feature
    Product getById(String id);
    Product update(Product product);
}
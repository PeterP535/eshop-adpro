// ProductRepository.java
package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String id) {
        for (Product p : productData) {
            if (p.getProductId() != null && p.getProductId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void update(Product product) {
        for (int i = 0; i < productData.size(); i++) {
            if (productData.get(i).getProductId() != null &&
                    productData.get(i).getProductId().equals(product.getProductId())) {
                productData.set(i, product);
                break;
            }
        }
    }
}
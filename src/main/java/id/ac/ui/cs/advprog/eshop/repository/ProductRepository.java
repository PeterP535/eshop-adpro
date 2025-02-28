package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

@Repository
public class ProductRepository implements IProductRepository {
    private List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public Product findById(String id) {
        return productData.stream()
                .filter(product -> product.getProductId() != null && product.getProductId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product update(Product product) {
        for (int i = 0; i < productData.size(); i++) {
            if (productData.get(i).getProductId() != null &&
                    productData.get(i).getProductId().equals(product.getProductId())) {
                productData.set(i, product);
                return product; // ✅ Return the updated product
            }
        }
        return null; // If product not found
    }



    @Override
    public void delete(String id) {
        productData.removeIf(product -> product.getProductId() != null && product.getProductId().equals(id));
    }
}

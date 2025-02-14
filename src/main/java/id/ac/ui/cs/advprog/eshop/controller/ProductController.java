// ProductController.java
package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product, Model model) {
        service.create(product);
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }

    // New endpoint to load edit page
    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable("id") String id, Model model) {
        Product product = service.getById(id);
        if (product == null) {
            // If not found, you can redirect or handle error accordingly
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "editProduct";
    }

    // New endpoint to handle edit form submission
    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product) {
        service.update(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        service.delete(id);
        return "redirect:/product/list";
    }

}
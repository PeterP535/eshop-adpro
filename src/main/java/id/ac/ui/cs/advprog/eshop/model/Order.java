package id.ac.ui.cs.advprog.eshop.model;

public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;
    String status;


    public Order(String id, List<Product> products, Long orderTime, String author) {

    }


    public Order(String id, List<Product> products, Long orderTime, String author, String status) {

    }


}

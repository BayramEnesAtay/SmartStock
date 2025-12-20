package com.inventory.dao;

import com.inventory.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In‑memory data access object for products.  Preloads a few
 * products for demonstration.  Provides CRUD operations and a
 * simple search.  Replace with JDBC calls when connecting to
 * PostgreSQL.
 */
public class ProductDAO {
    private static final List<Product> products = new ArrayList<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);
    static {
        products.add(new Product(nextId.getAndIncrement(), "Kalem", 100, 2.5));
        products.add(new Product(nextId.getAndIncrement(), "Defter", 60, 5.0));
        products.add(new Product(nextId.getAndIncrement(), "Silgi", 80, 1.5));
    }
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    public void addProduct(String name, int qty, double price) {
        products.add(new Product(nextId.getAndIncrement(), name, qty, price));
    }
    public void updateProduct(Product p) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == p.getId()) {
                products.set(i, p);
                return;
            }
        }
    }
    public void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }
    public List<Product> searchByName(String query) {
        String lower = query.toLowerCase();
        return products.stream().filter(p -> p.getName().toLowerCase().contains(lower)).collect(Collectors.toList());
    }
}
package com.grid.webdevelopment.service;

import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    public Product getItem(String id) {
        return productRepository.get(id);
    }

    public List<Product> getItems() {
        return productRepository.getAll();
    }

    public String saveItem(Product product) {
        productRepository.save(product);
        return "Success";
    }

}

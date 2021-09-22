package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.ProductNotFoundException;
import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id=%s not found", id)));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);

//        if (productRepository.productExists(product.getId())) {
//            Product productFromStore = getProductById(product.getId());
//            productFromStore.setAvailable(productFromStore.getAvailable() + product.getAvailable());
//            productRepository.save(productFromStore);
//            log.info("{} has been updated", productFromStore);
//            return;
//        }
//        productRepository.save(product);
//        log.info("{} has been saved", product);
    }

}

package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.BigQuantityException;
import com.grid.webdevelopment.exception.ProductNotFoundException;
import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartShow;
import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;
    private ProductService productService;

    public String addToCart(CartItem cartItem) {
        String id = cartItem.getId();
        boolean contains = cartRepository.getAll().keySet().contains(id);
        if (contains == true) return String.format("Product with id=%s is added already", id);
        int quantity = cartItem.getQuantity();
        Product product = productService.getItem(id);
        if (product == null) throw new ProductNotFoundException(String.format("Product with id=%s not found", id));
        if (product.getAvailable() >= quantity) {
            cartRepository.save(id, quantity);
            product.setAvailable(product.getAvailable() - quantity);
            productService.saveItem(product);
            return "Success";
        } else {
            throw new BigQuantityException(String.format("Available quantity is %s, but requested %s", product.getAvailable(), quantity));
        }
    }

    public List<CartShow> displayCart() {
        List<String> ids = cartRepository.getAll().keySet().stream().collect(Collectors.toList());

        List<CartShow> collect = IntStream.range(0, ids.size())
                .mapToObj(i -> genCartShow(i + 1, ids.get(i)))
                .collect(Collectors.toList());

        return collect;
    }

    public String deleteItem(CartItem cartItem) {
        String id = cartItem.getId();
        int quantity = cartItem.getQuantity();
        int quantityInCart = cartRepository.getQuantity(id);
        if (quantity >= quantityInCart) {
            cartRepository.delete(id, quantityInCart);
            Product product = productService.getItem(id);
            product.setAvailable(product.getAvailable() + quantityInCart);
            productService.saveItem(product);
            return "Deleted fully";
        }
        else {
            int diff = quantityInCart - quantity;
            cartRepository.save(id, diff);
            Product product = productService.getItem(id);
            product.setAvailable(product.getAvailable() + diff);
            productService.saveItem(product);
            return "Deleted partially";
        }
    }

    public String modifyItemInCart(CartItem cartItem) {
        String id = cartItem.getId();
        int quantity = cartItem.getQuantity();
        boolean contains = cartRepository.getAll().keySet().contains(id);
        if (contains == false) throw new ProductNotFoundException(String.format("Product with id=%s not found", id));
        int quantityInShop = productService.getItem(id).getAvailable();
        if (quantityInShop < quantity) throw new BigQuantityException("Not enough items in shop");

        int quantityInCart = cartRepository.getQuantity(id);
        cartRepository.save(id, quantityInCart + quantity);
        Product product = productService.getItem(id);
        product.setAvailable(product.getAvailable() - quantity);
        productService.saveItem(product);
        return "Modified";
    }

    private CartShow genCartShow(int orderNumber, String id) {
        return CartShow.builder()
            .orderNumber(orderNumber)
            .productName(productService.getItem(id).getTitle())
            .quantities(cartRepository.getQuantity(id))
            .subtotal(cartRepository.getQuantity(id) * Double.valueOf(productService.getItem(id).getPrice()))
            .build();
    }
}

package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.BigQuantityException;
import com.grid.webdevelopment.exception.ProductAddedAlreadyException;
import com.grid.webdevelopment.exception.ProductNotFoundException;
import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartOrder;
import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class CartService {

    private final ProductService productService;
    private final UserService userService;

    protected boolean productExistsInCart(User user, String productId) {
        return user.getCart().keySet().contains(productId);
    }

    public Map<String, Integer> addToCart(String userId, CartItem cartItem) {
        User user = userService.getUserById(userId);

        String productId = cartItem.getId();
        if (productExistsInCart(user, productId)) {
            throw new ProductAddedAlreadyException(String.format("Product with id=%s is added already", productId));
        }

        int quantity = cartItem.getQuantity();
        Product product = productService.getProductById(productId);
        if (product.getAvailable() >= quantity) {
            user.getCart().put(productId, quantity);
            product.setAvailable(product.getAvailable() - quantity);
            userService.saveUser(user);
            productService.saveProduct(product);
            return user.getCart();
        } else {
            throw new BigQuantityException(String.format("Available quantity is %s, but requested %s", product.getAvailable(), quantity));
        }
    }

    public Map<String, Integer> deleteItem(String userId, CartItem cartItem) {
        User user = userService.getUserById(userId);

        String productId = cartItem.getId();

        user.getCart().keySet().stream()
                .filter(id -> id.equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id=%s not found in cart", productId)));

        int quantity = cartItem.getQuantity();
        int quantityInCart = user.getCart().get(productId);
        if (quantity >= quantityInCart) {
            user.getCart().remove(productId);
            Product product = productService.getProductById(productId);
            product.setAvailable(product.getAvailable() + quantityInCart);
            userService.saveUser(user);
            productService.saveProduct(product);
            return user.getCart();
        }
        else {
            int diff = quantityInCart - quantity;
            user.getCart().put(productId, diff);
            Product product = productService.getProductById(productId);
            product.setAvailable(product.getAvailable() + diff);
            userService.saveUser(user);
            productService.saveProduct(product);
            return user.getCart();
        }
    }

    public Map<String, Integer> modifyItemInCart(String userId, CartItem cartItem) {
        User user = userService.getUserById(userId);

        String productId = cartItem.getId();
        int quantity = cartItem.getQuantity();
        if (!productExistsInCart(user, productId)) throw new ProductNotFoundException(String.format("Product with id=%s not found in cart", productId));

        int quantityInShop = productService.getProductById(productId).getAvailable();
        if (quantityInShop < quantity) throw new BigQuantityException("Not enough items in shop");

        int quantityInCart = user.getCart().get(productId);
        user.getCart().put(productId, quantityInCart + quantity);
        Product product = productService.getProductById(productId);
        product.setAvailable(product.getAvailable() - quantity);
        userService.saveUser(user);
        productService.saveProduct(product);
        return user.getCart();
    }

    public List<CartOrder> displayCart(String userId) {
        List<String> ids = userService.getUserById(userId).getCart().keySet().stream().collect(Collectors.toList());

        List<CartOrder> collect = IntStream.range(0, ids.size())
                .mapToObj(i -> genCartOrder(userId, i + 1, ids.get(i)))
                .collect(Collectors.toList());

        return collect;
    }

    private CartOrder genCartOrder(String userId, int orderNumber, String productId) {
        Map<String, Integer> cart = userService.getUserById(userId).getCart();
        return CartOrder.builder()
                .orderNumber(orderNumber)
                .productId(productService.getProductById(productId).getId())
                .productName(productService.getProductById(productId).getTitle())
                .quantities(cart.get(productId))
                .subtotal(cart.get(productId) * Double.valueOf(productService.getProductById(productId).getPrice()))
                .build();
    }

}

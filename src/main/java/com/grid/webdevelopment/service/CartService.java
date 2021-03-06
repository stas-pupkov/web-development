package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.BigQuantityException;
import com.grid.webdevelopment.exception.CartIsEmptyException;
import com.grid.webdevelopment.exception.ProductAddedAlreadyException;
import com.grid.webdevelopment.exception.ProductNotFoundException;
import com.grid.webdevelopment.model.Cart;
import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartView;
import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.repository.CartItemsRepository;
import com.grid.webdevelopment.repository.CartOrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {
    private final ProductService productService;
    private final UserService userService;
    private final CartItemsRepository cartItemsRepository;
    private final CartOrderRepository cartOrderRepository;

    public Map<String, Integer> addToCart(String userId, CartItem cartItem) {
        String productId = cartItem.getId();
        int quantity = cartItem.getQuantity();

        if (productExistsInCart(userId, productId)) {
            throw new ProductAddedAlreadyException(String.format("Product with id=%s is added already", productId));
        }
        removeProductFromShop(productId, quantity);
        addProductToCart(userId, productId, quantity);

        return cartItemsRepository.get(userId);
    }

    public Map<String, Integer> deleteFromCart(String userId, CartItem cartItem) {
        String productId = cartItem.getId();
        int quantity = cartItem.getQuantity();

        if (!productExistsInCart(userId, productId)) {
            throw new ProductNotFoundException(String.format("Product with id=%s not found in cart", productId));
        }
        removeProductFromCart(userId, productId, quantity);
        addProductToShop(productId, quantity);

        return cartItemsRepository.get(userId);
    }

    public Map<String, Integer> modifyItemInCart(String userId, CartItem cartItem) {
        String productId = cartItem.getId();
        int quantity = cartItem.getQuantity();

        if (!productExistsInCart(userId, productId)) {
            throw new ProductNotFoundException(String.format("Product with id=%s not found in cart", productId));
        }
        removeProductFromShop(productId, quantity);
        addProductToCart(userId, productId, quantity);

        return cartItemsRepository.get(userId);
    }

    public List<CartView> displayItemsInCart(String userId) {
        List<String> ids = getUserItems(userId).keySet().stream().collect(Collectors.toList());
        return IntStream.range(0, ids.size())
                .mapToObj(i -> createCartView(userId, i + 1, ids.get(i)))
                .collect(Collectors.toList());
    }

    public Set<Cart> showOrders(String userId) {
        return userService.getUserById(userId).getOrders().stream()
                .map(id -> cartOrderRepository.getOrders().get(id))
                .collect(Collectors.toSet());
    }

    protected void removeProductFromShop(String productId, int quantity) {
        Product product = productService.getProductById(productId);
        int diff = product.getAvailable() - quantity;
        if (diff < 0) {
            throw new BigQuantityException(String.format("Available quantity in shop is %s, but requested %s", product.getAvailable(), quantity));
        }
        product.setAvailable(diff);
        productService.saveProduct(product);
        log.info("{} has been updated on -{} pieces", product, quantity);
    }

    protected void addProductToCart(String userId, String productId, int quantity) {
        Map<String, Integer> items = getUserItems(userId);
        int productsInCart = items.containsKey(productId) ? items.get(productId) : 0;
        cartItemsRepository.save(userId, productId, productsInCart + quantity);
        log.info("ProductId={} has been updated in cartId={} in amount of {} pieces", userId, productId, quantity);
    }

    protected void removeProductFromCart(String userId, String productId, int quantity) {
        Map<String, Integer> items = getUserItems(userId);
        int productsInCart = items.containsKey(productId) ? items.get(productId) : 0;

        int diff = productsInCart - quantity;
        if (diff < 0) {
            throw new BigQuantityException(String.format("Available quantity in cart is %s, but requested %s", productsInCart, quantity));
        }
        if (diff == 0) {
            cartItemsRepository.delete(userId, productId);
            log.info("Product with id={} has been removed from cart", productId);
            return;
        }
        items.put(productId, diff);
        cartItemsRepository.save(userId, productId, diff);
        log.info("ProductId={} has been updated in cartId={} in amount of -{} pieces", productId, userId, quantity);
    }

    protected void addProductToShop(String productId, int quantity) {
        Product product = productService.getProductById(productId);
        int diff = product.getAvailable() + quantity;
        product.setAvailable(diff);
        productService.saveProduct(product);
        log.info("{} has been updated on {} pieces", product, quantity);
    }

    private CartView createCartView(String userId, int orderNumber, String productId) {
        Map<String, Integer> items = getUserItems(userId);
        return CartView.builder()
                .orderNumber(orderNumber)
                .productId(productService.getProductById(productId).getId())
                .productName(productService.getProductById(productId).getTitle())
                .quantities(items.get(productId))
                .subtotal(items.get(productId) * productService.getProductById(productId).getPrice())
                .build();
    }

    protected Map<String, Integer> getUserItems(String userId) {
        return cartItemsRepository.get(userId);
    }

    protected boolean productExistsInCart(String userId, String productId) {
        return getUserItems(userId).containsKey(productId);
    }

    public void returnAllProductsToShop(String userId) {
        List<String> ids = getUserItems(userId).keySet().stream().collect(Collectors.toList());
        ids.forEach(productId -> {
            int quantity = getUserItems(userId).get(productId);
            removeProductFromCart(userId, productId, quantity);
            addProductToShop(productId, quantity);
        });
    }

    public Cart checkout(String userId) {
        Map<String, Integer> items = getUserItems(userId);
        List<String> ids = items.keySet().stream().collect(Collectors.toList());
        if (ids.size() == 0) {
            throw new CartIsEmptyException("Your cart is empty");
        }
        double total = ids.stream()
                .mapToDouble(productId -> items.get(productId) * productService.getProductById(productId).getPrice())
                .sum();
        Cart cart = createCartOrder(total);
        userService.getUserById(userId).getOrders().add(cart.getOrderId());
        cartOrderRepository.saveOrder(cart);
        cartItemsRepository.getItems().put(userId, new HashMap<>());
        return cart;
    }

    protected Cart createCartOrder(Double total) {
        return Cart.builder()
                .orderId(UUID.randomUUID().toString())
                .date(LocalDateTime.now())
                .total(total)
                .status(Cart.OrderStatus.PROGRESS)
                .build();
    }

}

package com.grid.webdevelopment.service;

import com.grid.webdevelopment.exception.BigQuantityException;
import com.grid.webdevelopment.exception.ProductAddedAlreadyException;
import com.grid.webdevelopment.exception.ProductNotFoundException;
import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartView;
import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.repository.CartItemsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {
    private final ProductService productService;
    private final CartItemsRepository cartItemsRepository;

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



//    protected void updateCart(String userId, String productId, int quantity) {
//        boolean buy = quantity >= 0;
//        Map<String, Integer> item = updateItem(userId, productId, Math.abs(quantity), buy);
//        cartItemsRepository.save(userId, item);
//        log.info("ProductId={} has been updated in cartId={} in amount of {} pieces", userId, productId, quantity);
//    }
//
//    protected Map<String, Integer> updateItem(String userId, String productId, int quantity, boolean buy) {
//        Map<String, Integer> items = cartItemsRepository.getItems(userId);
//        int productsInCart = items.containsKey(productId) ? items.get(productId) : 0;
//        int diff = productsInCart + quantity;
//        if (!buy) {
//            diff = productsInCart - quantity;
//            if (diff < 0) {
//                throw new BigQuantityException(String.format("Available quantity in cart is %s, but requested %s", productsInCart, quantity));
//            }
//        }
//        items.put(productId, diff);
//        return items;
//    }
//
//    protected void updateProduct(String productId, int quantity) {
//        Product product = productService.getProductById(productId);
//        int diff = product.getAvailable() - quantity;
//        if (quantity > 0 && diff < 0) {
//            throw new BigQuantityException(String.format("Available quantity in shop is %s, but requested %s", product.getAvailable(), quantity));
//        }
//        product.setAvailable(diff);
//        productService.saveProduct(product);
//        log.info("{} has been updated on {} pieces", product, quantity);

//    }

}

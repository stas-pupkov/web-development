package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.model.Product;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Data
public class DefaultProducts {

    private Map<String, Product> products = new HashMap<>();

    @PostConstruct
    private void init() {
        Product apple = Product.builder().id("1").title("apple").available(10).price(2.5).build();
        Product banana = Product.builder().id("2").title("banana").available(5).price(4.2).build();
        Product cake = Product.builder().id("3").title("cake").available(20).price(1.1).build();
        products = Stream.of(apple, banana, cake).collect(Collectors.toMap(Product::getId, Function.identity()));
    }
}

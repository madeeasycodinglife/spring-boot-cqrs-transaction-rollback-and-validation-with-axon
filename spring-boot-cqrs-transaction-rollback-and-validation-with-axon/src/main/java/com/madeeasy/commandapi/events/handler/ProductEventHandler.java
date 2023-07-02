package com.madeeasy.commandapi.events.handler;

import com.madeeasy.commandapi.entity.Product;
import com.madeeasy.commandapi.events.ProductCreatedEvent;
import com.madeeasy.commandapi.events.ProductDeletedEvent;
import com.madeeasy.commandapi.events.ProductPartiallyUpdatedEvent;
import com.madeeasy.commandapi.events.ProductUpdatedEvent;
import com.madeeasy.commandapi.repository.ProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@ProcessingGroup("product")
public class ProductEventHandler {

    private final ProductRepository productRepository;

    public ProductEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
        Product product = new Product();
        BeanUtils.copyProperties(productCreatedEvent, product);
        System.out.println("product = " + product);
        productRepository.save(product);
        // data will not save as it is throwing exception and we handle this
        // exception and rollback. if you comment the below throw new Exception("error occurred while saving product");
        // and remove throws Exception in method signature then it will save the data to database;
        throw new Exception("error occurred while saving product");
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(ProductDeletedEvent event) {
        productRepository.deleteById(event.getProductId());
    }

    @EventHandler
    public void on(ProductUpdatedEvent event) {
        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(event.getName());
        product.setPrice(event.getPrice());
        product.setQuantity(event.getQuantity());
        productRepository.save(product);
//        throw new Exception("error");
    }

    @EventHandler
    public void on(ProductPartiallyUpdatedEvent event) {
        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Map<String, Object> updates = event.getUpdates();

        if (updates.containsKey("name")) {
            product.setName((String) updates.get("name"));
        }
        if (updates.containsKey("price")) {
            product.setPrice((BigDecimal) updates.get("price"));
        }
        if (updates.containsKey("quantity")) {
            product.setQuantity((Integer) updates.get("quantity"));
        }
        productRepository.save(product);
    }
}






















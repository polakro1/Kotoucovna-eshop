package cz.example.kotoucovnaeshop;

import cz.example.kotoucovnaeshop.model.Cart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.SessionScope;

@SpringBootApplication
public class KotoucovnaEshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(KotoucovnaEshopApplication.class, args);
    }

    @Bean
    @SessionScope
    public Cart cart(){
        return new Cart();
    }
}

package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.LoginRequest;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @GetMapping("/hello")
    public LoginRequest helloPost(@RequestBody LoginRequest loginRequest) {

        return loginRequest;
    }
    // Common spring boot annotations
    /*
    @RestController- Marks a class as a REST controller (Returns JSON or text instead of views)
    @Controller-Marks a class as a Spring MVC controller(For returning views and templates)
    @RequestMapping- Maps HTTP requests to methods/classes
    @GetMapping, @PutMapping, @PushMapping, @DeleteMapping, @PatchMapping- Shorthand for request mappings by HTTP method
    @RequestParam-Extracts query parameters from URL
    @PathVariable- Extracts values from URI path
    @RequestBody- Maps the request body(JSON/XML) to a Java object
    @ResponseBody- Sends return value as response body
     */
    // What is a Bean- A Java object managed by Spring IoC Container
    /*
    @component- Marks a class as a Spring-managed Bean
    @autowired-Injects dependencies automatically
    @Service- Mars a class as a business logic/service bean
    @Repository-Marks a DAO/repository bean(adds exception translation
    @Bean- Defines a bean inside a @Configuration class
     */

    // Autoconfiguration- A Spring Boot feature that automatically configures your application based on the dependencies available on the classpath and sensible defaults
    /*
    @EnableAutoConfiguration (inside the @SpringBootApplication
     */
    // application.yaml vs application.properties
    /*
    Both are used for same purpose(external configuration) Format is different(application.yaml has a hierarchical structure)
     */
    // Why not Spring? Why Spring Boot
    /*
    Advantages of Spring Boot
        *Auto-Configuration- Configures beans automatically
        *Embedded Servers-Comes with embedded servers
        *Starters-Predefined dependencies
        *Faster Development and testing
        *Easier Microservices- Works smoothly with Spring Cloud for distributed systems
     */


}

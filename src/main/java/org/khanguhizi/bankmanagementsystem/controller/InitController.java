package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.LoginRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {

    @GetMapping("/hello")
    public LoginRequest helloPost(@RequestBody LoginRequest loginRequest) {

        return loginRequest;
    }
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

    /*
    EXCEPTION HANDLING IN SPRING BOOT
    What is Exception Handling:
        -Catching and managing runtime or application-level errors
        -Returning meaningful HTTP responses to the client

    Default Exception Handling:
        SpringBoot provides a built-in exception handling mechanism through
        BasicErrorController. When an exception occurs, Spring Boot automatically sends a JSON
        response for REST APIs

    Custom Exception Handling:
    1. @ExceptionHandler-Catches exceptions within its own controller
    2. @ControllerAdvice- Makes this class global for all controllers
            @RestControllerAdvice
            public class GlobalExceptionHandler {

                @ExceptionHandler(ResourceNotFoundException.class)
                public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
                    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
                }

                @ExceptionHandler(MethodArgumentNotValidException.class)
                public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
                    return new ResponseEntity<>("Validation failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
                }

                @ExceptionHandler(Exception.class)
                public ResponseEntity<String> handleGenericException(Exception ex) {
                    return new ResponseEntity<>("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

    3. Custom Exception classes
        public class ResourceNotFoundException extends RuntimeException{
            public ResourceNotFoundException(String message){
                super(message);
            }
        }

     4. @ResponseStatus - Annotate your exception class directly with an HTTP status
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public class ResourceNotFoundException extends RuntimeException {
            public ResourceNotFoundException(String message) {
                super(message);
            }
        }

     5. ResponseEntityExceptionHandler-Advanced use cases, like validation errors, structured responses
             @ControllerAdvice
            public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

                // Handle validation errors
                @Override
                protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex,
                        HttpHeaders headers,
                        HttpStatus status,
                        WebRequest request) {

                    Map<String, Object> body = new HashMap<>();
                    body.put("timestamp", LocalDateTime.now());
                    body.put("status", status.value());

                    // Get validation errors
                    Map<String, String> errors = new HashMap<>();
                    ex.getBindingResult().getFieldErrors().forEach(error ->
                            errors.put(error.getField(), error.getDefaultMessage())
                    );

                    body.put("errors", errors);

                    return new ResponseEntity<>(body, headers, status);
                }
            }


           6. @RestControllerAdvice- combines @ControllerAdvice and @ResponseBody

     */

    /*
                        CORS ERROR IN SPRING BOOT
         CORS - Cross Origin Resource Sharing
              - Occurs when your frontend (on a different origin/domain/port)
                tries to make a request to your Spring Boot backend, and the backend
                doesn't explicitly allow that origin.

         How to solve CORS error
            1. Global CORS Configuration - create a CORS configuration
            2. CORS at Controller level - add @CrossOrigin to specific controllers or methods
            3. CORS with Spring Security (if you're using Spring Security) - If you're using
               Spring Security, it can override global or controller-level CORS settings unless
               explicitly allowed.


               *Add Dependencies (httpclient5)



               USING @CrossOrigin for specific controllers or methods
              	@CrossOrigin(origins = "http://localhost:9000")
                @GetMapping("/greeting")
                public Greeting greeting(@RequestParam(required = false, defaultValue = "World") String name) {
                    System.out.println("==== get greeting ====");
                    return new Greeting(counter.incrementAndGet(), String.format(template, name));

                Explanation
                This @CrossOrigin annotation enables cross-origin resource sharing only for this specific method.
                In this example, we allow only http://localhost:9000 to send cross-origin requests.
                You can also add the @CrossOrigin annotation at the controller class level as well, to enable
                CORS on all handler methods of this class.




                GLOBAL CORS CONFIGURATION
                 Create a class that returns WebMvcConfigurer
                    @Bean
                    public WebMvcConfigurer corsConfigurer() {
                        return new WebMvcConfigurer() {
                            @Override
                            public void addCorsMappings(CorsRegistry registry) {
                                registry.addMapping("/greeting-javaconfig").allowedOrigins("http://localhost:9000");
                            }
                        };
                    }

                    Explanation


     */



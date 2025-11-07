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

    /*
                    SECURITY IN SPRINGBOOT
        What is Spring Security?
            Spring Security is a framework that provides:
                Authentication: Who are you?
                Authorization: Are you allowed to do this?
                Protection against common vulnerabilities (e.g., CSRF, session fixation, clickjacking)
                Integration with OAuth2, JWT, LDAP, etc.

        Add Spring Security dependencies

        @Configuration
        @EnableWebSecurity
        public class SecurityConfig extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                    .authorizeRequests()
                    .antMatchers("/public").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic(); // Basic authentication
            }

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth
                    .inMemoryAuthentication()
                    .withUser("user")
                    .password(passwordEncoder().encode("password"))
                    .roles("USER");
            }

            @Bean
            public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
            }
        }


                JWT (JSON Web Token) Authentication
        JWT is commonly used in stateless APIs:
            Clients authenticate and receive a token
            Token is sent with each request in the Authorization header
            The server verifies the token without keeping session state
        Example JWT flow involves:
            AuthenticationFilter: to login and generate JWT
            AuthorizationFilter: to validate JWT on each request


                Method-Level Security
        Use annotations like:
            @PreAuthorize("hasRole('ADMIN')")
            @Secured("ROLE_USER")
            @RolesAllowed("ADMIN")


                Security Best Practices
        Use BCrypt for password encoding
        Enable HTTPS
        Protect against CSRF (enabled by default for web apps)
        Limit CORS as needed
        Use stateless sessions for REST APIs
        Avoid exposing sensitive data in errors

                Annotations
        @EnableWebSecurity	Enables Spring Security config
        @EnableGlobalMethodSecurity	Enables method-level security
        @WithMockUser	Used in tests to simulate logged-in user


        UserDetailsService: this is the main service which retrieves security
                            information about users. As we defined our custom bean,
                            we don’t see anymore in the log the default password provided
                            by Spring, as we are now providing it.
        UserDetails class: this is the interface which Spring uses to process user’s
                           security related information, like the username or its password
                           (take a look into the class for more information). We are using
                           the standard User class (also provided by Spring) as its
                           implementation.
     */

    /*
                AUTHENTICATION METHODS
            1. Basic Authentication
            2. Form-Based Authentication
            3. JWT (JSON Web Token) Authentication
            4. OAuth2 / OpenID Connect (OIDC)
            5. LDAP Authentication
            6. Database Authentication (CustomUserDetailsService)
            7. X.509 Certificate Authentication
            8. SAML 2.0 Authentication
            9. API Key Authentication
           10. Session-Based Authentication
           11. Token-Based Authentication
           12. Multi-Factor Authentication (MFA)

                    2. Form-Based Authentication
            User logs in through an HTML form instead of the browser's Basic Auth Popup
                Credentials are submitted through the form
                Spring Security validates them
                On Success, it creates a HTTP session and stores authentication info

                    3. JWT (JSON Web Token) Authentication
            The server issues a signed token after login, the client includes it in subsequent requests
                User logs in
                    Sever validates credentials
                    Server issues a JWT
                 Client stores JWT (Usually in localStorage or Cookies)
                 Client sends JWT in the Authorization header
                 Server validates JWT signature for each request

                 4. OAuth2 / OpenID Connect (OIDC)
             Allows login via third-party providers eg Google, GitHub etc
                User is redirected to the provider (eg Google) for authentication
                Provider sends an access token (and optionally an ID Token)
                Spring Security uses that token to authenticate the user

                5. LDAP Authentication (Lightweight Directory Access Protocol)
            LDAP Authentication is used to validate users stored in a directory service like Active Directory in organizations
                The app connects to LDAP server
                It searches for the name in the directory
                It verifies the password with LDAP
                If valid, the user gains access

                6. Database Authentication (CustomUserDetailsService)
            This is where you manage users in your own database
            You create a table for users and implement your own logic to validate them
                You create a user entity and repository
                You implement UserDetailsService to fetch user data from the DB
                Spring Security uses this service for authentication

                7. X.509 Certificate Authentication


                8. SAML 2.0 Authentication


                9. API Key Authentication
             An API Key is a static, pre-generated secret string (like a password for your application)
             It is created manually or stored in a database, then shared with a trusted client (another system or developer)
                The client sends the API Key in each request header or query parameter
                The server checks if the key is valid (matches one in the database or configuration)
                If valid - access is granted
                If invalid - request is rejected

               10. Session-Based Authentication
             After a successful login, Spring stores authentication in HTTP session

               11. Token-Based Authentication
             A token is a temporary digital credential issued after a successful login
             It represents a specific authenticated user and usually contains user info (claims)
                User logs in with username/password
                Server validates credentials and issues a token
                The token contains encoded user info (eg username, roles)
                Client sends the token with each request
                The server validates the token's signature and expiry before processing

               12. Multi-Factor Authentication (MFA)
     */

        /*
                            SWAGGER
             Swagger is a set of tools and specifications used to design, build, document, and consume RESTful APIs.
             It’s now officially part of the OpenAPI Specification (OAS), which is the industry standard for describing
             APIs in a machine-readable way.

                 In simple terms:
             Swagger provides a standard format (usually a YAML or JSON file) that describes your API —
             what endpoints it has, what parameters they take, what responses they return, etc.
             For example, a Swagger (OpenAPI) document might describe an endpoint like this:

                        paths:
                          /users:
                            get:
                              summary: Get all users
                              responses:
                                '200':
                                  description: A list of users

                Key Components of Swagger:
        Swagger/OpenAPI Specification
            A language-agnostic definition format for REST APIs.
            Example file: swagger.yaml or openapi.json.
        Swagger UI
            A web-based interface that automatically generates interactive API documentation from your Swagger file.
            You can test endpoints directly in the browser.
        Swagger Editor
            An online or local editor for writing and validating Swagger/OpenAPI files.
        Swagger Codegen / OpenAPI Generator
            Tools that can automatically generate client SDKs, server stubs, or API documentation from your Swagger spec.

                Why Swagger is Useful
        Documentation: Automatically generates interactive docs for developers.
        Testing: Try out API endpoints directly from the browser.
        Consistency: Ensures all teams work from the same API contract.
        Automation: Generates server and client code automatically.

               Swagger vs OpenAPI
        Swagger was the original specification created by SmartBear.
        It was later donated to the OpenAPI Initiative (under the Linux Foundation).
        The current standard is called OpenAPI Specification (OAS) — Swagger tools still support it.

                In short:
        Swagger = Tools + Format for defining, testing, and documenting REST APIs (now based on the OpenAPI standard).
         */

//U5CC3141EN8MNDEM34X67DHV


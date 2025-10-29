package org.khanguhizi.bankmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class AppConfig {
    @Bean
    public OpenAPI bankManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Management System")
                        .description("API documentation for the Bank Management System")
                        .version("1.0.0")
                );
    }
}

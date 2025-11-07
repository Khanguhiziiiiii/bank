package org.khanguhizi.bankmanagementsystem.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.cert.X509Certificate;

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

    @Bean
    public RestTemplate restTemplate() throws Exception {
        // Create a trust manager that trusts all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        // Initialize an SSL context with that trust manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // Create a socket factory that uses our SSL context and disables hostname verification
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(
                sslContext,
                (hostname, session) -> true // trust all hostnames
        );

        // Build a custom HttpClient using HttpClient 5.x API
        org.apache.hc.client5.http.classic.HttpClient httpClient =
                HttpClients.custom()
                        .setConnectionManager(
                                org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder.create()
                                        .setSSLSocketFactory(csf)
                                        .build()
                        )
                        .build();

        // Use that client for Spring’s RestTemplate
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // Return a RestTemplate that ignores all SSL certs
        return new RestTemplate(requestFactory);
    }

}

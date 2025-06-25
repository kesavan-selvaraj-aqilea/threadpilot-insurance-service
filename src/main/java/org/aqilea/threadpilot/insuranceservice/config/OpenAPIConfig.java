package org.aqilea.threadpilot.insuranceservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${app.environment:Development}")
    private String environment;

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort);
        server.setDescription(environment);

        Contact myContact = new Contact();
        myContact.setName("Kesavan Selvaraj");
        myContact.setEmail("kesavan.selvaraj@aqilea.com");

        Info information = new Info()
                .title("Insurance Service API")
                .version("1.0")
                .description("This API exposes endpoints realted to Insurance.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}

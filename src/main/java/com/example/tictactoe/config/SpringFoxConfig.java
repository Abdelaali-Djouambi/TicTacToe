package com.example.tictactoe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(myApiInfo());
    }

    private ApiInfo myApiInfo() {
        return new ApiInfoBuilder().title("TicTacToe Kata")
                .description("This kata is a little application developped using SpringBoot 2.5.5. " +
                        "It has multiple endpoints to play a TicTacToe game")
                .contact(new Contact("Abdelaali Djouambi", "", "abdelaali.djouambi@digitalstrok.be"))
                .build();

    }
}

package com.booksforeveryone.bookstore.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Bean
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER.registerModule(new VavrModule());
    }

}

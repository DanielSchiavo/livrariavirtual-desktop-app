package br.com.danielschiavo.livrariavirtual.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.Getter;

public class UsuarioServiceClientFactory {

    @Getter
    private static UsuarioServiceClient client;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        client = Feign.builder()
                .decoder(new JacksonDecoder(objectMapper))
                .encoder(new JacksonEncoder(objectMapper))
                .errorDecoder(new CustomErrorDecoder())
                .target(UsuarioServiceClient.class, "http://localhost:8080/livrariavirtual/usuario");
    }

}

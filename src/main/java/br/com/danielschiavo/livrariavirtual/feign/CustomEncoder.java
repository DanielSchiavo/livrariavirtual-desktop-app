package br.com.danielschiavo.livrariavirtual.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;

public class CustomEncoder implements Encoder {

    private final ObjectMapper objectMapper;

    public CustomEncoder() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        try {
            String json = objectMapper.writeValueAsString(object);
            template.body(json);
        } catch (JsonProcessingException e) {
            throw new EncodeException("Erro ao serializar o objeto para JSON", e);
        }
    }
}

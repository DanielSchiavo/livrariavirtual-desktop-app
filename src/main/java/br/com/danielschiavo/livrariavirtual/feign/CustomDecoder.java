package br.com.danielschiavo.livrariavirtual.feign;

import br.com.danielschiavo.livrariavirtual.modelo.UsuarioNaoEncontradoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class CustomDecoder implements Decoder {



    // Decoder
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException {
        if (response.body() == null) {
            return null;
        }

        try {
            // Obtém a classe a partir do Type
            Class<?> clazz = Class.forName(type.getTypeName());

            // Usa o ObjectMapper para desserializar o corpo da resposta
            return new ObjectMapper().readValue(response.body().asReader(), clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
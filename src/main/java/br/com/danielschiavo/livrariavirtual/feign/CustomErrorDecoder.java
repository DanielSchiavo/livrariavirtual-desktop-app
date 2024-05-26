package br.com.danielschiavo.livrariavirtual.feign;

import br.com.danielschiavo.livrariavirtual.modelo.UsuarioNaoEncontradoException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                try {
                    System.out.println(" ERRO 400 ");
                    var json = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
                    return new UsuarioNaoEncontradoException(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case 404:
                System.out.println(" ERRO 404 ");
                return new RuntimeException();
            default:
                System.out.println(" OUTRO ERRO ");
                try {
                    return new Exception("Generic error, ResponseBody = " + IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8) + " / " + response.toString() + ", status" + response.status() + ", URL" + response.request().url());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}

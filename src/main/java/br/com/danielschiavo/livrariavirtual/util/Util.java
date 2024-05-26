package br.com.danielschiavo.livrariavirtual.util;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.function.Consumer;

public class Util {

    public static <T, U> T carregarView(String caminhoAbsoluto, Class<T> clazz, Consumer<U> controllerAction) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Util.class.getResource(caminhoAbsoluto));
            Object object = fxmlLoader.load();

            U controller = fxmlLoader.getController();
            controllerAction.accept(controller);

            if (object != null && clazz.isAssignableFrom(object.getClass())) {
                return clazz.cast(object);
            } else {
                throw new IllegalArgumentException("O object não é atribuível ao tipo fornecido.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

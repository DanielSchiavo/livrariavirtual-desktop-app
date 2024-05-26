package br.com.danielschiavo.livrariavirtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class Main extends Application {

    @Getter
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/MainView.fxml"));
        VBox vBox = fxmlLoader.load();

        mainScene = new Scene(vBox);
        stage.setScene(mainScene);
        stage.setTitle("Livraria Virtual");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
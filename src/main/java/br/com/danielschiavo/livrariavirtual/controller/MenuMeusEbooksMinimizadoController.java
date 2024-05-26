package br.com.danielschiavo.livrariavirtual.controller;

import br.com.danielschiavo.livrariavirtual.Main;
import br.com.danielschiavo.livrariavirtual.util.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuMeusEbooksMinimizadoController implements Initializable {

    @FXML
    private Pane paneMenuMinimizado;

    @FXML
    private Button btnMeusEbooks;

    private AnchorPane anchorPaneMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    public void onBtnMeusEbooksAction() {
        paneMenuMinimizado.setVisible(false);

        Scene mainScene = Main.getMainScene();
        SplitPane splitPanePrincipal = (SplitPane) mainScene.lookup("#splitPanePrincipal");

        if (anchorPaneMenu == null)
            anchorPaneMenu = Util.carregarView("/MenuMeusEbooks.fxml", AnchorPane.class, x -> {});

        anchorPaneMenu.setVisible(true);
        anchorPaneMenu.setPrefWidth(300);


        splitPanePrincipal.getItems().set(0, anchorPaneMenu);

        System.out.println("onBtnMenuMinimizadoMeusEbooksAction");
    }
}

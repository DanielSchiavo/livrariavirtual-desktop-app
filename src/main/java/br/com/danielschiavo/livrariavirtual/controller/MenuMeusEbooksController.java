package br.com.danielschiavo.livrariavirtual.controller;

import br.com.danielschiavo.livrariavirtual.Main;
import br.com.danielschiavo.livrariavirtual.dto.AdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.dto.RespostaAdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.feign.UsuarioServiceClientFactory;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class MenuMeusEbooksController implements Initializable {

    @FXML
    private AnchorPane anchorPaneListaEbooks;

    @FXML
    private ListView<Ebook> listViewMeusEbooks;

    @FXML
    private Button btnAdicionarEbook;

    @FXML
    private Button btnMinimizarMenu;

    private Pane paneMenuMinimizado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Ebook> ebooks = MainController.usuario.getEbooks();
        ObservableList<Ebook> observableEbookItems = FXCollections.observableArrayList(ebooks);
        listViewMeusEbooks.setItems(observableEbookItems);

        listViewMeusEbooks.setCellFactory(new Callback<ListView<Ebook>, ListCell<Ebook>>() {
            @Override
            public ListCell<Ebook> call(ListView<Ebook> listView) {
                return new ListCell<Ebook>() {
                    @Override
                    protected void updateItem(Ebook ebook, boolean empty) {
                        super.updateItem(ebook, empty);
                        if (ebook == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Exibe o nome do eBook e a miniatura da capa
                            setText(ebook.getNome());
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ebook.getImagemCapa());
                            ImageView imageView = new ImageView(new Image(byteArrayInputStream));
                            imageView.setFitWidth(80); // Ajusta a largura da imagem (opcional)
                            imageView.setPreserveRatio(true);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        listViewMeusEbooks.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Ebook ebookSelecionado = listViewMeusEbooks.getSelectionModel().getSelectedItem();

                System.out.println("Item selecionado: " + ebookSelecionado.getNome());
            }
        });
    }

    @FXML
    public void onBtnMinimizarMenuAction() {
        anchorPaneListaEbooks.setVisible(false);
        anchorPaneListaEbooks.setPrefWidth(0);

        Scene mainScene = Main.getMainScene();
        SplitPane splitPanePrincipal = (SplitPane) mainScene.lookup("#splitPanePrincipal");
        splitPanePrincipal.setDividerPositions(0.03);

        if (paneMenuMinimizado == null)
            paneMenuMinimizado = Util.carregarView("/MenuMeusEbooksMinimizadoView.fxml", Pane.class, x -> {});

        splitPanePrincipal.getItems().set(0, paneMenuMinimizado);

        System.out.println("onBtnMinimizarAction");
    }

    @FXML
    public void onBtnAdicionarEbookAction() {
        FileChooser fileChooser = new FileChooser();

        // Define as extensões específicas
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Permite a seleção de múltiplos arquivos
        fileChooser.setTitle("Selecione os e-books");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null) {
            Set<AdicionarEbookDTO> listaAdicionarEbookDTO = new HashSet<>();
            for (File file : selectedFiles) {
                listaAdicionarEbookDTO.add(AdicionarEbookDTO.builder()
                        .nomeArquivo(file.getName())
                        .conteudo(pegarBytes(file))
                        .senha(null).build());
                System.out.println("Selected file: " + file.getAbsolutePath());
            }

            try {
                RespostaAdicionarEbookDTO respostaAdicionarEbooksDTO = UsuarioServiceClientFactory.getClient().adicionarEbooks(listaAdicionarEbookDTO, MainController.usuario.getId().toString());
                System.out.println("Ebooks adicionados com sucesso!");
                System.out.println("Tamanho da lista sucesso: " + respostaAdicionarEbooksDTO.ebooksSucesso().size());
                System.out.println("Tamanho da lista falha: " + respostaAdicionarEbooksDTO.ebooksFalha().size());

                listViewMeusEbooks.getItems().addAll(respostaAdicionarEbooksDTO.ebooksSucesso());

                if (!respostaAdicionarEbooksDTO.ebooksFalha().isEmpty()) {
                    ObservableList<Ebook> observableEbookItems = FXCollections.observableArrayList(respostaAdicionarEbooksDTO.ebooksFalha());

                    AnchorPane anchorPaneVerificaoSenhaEbook = Util.carregarView("/InformarSenhaEbookView.fxml", AnchorPane.class, (InformarSenhaEbookController controller) -> {
                        controller.adicionarItemsNaTableView(observableEbookItems);
                        controller.adicionarEbooksAdicionadosComSucesso(respostaAdicionarEbooksDTO.ebooksSucesso());
                        controller.setMenuMeusEbooksController(this);
                    });
                    Stage sobreStage = new Stage();
                    sobreStage.setTitle("Verificação de senha ebook");
                    sobreStage.setScene(new Scene(anchorPaneVerificaoSenhaEbook));
                    sobreStage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Falha ao tentar fazer requisição ao backend para adicionar os ebooks.");
            }

        } else {
            System.out.println("Seleção de arquivos cancelada");
        }
    }

    private byte[] pegarBytes(File file) {
        try {
            return new FileInputStream(file).readAllBytes();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void adicionarEbooksNaListView(List<Ebook> ebooks) {
        listViewMeusEbooks.getItems().addAll(ebooks);
    }
}

package br.com.danielschiavo.livrariavirtual.controller;

import br.com.danielschiavo.livrariavirtual.Main;
import br.com.danielschiavo.livrariavirtual.dto.CadastrarUsuarioDTO;
import br.com.danielschiavo.livrariavirtual.feign.UsuarioServiceClientFactory;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import br.com.danielschiavo.livrariavirtual.modelo.UsuarioNaoEncontradoException;
import br.com.danielschiavo.livrariavirtual.util.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private MenuItem menuItemSobre;

    @FXML
    private SplitPane splitPanePrincipal;

    private static List<Ebook> bancoDeDados = new ArrayList<>();

    public static Usuario usuario;

    @FXML
    public void onMenuItemSobreAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/SobreView.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            Stage sobreStage = new Stage();
            sobreStage.setTitle("Sobre");
            sobreStage.setScene(new Scene(anchorPane));
            sobreStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        Ebook ebook1 = Ebook.builder().nome("Clean Architecture yuigyuguyyuguyguygyugyugyuguygugy").nomeArquivo("Book - Clean Architecture - Robert Cecil Martin.pdf").build();
        Ebook ebook2 = Ebook.builder().nome("Design Patterns").nomeArquivo("Design Patterns - Elements of Reusable Object-Oriented Software.pdf").build();
        Ebook ebook3 = Ebook.builder().nome("A lei da atração").nomeArquivo("Law of Attraction.pdf").build();
        bancoDeDados.addAll(List.of(ebook1, ebook2, ebook3));
    };

    String[] ebook = {"Clean Architecture", "Design Patterns", "A lei da atração"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            usuario = UsuarioServiceClientFactory.getClient().pegarUsuario();
            System.out.println("Recuperado usuario do backend com sucesso");
        } catch (UsuarioNaoEncontradoException e) {
            System.out.println("Usuario ainda não foi cadastrado");
            CadastrarUsuarioDTO cadastrarUsuarioDTO = new CadastrarUsuarioDTO("", "");
            usuario = UsuarioServiceClientFactory.getClient().cadastrarUsuario(cadastrarUsuarioDTO);
            System.out.println("Usuario cadastrado com sucesso");
        }

        AnchorPane anchorPane = Util.carregarView("/MenuMeusEbooks.fxml", AnchorPane.class, x -> {});
        splitPanePrincipal.getItems().addFirst(anchorPane);

//        bancoDeDados.forEach(eb -> {
//            try {
//                PDDocument document = Loader.loadPDF(new File("C:\\Users\\danie\\Documents\\Intellij Workspace\\Meus projetos\\LivrariaVirtual\\ebooks\\" + eb.getNomeArquivo()));
//                PDFRenderer pdfRenderer = new PDFRenderer(document);
//
//                int pageNumber = 0;
//                BufferedImage image = pdfRenderer.renderImage(pageNumber);
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(image, "jpg", baos);
//                byte[] imageBytes = baos.toByteArray();
//
//                eb.setImagemCapa(imageBytes);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        ebooks.getItems().addAll(bancoDeDados);
//
//        ebooks.setCellFactory(new Callback<ListView<Ebook>, ListCell<Ebook>>() {
//            @Override
//            public ListCell<Ebook> call(ListView<Ebook> listView) {
//                return new ListCell<Ebook>() {
//                    @Override
//                    protected void updateItem(Ebook ebook, boolean empty) {
//                        super.updateItem(ebook, empty);
//                        if (ebook == null || empty) {
//                            setText(null);
//                            setGraphic(null);
//                        } else {
//                            // Exibe o nome do eBook e a miniatura da capa
//                            setText(ebook.getNome());
//                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ebook.getImagemCapa());
//                            ImageView imageView = new ImageView(new Image(byteArrayInputStream));
//                            imageView.setFitWidth(80); // Ajusta a largura da imagem (opcional)
//                            imageView.setPreserveRatio(true);
//                            setGraphic(imageView);
//                        }
//                    }
//                };
//            }
//        });

//        ebooks.setOnMouseClicked(event -> {
//            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
//                String ebookSelecionado = ebooks.getSelectionModel().getSelectedItem();
//
//                System.out.println("Item selecionado: " + ebookSelecionado);
//            }
//        });

    }
}
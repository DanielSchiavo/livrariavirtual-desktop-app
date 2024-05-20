package br.com.danielschiavo.livrariavirtualdesktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import modelo.Ebook;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MeusEbooksController implements Initializable {

    @FXML
    private ListView<Ebook> ebooks;

    private static List<Ebook> bancoDeDados = new ArrayList<>();

    static {
        Ebook ebook1 = Ebook.builder().nome("Clean Architecture").nomeArquivo("Book - Clean Architecture - Robert Cecil Martin.pdf").build();
        Ebook ebook2 = Ebook.builder().nome("Design Patterns").nomeArquivo("Design Patterns - Elements of Reusable Object-Oriented Software.pdf").build();
        Ebook ebook3 = Ebook.builder().nome("A lei da atração").nomeArquivo("Law of Attraction.pdf").build();
        bancoDeDados.addAll(List.of(ebook1, ebook2, ebook3));
    };

    String[] ebook = {"Clean Architecture", "Design Patterns", "A lei da atração"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        bancoDeDados.forEach(eb -> {
            try {
                PDDocument document = Loader.loadPDF(new File("C:\\Users\\danie\\Documents\\Intellij Workspace\\Meus projetos\\LivrariaVirtual\\ebooks\\" + eb.getNomeArquivo()));
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                int pageNumber = 0;
                BufferedImage image = pdfRenderer.renderImage(pageNumber);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();

                eb.setImagemCapa(imageBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ebooks.getItems().addAll(bancoDeDados);

        ebooks.setCellFactory(new Callback<ListView<Ebook>, ListCell<Ebook>>() {
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

//        ebooks.setOnMouseClicked(event -> {
//            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
//                String ebookSelecionado = ebooks.getSelectionModel().getSelectedItem();
//
//                System.out.println("Item selecionado: " + ebookSelecionado);
//            }
//        });

    }
}
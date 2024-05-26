package br.com.danielschiavo.livrariavirtual.controller;

import br.com.danielschiavo.livrariavirtual.dto.AdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.dto.RespostaAdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.feign.UsuarioServiceClientFactory;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Setter;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class InformarSenhaEbookController implements Initializable {

    @FXML
    private AnchorPane anchorPaneVerificaoSenhaEbook;

    @FXML
    private TableView<Ebook> tableViewInformarSenhaEbooks = new TableView<>();

    @FXML
    private TableColumn<Ebook, String> tableColumnNomeEbook = new TableColumn<>();

    @FXML
    private TableColumn<Ebook, PasswordField> tableColumnSenha = new TableColumn<>();

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnDescartar;

    @Setter
    private MenuMeusEbooksController menuMeusEbooksController;

    private List<Ebook> ebooksAdicionadosComSucesso = new ArrayList<>();

    public void adicionarEbooksAdicionadosComSucesso(List<Ebook> ebooks) {
        ebooksAdicionadosComSucesso.addAll(ebooks);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Inicializando InformarSenhaEbookController");
        tableColumnNomeEbook.setCellValueFactory(new PropertyValueFactory<>("nomeArquivo"));

        tableColumnSenha.setCellFactory(new Callback<TableColumn<Ebook, PasswordField>, TableCell<Ebook, PasswordField>>() {
            @Override
            public TableCell<Ebook, PasswordField> call(TableColumn<Ebook, PasswordField> param) {
                return new TableCell<Ebook, PasswordField>() {
                    private final PasswordField passwordField = new PasswordField();
                    {
                        passwordField.setPromptText("Digite a senha do ebook aqui");
                        passwordField.setOnKeyReleased(event -> {
                            Ebook ebook = getTableView().getItems().get(getIndex());
                            ebook.setSenhaDoEbook(passwordField.getText());
                        });
                    }

                    @Override
                    protected void updateItem(PasswordField item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(passwordField);
                        }
                    }
                };
            }
        });

        System.out.println("Finalizando InformarSenhaEbookController");
    }

    @FXML
    public void onBtnConfirmarAction() {
        boolean prosseguir = true;
        for (Ebook ebook : tableViewInformarSenhaEbooks.getItems()) {
            // Verificar se a senha foi preenchida
            if (ebook.getSenhaDoEbook() == null && ebook.getSenhaDoEbook().isEmpty()) {
                // Faça o procedimento necessário com o ebook
                System.out.println("Ebook: " + ebook.getNomeArquivo() + ", Senha: " + ebook.getSenhaDoEbook());
                prosseguir = false;
            }
        }

        if (prosseguir) {
            RespostaAdicionarEbookDTO respostaAdicionarEbookDTO = adicionarEbook(tableViewInformarSenhaEbooks.getItems());
            if (respostaAdicionarEbookDTO.ebooksFalha().isEmpty()) {
                fecharJanela();
                menuMeusEbooksController.adicionarEbooksNaListView(respostaAdicionarEbookDTO.ebooksSucesso());
                System.out.println("Chama para atualizar meus ebooks");
            }
            else {
                System.out.println("Aqui vai uma pop up avisando que a senha não funcionou");
                tableViewInformarSenhaEbooks.getItems().clear();
                tableViewInformarSenhaEbooks.getItems().addAll(respostaAdicionarEbookDTO.ebooksFalha());
                System.out.println("Chama para atualizar meus ebooks " + respostaAdicionarEbookDTO.ebooksFalha().get(0).getNomeArquivo());
            }
        }
    }

    @FXML
    public void onBtnDescartarAction() {
        fecharJanela();
    }

    public RespostaAdicionarEbookDTO adicionarEbook(List<Ebook> ebooksComSenhasInformadas) {
        try {
            Set<AdicionarEbookDTO> setAdicionarEbookDTO = ebooksComSenhasInformadas.stream().map(ebook -> {
                return AdicionarEbookDTO.builder().nomeArquivo(ebook.getNomeArquivo())
                        .conteudo(ebook.getConteudo())
                        .senha(ebook.getSenhaDoEbook()).build();
            }).collect(Collectors.toSet());

            return UsuarioServiceClientFactory.getClient().adicionarEbooks(setAdicionarEbookDTO, MainController.usuario.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Falha ao tentar fazer requisição ao backend para adicionar os ebooks.");
            throw new RuntimeException("Backend fora do ar");
        }
    }

    public void fecharJanela() {
        Stage stage = (Stage) anchorPaneVerificaoSenhaEbook.getScene().getWindow();
        stage.close();
    }

    public void adicionarItemsNaTableView(ObservableList<Ebook> observableEbookItems) {
        tableViewInformarSenhaEbooks.getItems().addAll(observableEbookItems);
    }
}

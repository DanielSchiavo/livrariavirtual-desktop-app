package br.com.danielschiavo.livrariavirtual.feign;

import br.com.danielschiavo.livrariavirtual.dto.AdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.dto.CadastrarUsuarioDTO;
import br.com.danielschiavo.livrariavirtual.dto.RespostaAdicionarEbookDTO;
import br.com.danielschiavo.livrariavirtual.modelo.Ebook;
import br.com.danielschiavo.livrariavirtual.modelo.Usuario;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;
import java.util.Set;

public interface UsuarioServiceClient {

    @RequestLine("GET /verificar")
    boolean verificarSeUsuarioExiste();

    @RequestLine("POST ")
    @Headers("Content-Type: application/json")
    Usuario cadastrarUsuario(CadastrarUsuarioDTO cadastrarUsuarioDTO);

    @RequestLine("GET ")
    @Headers("Content-Type: application/json")
    Usuario pegarUsuario();

    @RequestLine("POST /{usuarioId}/ebook")
    @Headers("Content-Type: application/json")
    RespostaAdicionarEbookDTO adicionarEbooks(Set<AdicionarEbookDTO> ebooks, @Param("usuarioId") String usuarioId);

    @RequestLine("GET /{usuarioId}/ebook/{ebooksId}")
    @Headers("Content-Type: application/json")
    List<Ebook> pegarEbooksPorId(@Param("ebooksId") Set<String> ebooksId, @Param("usuarioId") String usuarioId);
}

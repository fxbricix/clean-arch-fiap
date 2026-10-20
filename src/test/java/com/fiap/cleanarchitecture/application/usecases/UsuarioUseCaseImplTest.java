package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.exceptions.RegraDeNegocioException;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UsuarioUseCaseImplTest {

    private IUsuarioGateway usuarioGateway;
    private ITipoUsuarioGateway tipoUsuarioGateway;
    private IRestauranteGateway restauranteGateway;
    private UsuarioUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioGateway.class);
        tipoUsuarioGateway = mock(ITipoUsuarioGateway.class);
        restauranteGateway = mock(IRestauranteGateway.class);
        useCase = new UsuarioUseCaseImpl(usuarioGateway, tipoUsuarioGateway, restauranteGateway);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        TipoUsuario tipo = new TipoUsuario(1L, "CLIENTE");
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));
        when(usuarioGateway.existePorEmail("fabricio@fiap.com")).thenReturn(false);
        when(usuarioGateway.salvar(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = useCase.criar("Fabricio", "fabricio@fiap.com", 1L);

        assertEquals("Fabricio", resultado.getNome());
        assertEquals(new Email("fabricio@fiap.com"), resultado.getEmail());
        assertSame(tipo, resultado.getTipoUsuario());
        verify(usuarioGateway).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve criar usuário com e-mail já existente")
    void naoDeveCriarUsuarioComEmailExistente() {
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(new TipoUsuario(1L, "CLIENTE")));
        when(usuarioGateway.existePorEmail("fabricio@fiap.com")).thenReturn(true);

        assertThrows(ConflitoException.class,
                () -> useCase.criar("Fabricio", "fabricio@fiap.com", 1L));

        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve criar usuário sem ID de tipo")
    void naoDeveCriarUsuarioSemIdTipo() {
        DominioException exception = assertThrows(DominioException.class,
                () -> useCase.criar("Fabricio", "fabricio@fiap.com", null));

        assertEquals("O tipo de usuário é obrigatório", exception.getMessage());
        verifyNoInteractions(usuarioGateway, tipoUsuarioGateway);
    }

    @Test
    @DisplayName("Não deve criar usuário quando o tipo não existe")
    void naoDeveCriarUsuarioComTipoInexistente() {
        when(tipoUsuarioGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> useCase.criar("Fabricio", "fabricio@fiap.com", 1L));

        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() {
        Usuario usuario = criarUsuario(1L, new TipoUsuario(2L, "CLIENTE"));
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = useCase.buscarPorId(1L);

        assertSame(usuario, resultado);
    }

    @Test
    @DisplayName("Não deve buscar usuário inexistente")
    void naoDeveBuscarUsuarioInexistente() {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.buscarPorId(1L));
    }

    @Test
    @DisplayName("Deve listar usuários")
    void deveListarUsuarios() {
        List<Usuario> usuarios = List.of(
                criarUsuario(1L, new TipoUsuario(2L, "CLIENTE")),
                criarUsuario(2L, new TipoUsuario(3L, "ADMIN")));
        when(usuarioGateway.listar()).thenReturn(usuarios);

        List<Usuario> resultado = useCase.listar();

        assertSame(usuarios, resultado);
    }

    @Test
    @DisplayName("Deve associar tipo ao usuário")
    void deveAssociarTipoAoUsuario() {
        TipoUsuario novoTipo = new TipoUsuario(3L, "DONO_RESTAURANTE");
        Usuario usuario = criarUsuario(1L, new TipoUsuario(2L, "CLIENTE"));
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.of(novoTipo));
        when(usuarioGateway.salvar(usuario)).thenReturn(usuario);

        Usuario resultado = useCase.associarTipo(1L, 3L);

        assertSame(usuario, resultado);
        assertSame(novoTipo, usuario.getTipoUsuario());
        verify(usuarioGateway).salvar(usuario);
    }

    @Test
    @DisplayName("Não deve associar tipo a usuário inexistente")
    void naoDeveAssociarTipoAUsuarioInexistente() {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.associarTipo(1L, 3L));

        verifyNoInteractions(tipoUsuarioGateway);
        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve associar tipo inexistente ao usuário")
    void naoDeveAssociarTipoInexistente() {
        when(usuarioGateway.buscarPorId(1L))
                .thenReturn(Optional.of(criarUsuario(1L, new TipoUsuario(2L, "CLIENTE"))));
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.associarTipo(1L, 3L));

        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve associar tipo quando o ID do tipo for nulo")
    void naoDeveAssociarTipoComIdNulo() {
        when(usuarioGateway.buscarPorId(1L))
                .thenReturn(Optional.of(criarUsuario(1L, new TipoUsuario(2L, "CLIENTE"))));

        DominioException exception = assertThrows(DominioException.class,
                () -> useCase.associarTipo(1L, null));

        assertEquals("O tipo de usuário é obrigatório", exception.getMessage());
        verifyNoInteractions(tipoUsuarioGateway);
        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Não deve alterar tipo de dono que possui restaurantes")
    void naoDeveAlterarTipoDeDonoComRestaurantes() {
        Usuario dono = criarUsuario(1L, new TipoUsuario(3L, "DONO_RESTAURANTE"));
        TipoUsuario cliente = new TipoUsuario(2L, "CLIENTE");
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(dono));
        when(tipoUsuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(cliente));
        when(restauranteGateway.existePorDonoId(1L)).thenReturn(true);

        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
                () -> useCase.associarTipo(1L, 2L));

        assertEquals("Usuário é dono de restaurantes. Transfira ou exclua os restaurantes antes de alterar o tipo",
                exception.getMessage());
        assertEquals("DONO_RESTAURANTE", dono.getTipoUsuario().getNome());
        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve alterar tipo de dono que não possui restaurantes")
    void deveAlterarTipoDeDonoSemRestaurantes() {
        Usuario dono = criarUsuario(1L, new TipoUsuario(3L, "DONO_RESTAURANTE"));
        TipoUsuario cliente = new TipoUsuario(2L, "CLIENTE");
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(dono));
        when(tipoUsuarioGateway.buscarPorId(2L)).thenReturn(Optional.of(cliente));
        when(restauranteGateway.existePorDonoId(1L)).thenReturn(false);
        when(usuarioGateway.salvar(dono)).thenReturn(dono);

        Usuario resultado = useCase.associarTipo(1L, 2L);

        assertSame(cliente, resultado.getTipoUsuario());
        verify(usuarioGateway).salvar(dono);
    }

    @Test
    @DisplayName("Deve permitir manter o tipo dono de restaurante sem consultar restaurantes")
    void deveManterTipoDonoSemConsultarRestaurantes() {
        Usuario dono = criarUsuario(1L, new TipoUsuario(3L, "DONO_RESTAURANTE"));
        TipoUsuario mesmoTipo = new TipoUsuario(3L, "DONO_RESTAURANTE");
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(dono));
        when(tipoUsuarioGateway.buscarPorId(3L)).thenReturn(Optional.of(mesmoTipo));
        when(usuarioGateway.salvar(dono)).thenReturn(dono);

        useCase.associarTipo(1L, 3L);

        verifyNoInteractions(restauranteGateway);
        verify(usuarioGateway).salvar(dono);
    }

    @Test
    @DisplayName("Não deve consultar restaurantes quando usuário não é dono")
    void naoDeveConsultarRestaurantesQuandoUsuarioNaoEhDono() {
        Usuario cliente = criarUsuario(1L, new TipoUsuario(2L, "CLIENTE"));
        TipoUsuario admin = new TipoUsuario(4L, "ADMIN");
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoUsuarioGateway.buscarPorId(4L)).thenReturn(Optional.of(admin));
        when(usuarioGateway.salvar(cliente)).thenReturn(cliente);

        useCase.associarTipo(1L, 4L);

        verifyNoInteractions(restauranteGateway);
    }

    private Usuario criarUsuario(Long id, TipoUsuario tipo) {
        return new Usuario(id, "Fabricio", new Email("fabricio@fiap.com"), tipo);
    }
}

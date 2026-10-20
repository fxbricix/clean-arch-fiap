package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.exceptions.RegraDeNegocioException;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RestauranteUseCaseImplTest {

    private IRestauranteGateway restauranteGateway;
    private IUsuarioGateway usuarioGateway;
    private RestauranteUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        restauranteGateway = mock(IRestauranteGateway.class);
        usuarioGateway = mock(IUsuarioGateway.class);
        useCase = new RestauranteUseCaseImpl(restauranteGateway, usuarioGateway);
    }

    @Test
    @DisplayName("Deve criar restaurante quando o dono existe")
    void deveCriarRestauranteComSucesso() {
        Restaurante restaurante = criarRestaurante(1L, 10L, "Restaurante");
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(criarUsuarioDono(10L)));
        when(restauranteGateway.salvar(restaurante)).thenReturn(restaurante);

        Restaurante resultado = useCase.criar(restaurante);

        assertSame(restaurante, resultado);
        verify(usuarioGateway).buscarPorId(10L);
        verify(restauranteGateway).salvar(restaurante);
    }

    @Test
    @DisplayName("Não deve criar restaurante quando o dono não existe")
    void naoDeveCriarRestauranteComDonoInexistente() {
        Restaurante restaurante = criarRestaurante(1L, 10L, "Restaurante");
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.criar(restaurante));

        verify(restauranteGateway, never()).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Não deve criar restaurante quando o usuário não é dono")
    void naoDeveCriarRestauranteQuandoUsuarioNaoEDono() {
        Restaurante restaurante = criarRestaurante(1L, 10L, "Restaurante");
        when(usuarioGateway.buscarPorId(10L)).thenReturn(Optional.of(criarUsuarioCliente(10L)));

        assertThrows(RegraDeNegocioException.class, () -> useCase.criar(restaurante));

        verify(restauranteGateway, never()).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID")
    void deveBuscarRestaurantePorId() {
        Restaurante restaurante = criarRestaurante(1L, 10L, "Restaurante");
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

        Restaurante resultado = useCase.buscarPorId(1L);

        assertSame(restaurante, resultado);
    }

    @Test
    @DisplayName("Não deve buscar restaurante inexistente")
    void naoDeveBuscarRestauranteInexistente() {
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.buscarPorId(1L));
    }

    @Test
    @DisplayName("Deve listar restaurantes")
    void deveListarRestaurantes() {
        List<Restaurante> restaurantes = List.of(
                criarRestaurante(1L, 10L, "Restaurante 1"),
                criarRestaurante(2L, 20L, "Restaurante 2"));
        when(restauranteGateway.listar()).thenReturn(restaurantes);

        List<Restaurante> resultado = useCase.listar();

        assertSame(restaurantes, resultado);
    }

    @Test
    @DisplayName("Deve atualizar os dados do restaurante, incluindo o dono, e salvar")
    void deveAtualizarRestaurante() {
        Restaurante existente = criarRestaurante(1L, 10L, "Nome anterior");
        HorarioFuncionamento novoHorario = new HorarioFuncionamento(LocalTime.of(11, 0), LocalTime.of(23, 0));
        Restaurante dadosAtualizados = new Restaurante(2L, "Nome novo", "Endereço novo", "Italiana", novoHorario, 20L);
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(usuarioGateway.buscarPorId(20L)).thenReturn(Optional.of(criarUsuarioDono(20L)));
        when(restauranteGateway.salvar(existente)).thenReturn(existente);

        Restaurante resultado = useCase.atualizar(1L, dadosAtualizados);

        assertSame(existente, resultado);
        assertEquals(1L, existente.getId());
        assertEquals("Nome novo", existente.getNome());
        assertEquals("Endereço novo", existente.getEndereco());
        assertEquals("Italiana", existente.getTipoCozinha());
        assertEquals(novoHorario, existente.getHorarioFuncionamento());
        assertEquals(20L, existente.getIdDono());
        verify(restauranteGateway).salvar(existente);
    }

    @Test
    @DisplayName("Não deve atualizar restaurante inexistente")
    void naoDeveAtualizarRestauranteInexistente() {
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> useCase.atualizar(1L, criarRestaurante(2L, 20L, "Restaurante")));

        verifyNoInteractions(usuarioGateway);
        verify(restauranteGateway, never()).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Não deve atualizar restaurante quando o novo dono não existe")
    void naoDeveAtualizarRestauranteComDonoInexistente() {
        Restaurante existente = criarRestaurante(1L, 10L, "Nome anterior");
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(usuarioGateway.buscarPorId(20L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> useCase.atualizar(1L, criarRestaurante(2L, 20L, "Nome novo")));

        assertEquals("Nome anterior", existente.getNome());
        verify(restauranteGateway, never()).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Não deve atualizar restaurante quando o novo dono não é dono de restaurante")
    void naoDeveAtualizarRestauranteQuandoNovoDonoNaoEDono() {
        Restaurante existente = criarRestaurante(1L, 10L, "Nome anterior");
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(usuarioGateway.buscarPorId(20L)).thenReturn(Optional.of(criarUsuarioCliente(20L)));

        assertThrows(RegraDeNegocioException.class,
                () -> useCase.atualizar(1L, criarRestaurante(2L, 20L, "Nome novo")));

        assertEquals("Nome anterior", existente.getNome());
        assertEquals(10L, existente.getIdDono());
        verify(restauranteGateway, never()).salvar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve excluir restaurante existente")
    void deveExcluirRestaurante() {
        Restaurante restaurante = criarRestaurante(1L, 10L, "Restaurante");
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

        useCase.excluir(1L);

        verify(restauranteGateway).excluir(1L);
    }

    @Test
    @DisplayName("Não deve excluir restaurante inexistente")
    void naoDeveExcluirRestauranteInexistente() {
        when(restauranteGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.excluir(1L));

        verify(restauranteGateway, never()).excluir(anyLong());
    }

    private Restaurante criarRestaurante(Long id, Long idDono, String nome) {
        return new Restaurante(id, nome, "Endereço", "Brasileira",
                new HorarioFuncionamento(LocalTime.of(9, 0), LocalTime.of(18, 0)), idDono);
    }

    private Usuario criarUsuarioDono(Long id) {
        return new Usuario(id, "Dono", new Email("dono@fiap.com"),
                new TipoUsuario(1L, TipoUsuario.DONO_RESTAURANTE));
    }

    private Usuario criarUsuarioCliente(Long id) {
        return new Usuario(id, "Cliente", new Email("cliente@fiap.com"),
                new TipoUsuario(2L, TipoUsuario.CLIENTE));
    }
}

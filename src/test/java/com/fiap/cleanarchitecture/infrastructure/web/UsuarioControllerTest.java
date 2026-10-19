package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.application.interfaces.IUsuarioUseCase;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import com.fiap.cleanarchitecture.infrastructure.web.controllers.UsuarioController;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.TipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioControllerTest {

    private IUsuarioUseCase useCase;
    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        useCase = mock(IUsuarioUseCase.class);
        controller = new UsuarioController(useCase);
    }

    @Test
    @DisplayName("Deve criar usuário e responder com status criado")
    void deveCriarUsuario() {
        CriarUsuarioDTO requisicao = new CriarUsuarioDTO("Fabricio", "fabricio@fiap.com", 2L);
        when(useCase.criar("Fabricio", "fabricio@fiap.com", 2L)).thenReturn(usuario(1L, "Fabricio", "CLIENTE"));

        ResponseEntity<UsuarioDTO> resposta = controller.criarUsuario(requisicao);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(usuarioDTO(1L, "Fabricio", "CLIENTE"), resposta.getBody());
        verify(useCase).criar("Fabricio", "fabricio@fiap.com", 2L);
    }

    @Test
    @DisplayName("Deve buscar usuário pelo ID")
    void deveBuscarUsuarioPorId() {
        when(useCase.buscarPorId(1L)).thenReturn(usuario(1L, "Fabricio", "CLIENTE"));

        UsuarioDTO resposta = controller.buscarPorId(1L);

        assertEquals(usuarioDTO(1L, "Fabricio", "CLIENTE"), resposta);
        verify(useCase).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve listar usuários")
    void deveListarUsuarios() {
        when(useCase.listar()).thenReturn(List.of(
                usuario(1L, "Fabricio", "CLIENTE"),
                usuario(2L, "Ana", "ADMIN")));

        List<UsuarioDTO> resposta = controller.listarUsuarios();

        assertEquals(List.of(usuarioDTO(1L, "Fabricio", "CLIENTE"),
                usuarioDTO(2L, "Ana", "ADMIN")), resposta);
        verify(useCase).listar();
    }

    @Test
    @DisplayName("Deve associar tipo ao usuário")
    void deveAssociarTipoAoUsuario() {
        when(useCase.associarTipo(1L, 3L)).thenReturn(usuario(1L, "Fabricio", "DONO_RESTAURANTE"));

        UsuarioDTO resposta = controller.associarTipo(1L, 3L);

        assertEquals(usuarioDTO(1L, "Fabricio", "DONO_RESTAURANTE"), resposta);
        verify(useCase).associarTipo(1L, 3L);
    }

    private static Usuario usuario(Long id, String nome, String tipoNome) {
        return new Usuario(id, nome, new Email(nome.toLowerCase() + "@fiap.com"),
                new TipoUsuario(2L, tipoNome));
    }

    private static UsuarioDTO usuarioDTO(Long id, String nome, String tipoNome) {
        return new UsuarioDTO(id, nome, nome.toLowerCase() + "@fiap.com", new TipoUsuarioDTO(2L, tipoNome));
    }
}

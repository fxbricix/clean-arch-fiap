package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.IntegrationTestBase;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.TipoUsuarioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TipoUsuarioControllerTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TipoUsuarioJpaRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limparDados() {
        jdbcTemplate.update("DELETE FROM usuario");
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um tipo de usuário com sucesso")
    void deveCriarTipoUsuarioComSucesso() throws Exception {
        mockMvc.perform(post("/v1/tipo-usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nome": "ADMIN"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("ADMIN"));

        assertTrue(repository.existsByNome("ADMIN"));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar tipo de usuário com nome inválido")
    void deveRetornarErroAoCriarTipoUsuarioComNomeInvalido() throws Exception {
        mockMvc.perform(post("/v1/tipo-usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nome": " "}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro ao criar tipo de usuário com nome muito longo")
    void deveRetornarErroAoCriarTipoUsuarioComNomeMuitoLongo() throws Exception {
        String nome = "A".repeat(51);

        mockMvc.perform(post("/v1/tipo-usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nome": "%s"}
                                """.formatted(nome)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar conflito ao criar tipo de usuário duplicado")
    void deveRetornarConflitoAoCriarTipoUsuarioDuplicado() throws Exception {
        repository.save(tipoUsuario("ADMIN"));

        mockMvc.perform(post("/v1/tipo-usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nome": "ADMIN"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve obter um tipo de usuário pelo ID")
    void deveObterTipoUsuarioPeloId() throws Exception {
        var tipoUsuario = repository.save(tipoUsuario("ADMIN"));

        mockMvc.perform(get("/v1/tipo-usuarios/{id}", tipoUsuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tipoUsuario.getId()))
                .andExpect(jsonPath("$.nome").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve retornar não encontrado ao buscar um tipo de usuário inexistente")
    void deveRetornarNaoEncontradoAoBuscarTipoUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/v1/tipo-usuarios/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar todos os tipos de usuário")
    void deveListarTodosOsTiposUsuario() throws Exception {
        repository.save(tipoUsuario("ADMIN"));
        repository.save(tipoUsuario("CLIENTE"));

        mockMvc.perform(get("/v1/tipo-usuarios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nome", containsInAnyOrder("ADMIN", "CLIENTE")));
    }

    @Test
    @DisplayName("Deve alterar o nome de um tipo de usuário")
    void deveAlterarTipoUsuario() throws Exception {
        var tipoUsuario = repository.save(tipoUsuario("ADMIN"));

        mockMvc.perform(patch("/v1/tipo-usuarios/{id}", tipoUsuario.getId())
                        .contentType("application/json")
                        .content("""
                                {"nome": "GERENTE"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tipoUsuario.getId()))
                .andExpect(jsonPath("$.nome").value("GERENTE"));

        assertTrue(repository.existsByNome("GERENTE"));
    }

    @Test
    @DisplayName("Deve retornar não encontrado ao alterar um tipo de usuário inexistente")
    void deveRetornarNaoEncontradoAoAlterarTipoUsuarioInexistente() throws Exception {
        mockMvc.perform(patch("/v1/tipo-usuarios/{id}", 999999L)
                        .contentType("application/json")
                        .content("""
                                {"nome": "GERENTE"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar conflito ao alterar para um nome já utilizado")
    void deveRetornarConflitoAoAlterarParaNomeExistente() throws Exception {
        var tipoUsuario = repository.save(tipoUsuario("ADMIN"));
        repository.save(tipoUsuario("CLIENTE"));

        mockMvc.perform(patch("/v1/tipo-usuarios/{id}", tipoUsuario.getId())
                        .contentType("application/json")
                        .content("""
                                {"nome": "CLIENTE"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve excluir um tipo de usuário")
    void deveExcluirTipoUsuario() throws Exception {
        var tipoUsuario = repository.save(tipoUsuario("ADMIN"));

        mockMvc.perform(delete("/v1/tipo-usuarios/{id}", tipoUsuario.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(tipoUsuario.getId()));
    }

    @Test
    @DisplayName("Deve retornar não encontrado ao excluir um tipo de usuário inexistente")
    void deveRetornarNaoEncontradoAoExcluirTipoUsuarioInexistente() throws Exception {
        mockMvc.perform(delete("/v1/tipo-usuarios/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar conflito ao excluir um tipo de usuário em uso")
    void deveRetornarConflitoAoExcluirTipoUsuarioEmUso() throws Exception {
        var tipoUsuario = repository.save(tipoUsuario("ADMIN"));
        jdbcTemplate.update(
                "INSERT INTO usuario (nome, email, tipo_usuario_id) VALUES (?, ?, ?)",
                "Usuário de teste", "teste@example.com", tipoUsuario.getId());

        mockMvc.perform(delete("/v1/tipo-usuarios/{id}", tipoUsuario.getId()))
                .andExpect(status().isConflict());
    }

    private TipoUsuarioEntity tipoUsuario(String nome) {
        var tipoUsuario = new TipoUsuarioEntity();
        tipoUsuario.setNome(nome);
        return tipoUsuario;
    }
}
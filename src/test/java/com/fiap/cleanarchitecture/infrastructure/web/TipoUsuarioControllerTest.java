package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.IntegrationTestBase;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.TipoUsuarioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItems;
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

    @Test
    @DisplayName("Deve criar um tipo de usuário com sucesso")
    void deveCriarTipoUsuarioComSucesso() throws Exception {
        String nome = nomeUnico();
        mockMvc.perform(post("/v1/tipo-usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nome": "%s"}
                                """.formatted(nome)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value(nome));

        assertTrue(repository.existsByNome(nome));
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
        String nome = nomeUnico();
        repository.save(tipoUsuario(nome));

        mockMvc.perform(post("/v1/tipo-usuarios")
                        .contentType("application/json")
                        .content("""
                                {"nome": "%s"}
                                """.formatted(nome)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve obter um tipo de usuário pelo ID")
    void deveObterTipoUsuarioPeloId() throws Exception {
        String nome = nomeUnico();
        var tipoUsuario = repository.save(tipoUsuario(nome));

        mockMvc.perform(get("/v1/tipo-usuarios/{id}", tipoUsuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tipoUsuario.getId()))
                .andExpect(jsonPath("$.nome").value(nome));
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
        String nomeA = nomeUnico();
        String nomeB = nomeUnico();
        repository.save(tipoUsuario(nomeA));
        repository.save(tipoUsuario(nomeB));

        mockMvc.perform(get("/v1/tipo-usuarios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nome", hasItems(nomeA, nomeB)));
    }

    @Test
    @DisplayName("Deve alterar o nome de um tipo de usuário")
    void deveAlterarTipoUsuario() throws Exception {
        String nome = nomeUnico();
        String novoNome = nomeUnico();
        var tipoUsuario = repository.save(tipoUsuario(nome));

        mockMvc.perform(patch("/v1/tipo-usuarios/{id}", tipoUsuario.getId())
                        .contentType("application/json")
                        .content("""
                                {"nome": "%s"}
                                """.formatted(novoNome)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tipoUsuario.getId()))
                .andExpect(jsonPath("$.nome").value(novoNome));

        assertTrue(repository.existsByNome(novoNome));
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
        String nomeOriginal = nomeUnico();
        String nomeExistente = nomeUnico();
        var tipoUsuario = repository.save(tipoUsuario(nomeOriginal));
        repository.save(tipoUsuario(nomeExistente));

        mockMvc.perform(patch("/v1/tipo-usuarios/{id}", tipoUsuario.getId())
                        .contentType("application/json")
                        .content("""
                                {"nome": "%s"}
                                """.formatted(nomeExistente)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve excluir um tipo de usuário")
    void deveExcluirTipoUsuario() throws Exception {
        var tipoUsuario = repository.save(tipoUsuario(nomeUnico()));

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
        var tipoUsuario = repository.save(tipoUsuario(nomeUnico()));
        jdbcTemplate.update(
                "INSERT INTO usuario (nome, email, tipo_usuario_id) VALUES (?, ?, ?)",
                "Usuário de teste", UUID.randomUUID() + "@teste.com", tipoUsuario.getId());

        mockMvc.perform(delete("/v1/tipo-usuarios/{id}", tipoUsuario.getId()))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar conflito ao excluir o tipo de usuário CLIENTE")
    void deveRetornarConflitoAoExcluirTipoUsuarioCliente() throws Exception {
        Long tipoClienteId = jdbcTemplate.queryForObject(
                "SELECT id FROM tipo_usuario WHERE nome = ?", Long.class, "CLIENTE");

        mockMvc.perform(delete("/v1/tipo-usuarios/{id}", tipoClienteId))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar conflito ao alterar o tipo de usuário CLIENTE")
    void deveRetornarConflitoAoAlterarTipoUsuarioCliente() throws Exception {
        Long tipoClienteId = jdbcTemplate.queryForObject(
                "SELECT id FROM tipo_usuario WHERE nome = ?", Long.class, "CLIENTE");

        mockMvc.perform(patch("/v1/tipo-usuarios/{id}", tipoClienteId)
                        .contentType("application/json")
                        .content("""
                                {"nome": "CLIENTE_ALTERADO"}
                                """))
                .andExpect(status().isConflict());
    }

    private static String nomeUnico() {
        return "T_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private TipoUsuarioEntity tipoUsuario(String nome) {
        var tipoUsuario = new TipoUsuarioEntity();
        tipoUsuario.setNome(nome);
        return tipoUsuario;
    }
}
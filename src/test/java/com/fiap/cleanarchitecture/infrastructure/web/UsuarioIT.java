package com.fiap.cleanarchitecture.infrastructure.web;

import com.fiap.cleanarchitecture.IntegrationTestBase;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UsuarioIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long idDoTipo(String nome) {
        return jdbcTemplate.queryForObject("SELECT id FROM tipo_usuario WHERE nome = ?", Long.class, nome);
    }

    private static String emailUnico() { return UUID.randomUUID().toString().substring(0, 8) + "@teste.com"; }

    private static String json(String nome, String email, Long tipoId) {
        return "{\"nome\":\"%s\",\"email\":\"%s\",\"tipoUsuarioId\":%s}".formatted(nome, email, tipoId);
    }

    private long criar(String email, long tipoId) throws Exception {
        String corpo = mockMvc.perform(post("/v1/usuarios").contentType(APPLICATION_JSON)
                        .content(json("Fulano de Teste", email, tipoId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(corpo, "$.id")).longValue();
    }

    @Test
    void criaUsuarioComTipoENormalizaOEmail() throws Exception {
        String email = emailUnico();
        mockMvc.perform(post("/v1/usuarios").contentType(APPLICATION_JSON)
                        .content(json("Maria", email.toUpperCase(), idDoTipo("CLIENTE"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.tipoUsuario.nome").value("CLIENTE"));
    }

    @Test
    void associaOUsuarioAoNovoTipo() throws Exception {
        long id = criar(emailUnico(), idDoTipo("CLIENTE"));
        mockMvc.perform(put("/v1/usuarios/{u}/tipo/{t}", id, idDoTipo("DONO_RESTAURANTE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoUsuario.nome").value("DONO_RESTAURANTE"));
        mockMvc.perform(get("/v1/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoUsuario.nome").value("DONO_RESTAURANTE"));
    }

    @Test
    void rejeitaEmailDuplicado() throws Exception {
        String email = emailUnico();
        long tipoId = idDoTipo("CLIENTE");
        criar(email, tipoId);

        mockMvc.perform(post("/v1/usuarios").contentType(APPLICATION_JSON)
                        .content(json("Outro Usuário", email, tipoId)))
                .andExpect(status().isConflict());
    }

    @Test
    void rejeitaTipoInexistenteAoCriarUsuario() throws Exception {
        mockMvc.perform(post("/v1/usuarios").contentType(APPLICATION_JSON)
                        .content(json("Maria", emailUnico(), -1L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaEmailInvalido() throws Exception {
        mockMvc.perform(post("/v1/usuarios").contentType(APPLICATION_JSON)
                        .content(json("Maria", "email-invalido", idDoTipo("CLIENTE"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscaUsuarioPorId() throws Exception {
        String email = emailUnico();
        long id = criar(email, idDoTipo("CLIENTE"));

        mockMvc.perform(get("/v1/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void retornaNaoEncontradoAoBuscarUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/v1/usuarios/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaUsuariosIncluindoUsuarioCriado() throws Exception {
        String email = emailUnico();
        criar(email, idDoTipo("CLIENTE"));

        mockMvc.perform(get("/v1/usuarios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].email", hasItem(email)));
    }

    @Test
    void rejeitaAssociacaoComUsuarioInexistente() throws Exception {
        mockMvc.perform(put("/v1/usuarios/{u}/tipo/{t}", -1, idDoTipo("CLIENTE")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaAssociacaoComTipoInexistente() throws Exception {
        long id = criar(emailUnico(), idDoTipo("CLIENTE"));

        mockMvc.perform(put("/v1/usuarios/{u}/tipo/{t}", id, -1))
                .andExpect(status().isNotFound());
    }
}

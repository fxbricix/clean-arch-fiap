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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RestauranteIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long idDoTipo(String nome) {
        return jdbcTemplate.queryForObject("SELECT id FROM tipo_usuario WHERE nome = ?", Long.class, nome);
    }

    private long criarUsuario(String tipo) throws Exception {
        String email = UUID.randomUUID().toString().substring(0, 8) + "@teste.com";
        String corpo = mockMvc.perform(post("/v1/usuarios").contentType(APPLICATION_JSON)
                        .content("{\"nome\":\"Fulano\",\"email\":\"%s\",\"tipoUsuarioId\":%d}"
                                .formatted(email, idDoTipo(tipo))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(corpo, "$.id")).longValue();
    }

    private static String json(String nome, Long donoId) {
        return ("{\"nome\":\"%s\",\"endereco\":\"Rua A, 10\",\"tipoCozinha\":\"Italiana\","
                + "\"horarioFuncionamento\":{\"abertura\":\"10:00\",\"fechamento\":\"22:30\"},\"donoId\":%s}")
                .formatted(nome, donoId);
    }

    private long criarRestaurante(String nome, long donoId) throws Exception {
        String corpo = mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON)
                        .content(json(nome, donoId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(corpo, "$.id")).longValue();
    }

    @Test
    void criaRestauranteParaDono() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON).content(json("Cantina", donoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Cantina"))
                .andExpect(jsonPath("$.donoId").value(donoId))
                .andExpect(jsonPath("$.horarioFuncionamento.abertura").value("10:00"))
                .andExpect(jsonPath("$.horarioFuncionamento.fechamento").value("22:30"));
    }

    @Test
    void rejeitaRestauranteParaUsuarioQueNaoEhDono() throws Exception {
        long clienteId = criarUsuario("CLIENTE");

        mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON).content(json("Cantina", clienteId)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void rejeitaRestauranteComDonoInexistente() throws Exception {
        mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON).content(json("Cantina", -1L)))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaRestauranteSemNome() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON).content(json("", donoId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaHorarioMalFormatado() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        String corpo = json("Cantina", donoId).replace("10:00", "dez horas");

        mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscaRestaurantePorId() throws Exception {
        long id = criarRestaurante("Cantina", criarUsuario("DONO_RESTAURANTE"));

        mockMvc.perform(get("/v1/restaurantes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Cantina"));
    }

    @Test
    void retornaNaoEncontradoAoBuscarRestauranteInexistente() throws Exception {
        mockMvc.perform(get("/v1/restaurantes/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaRestaurantesIncluindoRestauranteCriado() throws Exception {
        long id = criarRestaurante("Cantina Listada", criarUsuario("DONO_RESTAURANTE"));

        mockMvc.perform(get("/v1/restaurantes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem((int) id)));
    }

    @Test
    void atualizaRestaurante() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long id = criarRestaurante("Cantina", donoId);

        mockMvc.perform(put("/v1/restaurantes/{id}", id).contentType(APPLICATION_JSON)
                        .content(json("Cantina Nova", donoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cantina Nova"));
        mockMvc.perform(get("/v1/restaurantes/{id}", id))
                .andExpect(jsonPath("$.nome").value("Cantina Nova"));
    }

    @Test
    void rejeitaAtualizacaoDeRestauranteInexistente() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(put("/v1/restaurantes/{id}", -1).contentType(APPLICATION_JSON)
                        .content(json("Cantina", donoId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluiRestaurante() throws Exception {
        long id = criarRestaurante("Cantina", criarUsuario("DONO_RESTAURANTE"));

        mockMvc.perform(delete("/v1/restaurantes/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/v1/restaurantes/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejeitaExclusaoDeRestauranteInexistente() throws Exception {
        mockMvc.perform(delete("/v1/restaurantes/{id}", -1)).andExpect(status().isNotFound());
    }

    @Test
    void impedeAlterarTipoDeDonoComRestaurante() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        criarRestaurante("Cantina", donoId);

        mockMvc.perform(put("/v1/usuarios/{u}/tipo/{t}", donoId, idDoTipo("CLIENTE")))
                .andExpect(status().isUnprocessableEntity());
        mockMvc.perform(get("/v1/usuarios/{id}", donoId))
                .andExpect(jsonPath("$.tipoUsuario.nome").value("DONO_RESTAURANTE"));
    }

    @Test
    void permiteAlterarTipoDeDonoSemRestaurante() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(put("/v1/usuarios/{u}/tipo/{t}", donoId, idDoTipo("CLIENTE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoUsuario.nome").value("CLIENTE"));
    }

    @Test
    void permiteAlterarTipoDeDonoAposExcluirRestaurante() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long id = criarRestaurante("Cantina", donoId);
        mockMvc.perform(delete("/v1/restaurantes/{id}", id)).andExpect(status().isNoContent());

        mockMvc.perform(put("/v1/usuarios/{u}/tipo/{t}", donoId, idDoTipo("CLIENTE")))
                .andExpect(status().isOk());
    }
}

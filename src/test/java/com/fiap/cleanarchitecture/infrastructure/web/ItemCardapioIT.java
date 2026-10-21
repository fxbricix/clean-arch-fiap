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
class ItemCardapioIT extends IntegrationTestBase {

    private static final String CABECALHO = "X-Usuario-Id";

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

    private long criarRestaurante(long donoId) throws Exception {
        String corpo = mockMvc.perform(post("/v1/restaurantes").contentType(APPLICATION_JSON)
                        .content(("{\"nome\":\"Cantina\",\"endereco\":\"Rua A, 10\",\"tipoCozinha\":\"Italiana\","
                                + "\"horarioFuncionamento\":{\"abertura\":\"10:00\",\"fechamento\":\"22:00\"},"
                                + "\"donoId\":%d}").formatted(donoId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(corpo, "$.id")).longValue();
    }

    private static String json(String nome, String preco) {
        return ("{\"nome\":\"%s\",\"descricao\":\"Molho branco\",\"preco\":%s,"
                + "\"apenasNoLocal\":true,\"caminhoFoto\":\"/fotos/a.jpg\"}").formatted(nome, preco);
    }

    private long criarItem(long restauranteId, long usuarioId, String nome) throws Exception {
        String corpo = mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId)
                        .header(CABECALHO, usuarioId).contentType(APPLICATION_JSON).content(json(nome, "39.90")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(corpo, "$.id")).longValue();
    }

    @Test
    void criaItemParaODonoDoRestaurante() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Lasanha"))
                .andExpect(jsonPath("$.preco").value(39.90))
                .andExpect(jsonPath("$.apenasNoLocal").value(true))
                .andExpect(jsonPath("$.restauranteId").value(restauranteId));
    }

    @Test
    void rejeitaCriacaoPorOutroDono() throws Exception {
        long restauranteId = criarRestaurante(criarUsuario("DONO_RESTAURANTE"));
        long outroDono = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, outroDono)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.detail").value("Usuário não é o dono do restaurante"));
    }

    @Test
    void rejeitaCriacaoPorCliente() throws Exception {
        long restauranteId = criarRestaurante(criarUsuario("DONO_RESTAURANTE"));
        long clienteId = criarUsuario("CLIENTE");

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, clienteId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejeitaCriacaoEmRestauranteInexistente() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", -1).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaCriacaoComUsuarioInexistente() throws Exception {
        long restauranteId = criarRestaurante(criarUsuario("DONO_RESTAURANTE"));

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, -1)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaCriacaoSemCabecalhoDeUsuario() throws Exception {
        long restauranteId = criarRestaurante(criarUsuario("DONO_RESTAURANTE"));

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Cabeçalho obrigatório ausente: X-Usuario-Id"));
    }

    @Test
    void rejeitaCabecalhoDeUsuarioNaoNumerico() throws Exception {
        long restauranteId = criarRestaurante(criarUsuario("DONO_RESTAURANTE"));

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, "abc")
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaIdDeRestauranteNaoNumerico() throws Exception {
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", "abc", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaItemSemNome() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("", "39.90")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.nome").exists());
    }

    @Test
    void rejeitaItemSemIndicarApenasNoLocal() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        String corpo = "{\"nome\":\"Lasanha\",\"descricao\":\"Molho\",\"preco\":10.00}";

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.apenasNoLocal").exists());
    }

    @Test
    void rejeitaPrecoZeroOuComTresCasasDecimais() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);

        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "0")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.preco").exists());
        mockMvc.perform(post("/v1/restaurantes/{r}/itens", restauranteId).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "10.999")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.preco").exists());
    }

    @Test
    void buscaItemPorId() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long id = criarItem(restauranteId, donoId, "Lasanha");

        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", restauranteId, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Lasanha"));
    }

    @Test
    void naoEncontraItemPeloRestauranteErrado() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long id = criarItem(criarRestaurante(donoId), donoId, "Lasanha");
        long outroRestaurante = criarRestaurante(donoId);

        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", outroRestaurante, id))
                .andExpect(status().isNotFound());
    }

    @Test
    void retornaNaoEncontradoAoBuscarItemEmRestauranteInexistente() throws Exception {
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", -1, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaApenasItensDoRestaurante() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long outroRestaurante = criarRestaurante(donoId);
        long id = criarItem(restauranteId, donoId, "Lasanha");
        long idDoOutro = criarItem(outroRestaurante, donoId, "Pizza");

        mockMvc.perform(get("/v1/restaurantes/{r}/itens/listar", restauranteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem((int) id)))
                .andExpect(jsonPath("$[*].id", org.hamcrest.Matchers.not(hasItem((int) idDoOutro))));
    }

    @Test
    void retornaNaoEncontradoAoListarRestauranteInexistente() throws Exception {
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/listar", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizaItem() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long id = criarItem(restauranteId, donoId, "Lasanha");

        mockMvc.perform(put("/v1/restaurantes/{r}/itens/{id}", restauranteId, id).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha Nova", "45.50")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lasanha Nova"));
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", restauranteId, id))
                .andExpect(jsonPath("$.nome").value("Lasanha Nova"))
                .andExpect(jsonPath("$.preco").value(45.50));
    }

    @Test
    void rejeitaAtualizacaoPorOutroDono() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long id = criarItem(restauranteId, donoId, "Lasanha");
        long outroDono = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(put("/v1/restaurantes/{r}/itens/{id}", restauranteId, id).header(CABECALHO, outroDono)
                        .contentType(APPLICATION_JSON).content(json("Hackeado", "1.00")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", restauranteId, id))
                .andExpect(jsonPath("$.nome").value("Lasanha"));
    }

    @Test
    void rejeitaAtualizacaoDeItemInexistente() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);

        mockMvc.perform(put("/v1/restaurantes/{r}/itens/{id}", restauranteId, -1).header(CABECALHO, donoId)
                        .contentType(APPLICATION_JSON).content(json("Lasanha", "39.90")))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluiItem() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long id = criarItem(restauranteId, donoId, "Lasanha");

        mockMvc.perform(delete("/v1/restaurantes/{r}/itens/{id}", restauranteId, id).header(CABECALHO, donoId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", restauranteId, id))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaExclusaoPorOutroDono() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long id = criarItem(restauranteId, donoId, "Lasanha");
        long outroDono = criarUsuario("DONO_RESTAURANTE");

        mockMvc.perform(delete("/v1/restaurantes/{r}/itens/{id}", restauranteId, id).header(CABECALHO, outroDono))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/v1/restaurantes/{r}/itens/{id}", restauranteId, id))
                .andExpect(status().isOk());
    }

    @Test
    void rejeitaExclusaoDeRestauranteComItensAteOItemSerExcluido() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);
        long itemId = criarItem(restauranteId, donoId, "Lasanha");

        mockMvc.perform(delete("/v1/restaurantes/{id}", restauranteId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Não foi possível excluir restaurante"));
        mockMvc.perform(get("/v1/restaurantes/{id}", restauranteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restauranteId));

        mockMvc.perform(delete("/v1/restaurantes/{r}/itens/{id}", restauranteId, itemId).header(CABECALHO, donoId))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/v1/restaurantes/{id}", restauranteId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/v1/restaurantes/{id}", restauranteId))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaExclusaoDeItemInexistente() throws Exception {
        long donoId = criarUsuario("DONO_RESTAURANTE");
        long restauranteId = criarRestaurante(donoId);

        mockMvc.perform(delete("/v1/restaurantes/{r}/itens/{id}", restauranteId, -1).header(CABECALHO, donoId))
                .andExpect(status().isNotFound());
    }
}

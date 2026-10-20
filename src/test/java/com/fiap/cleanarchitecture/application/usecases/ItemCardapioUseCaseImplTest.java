package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.AcessoNegadoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.gateways.IItemCardapioGateway;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import com.fiap.cleanarchitecture.domain.valueobjects.HorarioFuncionamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemCardapioUseCaseImplTest {

    private static final long RESTAURANTE_ID = 7L;
    private static final long DONO_ID = 10L;
    private static final long OUTRO_DONO_ID = 11L;
    private static final long CLIENTE_ID = 12L;
    private static final long ITEM_ID = 1L;

    private IItemCardapioGateway itemGateway;
    private IRestauranteGateway restauranteGateway;
    private IUsuarioGateway usuarioGateway;
    private ItemCardapioUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        itemGateway = mock(IItemCardapioGateway.class);
        restauranteGateway = mock(IRestauranteGateway.class);
        usuarioGateway = mock(IUsuarioGateway.class);
        useCase = new ItemCardapioUseCaseImpl(itemGateway, restauranteGateway, usuarioGateway);

        when(restauranteGateway.buscarPorId(RESTAURANTE_ID)).thenReturn(Optional.of(restaurante()));
        when(usuarioGateway.buscarPorId(DONO_ID)).thenReturn(Optional.of(usuario(DONO_ID, "DONO_RESTAURANTE")));
        when(usuarioGateway.buscarPorId(OUTRO_DONO_ID))
                .thenReturn(Optional.of(usuario(OUTRO_DONO_ID, "DONO_RESTAURANTE")));
        when(usuarioGateway.buscarPorId(CLIENTE_ID)).thenReturn(Optional.of(usuario(CLIENTE_ID, "CLIENTE")));
    }

    @Nested
    @DisplayName("criar")
    class Criar {

        @Test
        @DisplayName("Deve criar item para o dono, associado ao restaurante")
        void deveCriarItem() {
            when(itemGateway.salvar(any(ItemCardapio.class))).thenAnswer(i -> i.getArgument(0));

            ItemCardapio resultado = chamar(RESTAURANTE_ID, DONO_ID);

            ArgumentCaptor<ItemCardapio> captor = ArgumentCaptor.forClass(ItemCardapio.class);
            verify(itemGateway).salvar(captor.capture());
            assertEquals(RESTAURANTE_ID, captor.getValue().getIdRestaurante());
            assertEquals("Lasanha", resultado.getNome());
            assertEquals(new BigDecimal("39.90"), resultado.getPreco());
        }

        @Test
        @DisplayName("Não deve criar item em restaurante inexistente")
        void restauranteInexistente() {
            when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> chamar(99L, DONO_ID));

            verify(itemGateway, never()).salvar(any(ItemCardapio.class));
        }

        @Test
        @DisplayName("Não deve criar item para usuário inexistente")
        void usuarioInexistente() {
            when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> chamar(RESTAURANTE_ID, 99L));

            verify(itemGateway, never()).salvar(any(ItemCardapio.class));
        }

        @Test
        @DisplayName("Não deve criar item para dono de outro restaurante")
        void outroDono() {
            assertThrows(AcessoNegadoException.class, () -> chamar(RESTAURANTE_ID, OUTRO_DONO_ID));

            verify(itemGateway, never()).salvar(any(ItemCardapio.class));
        }

        @Test
        @DisplayName("Não deve criar item para cliente")
        void cliente() {
            assertThrows(AcessoNegadoException.class, () -> chamar(RESTAURANTE_ID, CLIENTE_ID));

            verify(itemGateway, never()).salvar(any(ItemCardapio.class));
        }

        private ItemCardapio chamar(Long restauranteId, Long usuarioId) {
            return useCase.criar(restauranteId, usuarioId, "Lasanha", "Molho branco",
                    new BigDecimal("39.90"), false, null);
        }
    }

    @Nested
    @DisplayName("buscar e listar")
    class BuscarEListar {

        @Test
        @DisplayName("Deve buscar item do restaurante")
        void deveBuscarItem() {
            ItemCardapio item = item();
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.of(item));

            assertSame(item, useCase.buscarPorId(RESTAURANTE_ID, ITEM_ID));
        }

        @Test
        @DisplayName("Não deve buscar item em restaurante inexistente")
        void buscarRestauranteInexistente() {
            when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.buscarPorId(99L, ITEM_ID));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve buscar item de outro restaurante")
        void buscarItemDeOutroRestaurante() {
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.buscarPorId(RESTAURANTE_ID, ITEM_ID));
        }

        @Test
        @DisplayName("Deve listar itens do restaurante")
        void deveListarItens() {
            List<ItemCardapio> itens = List.of(item());
            when(itemGateway.listarPorRestauranteId(RESTAURANTE_ID)).thenReturn(itens);

            assertSame(itens, useCase.listar(RESTAURANTE_ID));
        }

        @Test
        @DisplayName("Não deve listar itens de restaurante inexistente")
        void listarRestauranteInexistente() {
            when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.listar(99L));

            verifyNoInteractions(itemGateway);
        }
    }

    @Nested
    @DisplayName("atualizar")
    class Atualizar {

        @Test
        @DisplayName("Deve atualizar item do restaurante para o dono")
        void deveAtualizarItem() {
            ItemCardapio item = item();
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.of(item));
            when(itemGateway.salvar(item)).thenReturn(item);

            ItemCardapio resultado = chamar(RESTAURANTE_ID, DONO_ID, new BigDecimal("45.00"));

            assertSame(item, resultado);
            assertEquals("Novo nome", item.getNome());
            assertEquals(new BigDecimal("45.00"), item.getPreco());
            verify(itemGateway).salvar(item);
        }

        @Test
        @DisplayName("Não deve atualizar em restaurante inexistente")
        void restauranteInexistente() {
            when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> chamar(99L, DONO_ID, BigDecimal.TEN));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve atualizar para usuário inexistente")
        void usuarioInexistente() {
            when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> chamar(RESTAURANTE_ID, 99L, BigDecimal.TEN));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve atualizar para dono de outro restaurante")
        void outroDono() {
            assertThrows(AcessoNegadoException.class, () -> chamar(RESTAURANTE_ID, OUTRO_DONO_ID, BigDecimal.TEN));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve atualizar para cliente")
        void cliente() {
            assertThrows(AcessoNegadoException.class, () -> chamar(RESTAURANTE_ID, CLIENTE_ID, BigDecimal.TEN));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve atualizar item inexistente")
        void itemInexistente() {
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> chamar(RESTAURANTE_ID, DONO_ID, BigDecimal.TEN));

            verify(itemGateway, never()).salvar(any(ItemCardapio.class));
        }

        @Test
        @DisplayName("Não deve alterar o item quando o domínio rejeita os dados")
        void dominioRejeitaDados() {
            ItemCardapio item = item();
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.of(item));

            assertThrows(DominioException.class,
                    () -> chamar(RESTAURANTE_ID, DONO_ID, new BigDecimal("-1")));

            assertEquals("Lasanha", item.getNome());
            assertEquals(new BigDecimal("39.90"), item.getPreco());
            verify(itemGateway, never()).salvar(any(ItemCardapio.class));
        }

        private ItemCardapio chamar(Long restauranteId, Long usuarioId, BigDecimal preco) {
            return useCase.atualizar(restauranteId, ITEM_ID, usuarioId, "Novo nome", "Nova descrição",
                    preco, true, null);
        }
    }

    @Nested
    @DisplayName("excluir")
    class Excluir {

        @Test
        @DisplayName("Deve excluir item do restaurante para o dono")
        void deveExcluirItem() {
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.of(item()));

            useCase.excluir(RESTAURANTE_ID, ITEM_ID, DONO_ID);

            verify(itemGateway).excluir(ITEM_ID);
        }

        @Test
        @DisplayName("Não deve excluir em restaurante inexistente")
        void restauranteInexistente() {
            when(restauranteGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.excluir(99L, ITEM_ID, DONO_ID));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve excluir para usuário inexistente")
        void usuarioInexistente() {
            when(usuarioGateway.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.excluir(RESTAURANTE_ID, ITEM_ID, 99L));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve excluir para dono de outro restaurante")
        void outroDono() {
            assertThrows(AcessoNegadoException.class,
                    () -> useCase.excluir(RESTAURANTE_ID, ITEM_ID, OUTRO_DONO_ID));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve excluir para cliente")
        void cliente() {
            assertThrows(AcessoNegadoException.class, () -> useCase.excluir(RESTAURANTE_ID, ITEM_ID, CLIENTE_ID));

            verifyNoInteractions(itemGateway);
        }

        @Test
        @DisplayName("Não deve excluir item inexistente")
        void itemInexistente() {
            when(itemGateway.buscarPorIdERestauranteId(ITEM_ID, RESTAURANTE_ID)).thenReturn(Optional.empty());

            assertThrows(RecursoNaoEncontradoException.class,
                    () -> useCase.excluir(RESTAURANTE_ID, ITEM_ID, DONO_ID));

            verify(itemGateway, never()).excluir(anyLong());
        }
    }

    private static Restaurante restaurante() {
        return new Restaurante(RESTAURANTE_ID, "Cantina", "Rua A, 10", "Italiana",
                new HorarioFuncionamento(LocalTime.of(10, 0), LocalTime.of(22, 0)), DONO_ID);
    }

    private static Usuario usuario(Long id, String tipo) {
        return new Usuario(id, "Usuario " + id, new Email("u" + id + "@fiap.com"), new TipoUsuario(1L, tipo));
    }

    private static ItemCardapio item() {
        return new ItemCardapio(ITEM_ID, "Lasanha", "Molho branco", new BigDecimal("39.90"), false, null,
                RESTAURANTE_ID);
    }
}

package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class TipoUsuarioUseCaseImplTest {

    ITipoUsuarioGateway gateway = mock(ITipoUsuarioGateway.class);
    TipoUsuarioUseCaseImpl useCase = new TipoUsuarioUseCaseImpl(gateway);

    @Nested
    @DisplayName("Casos de sucesso")
    class casosDeSucesso {

        @Test
        @DisplayName("Deve criar um tipo de usuário com sucesso")
        public void deveCriarTipoUsuarioComSucesso() {

            TipoUsuario tipo = new TipoUsuario("CLIENTE");
            when(gateway.existePorNome(tipo.getNome())).thenReturn(false);
            when(gateway.salvar(tipo)).thenReturn(tipo);

            TipoUsuario resultado = useCase.criar(tipo);

            assertEquals(tipo, resultado);
            verify(gateway).salvar(tipo);
        }

        @Test
        @DisplayName("Deve deletar um tipo de usuário com sucesso")
        public void deveDeletarTipoUsuarioComSucesso() {
            when(gateway.buscarPorId(1L)).thenReturn(Optional.of(new TipoUsuario("CLIENTE")));

            useCase.deletar(1L);

            verify(gateway).excluir(1L);
        }

        @Test
        @DisplayName("Deve alterar um tipo de usuário com sucesso")
        public void deveAlterarTipoUsuarioComSucesso() {
            when(gateway.buscarPorId(1L)).thenReturn(Optional.of(new TipoUsuario("CLIENTE")));
            when(gateway.existePorNome("ADMIN")).thenReturn(false);
            when(gateway.atualizar(any(TipoUsuario.class))).thenAnswer(i -> i.getArguments()[0]);

            useCase.alterar(1L, "ADMIN");

            verify(gateway).atualizar(any(TipoUsuario.class));
        }

        @Test
        @DisplayName("Deve permitir manter o mesmo nome")
        public void devePermitirManterMesmoNome() {
            TipoUsuario tipo = new TipoUsuario("CLIENTE");
            when(gateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));
            when(gateway.atualizar(any(TipoUsuario.class)))
                    .thenAnswer(invocacao -> invocacao.getArgument(0));

            TipoUsuario resultado = useCase.alterar(1L, "CLIENTE");

            assertEquals("CLIENTE", resultado.getNome());
            verify(gateway, never()).existePorNome(anyString());
            verify(gateway).atualizar(tipo);
        }

        @Test
        @DisplayName("Deve buscar um tipo de usuário por ID com sucesso")
        public void deveBuscarTipoUsuarioPorIdComSucesso() {
            TipoUsuario tipo = new TipoUsuario("CLIENTE");
            when(gateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));

            useCase.buscarPorId(1L);

            verify(gateway).buscarPorId(1L);
        }

        @Test
        @DisplayName("Deve listar todos os tipos de usuário com sucesso")
        public void deveListarTodosTiposUsuarioComSucesso() {
            when(gateway.listar()).thenReturn(List.of(new TipoUsuario("CLIENTE"), new TipoUsuario("ADMIN")));

            useCase.listar();

            verify(gateway).listar();
        }
    }

    @Nested
    @DisplayName("Casos de falha")
    class casosDeFalha {

        @Test
        @DisplayName("Não deve criar um tipo de usuário que já existe")
        public void naoDeveCriarTipoUsuarioQueJaExiste() {
            TipoUsuario tipo = new TipoUsuario("CLIENTE");
            when(gateway.existePorNome(tipo.getNome())).thenReturn(true);

            assertThrows(ConflitoException.class, () -> useCase.criar(tipo));
        }

        @Test
        @DisplayName("Não deve deletar um tipo de usuário que não existe")
        public void naoDeveDeletarTipoUsuarioQueNaoExiste() {
            when(gateway.buscarPorId(1L)).thenReturn(Optional.empty());
            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.deletar(1L));
        }

        @Test
        @DisplayName("Não deve alterar um tipo de usuário que não existe")
        public void naoDeveAlterarTipoUsuarioQueNaoExiste() {
            when(gateway.buscarPorId(1L)).thenReturn(Optional.empty());
            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.alterar(1L, "ADMIN"));
        }

        @Test
        @DisplayName("Não deve alterar um tipo de usuário para um nome que já existe")
        public void naoDeveAlterarTipoUsuarioParaNomeQueJaExiste() {
            TipoUsuario tipo = new TipoUsuario("CLIENTE");
            when(gateway.buscarPorId(1L)).thenReturn(Optional.of(tipo));
            when(gateway.existePorNome("ADMIN")).thenReturn(true);
            assertThrows(ConflitoException.class, () -> useCase.alterar(1L, "ADMIN"));
        }

        @Test
        @DisplayName("Não deve encontrar um tipo de usuário que não existe")
        public void naoDeveEncontrarUmUsuarioQueNaoExiste(){
            when(gateway.buscarPorId(1L)).thenReturn(Optional.empty());
            assertThrows(RecursoNaoEncontradoException.class, () -> useCase.buscarPorId(1L));
        }

        @Test
        @DisplayName("Não deve listar tipos de usuário quando não houver nenhum")
        public void naoDeveListarTiposUsuarioQuandoNaoHouverNenhum() {
            when(gateway.listar()).thenReturn(List.of());
            List<TipoUsuario> resultado = useCase.listar();
            assertEquals(0, resultado.size());
        }

    }
}
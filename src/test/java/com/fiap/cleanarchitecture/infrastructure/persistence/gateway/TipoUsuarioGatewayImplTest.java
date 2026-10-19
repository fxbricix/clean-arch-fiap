package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.gateway.TipoUsuarioGatewayImpl;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.TipoUsuarioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TipoUsuarioGatewayImplTest {

    private final TipoUsuarioJpaRepository repository = mock(TipoUsuarioJpaRepository.class);
    private final TipoUsuarioGatewayImpl gateway = new TipoUsuarioGatewayImpl(repository);

    @Nested
    @DisplayName("Casos de Sucesso")
    class casosDeSucesso {

        @Test
        @DisplayName("Deve salvar um tipo de usuário com sucesso")
        void deveSalvarTipoUsuario() {
            TipoUsuarioEntity salvo = new TipoUsuarioEntity();
            salvo.setId(1L);
            salvo.setNome("Admin");
            when(repository.save(any(TipoUsuarioEntity.class))).thenReturn(salvo);

            TipoUsuario resultado = gateway.salvar(new TipoUsuario(1L, "Admin"));

            assertEquals(1L, resultado.getId());
            assertEquals("Admin", resultado.getNome());
            verify(repository).save(any(TipoUsuarioEntity.class));
        }

        @Test
        @DisplayName("Deve buscar um tipo de usuário por ID com sucesso")
        void deveBuscarTipoUsuarioPorId() {
            TipoUsuarioEntity entity = new TipoUsuarioEntity();
            entity.setId(1L);
            entity.setNome("Admin");
            when(repository.findById(1L)).thenReturn(Optional.of(entity));

            Optional<TipoUsuario> resultado = gateway.buscarPorId(1L);

            assertEquals(1L, resultado.get().getId());
            assertEquals("Admin", resultado.get().getNome());
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("Deve listar todos os tipos de usuário com sucesso")
        void deveListarTiposUsuario() {
            TipoUsuarioEntity entity1 = new TipoUsuarioEntity();
            entity1.setId(1L);
            entity1.setNome("Admin");
            TipoUsuarioEntity entity2 = new TipoUsuarioEntity();
            entity2.setId(2L);
            entity2.setNome("User");
            when(repository.findAll()).thenReturn(List.of(entity1, entity2));

            assertEquals(2, gateway.listar().size());
            verify(repository).findAll();
        }

        @Test
        @DisplayName("Deve verificar se existe um tipo de usuário por nome com sucesso")
        void deveVerificarExistenciaPorNome() {
            when(repository.existsByNome("Admin")).thenReturn(true);

            assertEquals(true, gateway.existePorNome("Admin"));
            verify(repository).existsByNome("Admin");
        }

        @Test
        @DisplayName("Deve deletar um tipo de usuário com sucesso")
        void deveDeletarTipoUsuario() {
            doNothing().when(repository).deleteById(1L);

            gateway.excluir(1L);

            verify(repository).deleteById(1L);
            verify(repository).flush();
        }

        @Test
        @DisplayName("Deve atualizar um tipo de usuário com sucesso")
        void deveAtualizarTipoUsuario() {
            TipoUsuarioEntity salvo = new TipoUsuarioEntity();
            salvo.setId(1L);
            salvo.setNome("Admin");
            when(repository.save(any(TipoUsuarioEntity.class))).thenReturn(salvo);

            assertEquals(1L, gateway.atualizar(new TipoUsuario(1L, "Admin")).getId());
            verify(repository).save(any(TipoUsuarioEntity.class));
        }
    }

    @Nested
    @DisplayName("Casos de Erro")
    class casosDeErro {
        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar um tipo de usuário em uso")
        void deveLancarExcecaoAoDeletarTipoUsuarioEmUso() {
            doThrow(new DataIntegrityViolationException("fk")).when(repository).deleteById(1L);

            assertThrows(ConflitoException.class, () -> gateway.excluir(1L));
            verify(repository).deleteById(1L);
        }
    }

}
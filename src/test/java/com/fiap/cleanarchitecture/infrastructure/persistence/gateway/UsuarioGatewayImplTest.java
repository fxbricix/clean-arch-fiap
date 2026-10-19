package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.UsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.gateway.UsuarioGatewayImpl;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioGatewayImplTest {

    private UsuarioJpaRepository repository;
    private UsuarioGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        repository = mock(UsuarioJpaRepository.class);
        gateway = new UsuarioGatewayImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void deveSalvarUsuario() {
        UsuarioEntity salvo = usuarioEntity(1L, "Fabricio", "fabricio@fiap.com", 2L, "CLIENTE");
        when(repository.save(any(UsuarioEntity.class))).thenReturn(salvo);

        Usuario resultado = gateway.salvar(usuario(1L, "Fabricio", "fabricio@fiap.com"));

        assertEquals(1L, resultado.getId());
        assertEquals("Fabricio", resultado.getNome());
        assertEquals(new Email("fabricio@fiap.com"), resultado.getEmail());
        assertEquals(2L, resultado.getTipoUsuario().getId());
        verify(repository).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("Deve converter violação de integridade ao salvar em conflito")
    void deveLancarConflitoAoSalvarDuplicado() {
        when(repository.save(any(UsuarioEntity.class)))
                .thenThrow(new DataIntegrityViolationException("violação de integridade"));

        ConflitoException exception = assertThrows(ConflitoException.class,
                () -> gateway.salvar(usuario(null, "Fabricio", "fabricio@fiap.com")));

        assertEquals("E-mail já cadastrado ou tipo de usuário inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(usuarioEntity(1L, "Fabricio", "fabricio@fiap.com", 2L, "CLIENTE")));

        Optional<Usuario> resultado = gateway.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Fabricio", resultado.get().getNome());
        assertEquals("fabricio@fiap.com", resultado.get().getEmail().valor());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar usuário inexistente")
    void deveRetornarVazioAoBuscarUsuarioInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(gateway.buscarPorId(99L).isEmpty());
    }

    @Test
    @DisplayName("Deve listar usuários ordenados pelo nome")
    void deveListarUsuariosOrdenadosPorNome() {
        UsuarioEntity primeiro = usuarioEntity(1L, "Ana", "ana@fiap.com", 2L, "CLIENTE");
        UsuarioEntity segundo = usuarioEntity(2L, "Bruno", "bruno@fiap.com", 2L, "CLIENTE");
        when(repository.findAll(Sort.by("nome"))).thenReturn(List.of(primeiro, segundo));

        List<Usuario> resultado = gateway.listar();

        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNome());
        assertEquals("Bruno", resultado.get(1).getNome());
        verify(repository).findAll(Sort.by("nome"));
    }

    @Test
    @DisplayName("Deve verificar se existe usuário pelo e-mail")
    void deveVerificarExistenciaPorEmail() {
        when(repository.existsByEmail("fabricio@fiap.com")).thenReturn(true);

        assertTrue(gateway.existePorEmail("fabricio@fiap.com"));

        when(repository.existsByEmail("inexistente@fiap.com")).thenReturn(false);
        assertFalse(gateway.existePorEmail("inexistente@fiap.com"));
        verify(repository).existsByEmail("fabricio@fiap.com");
        verify(repository).existsByEmail("inexistente@fiap.com");
    }

    private static Usuario usuario(Long id, String nome, String email) {
        return new Usuario(id, nome, new Email(email), new TipoUsuario(2L, "CLIENTE"));
    }

    private static UsuarioEntity usuarioEntity(Long id, String nome, String email, Long tipoId, String tipoNome) {
        TipoUsuarioEntity tipo = new TipoUsuarioEntity();
        tipo.setId(tipoId);
        tipo.setNome(tipoNome);
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);
        entity.setNome(nome);
        entity.setEmail(email);
        entity.setTipoUsuario(tipo);
        return entity;
    }
}

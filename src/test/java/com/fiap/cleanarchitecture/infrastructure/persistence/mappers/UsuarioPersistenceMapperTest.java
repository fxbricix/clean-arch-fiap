package com.fiap.cleanarchitecture.infrastructure.persistence.mappers;

import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.UsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UsuarioPersistenceMapperTest {

    private final UsuarioPersistenceMapper mapper = new UsuarioPersistenceMapper();

    @Test
    @DisplayName("Deve converter entidade de persistência para entidade de domínio")
    void deveConverterParaEntidadeDeDominio() {
        TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity();
        tipoEntity.setId(2L);
        tipoEntity.setNome("CLIENTE");
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(1L);
        entity.setNome("Fabricio");
        entity.setEmail("fabricio@fiap.com");
        entity.setTipoUsuario(tipoEntity);

        Usuario usuario = mapper.toDomainEntity(entity);

        assertEquals(1L, usuario.getId());
        assertEquals("Fabricio", usuario.getNome());
        assertEquals(new Email("fabricio@fiap.com"), usuario.getEmail());
        assertEquals(2L, usuario.getTipoUsuario().getId());
        assertEquals("CLIENTE", usuario.getTipoUsuario().getNome());
    }

    @Test
    @DisplayName("Deve converter entidade de domínio para entidade de persistência")
    void deveConverterParaEntidadeDePersistencia() {
        Usuario usuario = new Usuario(1L, "Fabricio", new Email("fabricio@fiap.com"),
                new TipoUsuario(2L, "CLIENTE"));

        UsuarioEntity entity = mapper.toEntity(usuario);

        assertEquals(1L, entity.getId());
        assertEquals("Fabricio", entity.getNome());
        assertEquals("fabricio@fiap.com", entity.getEmail());
        assertEquals(2L, entity.getTipoUsuario().getId());
        assertEquals("CLIENTE", entity.getTipoUsuario().getNome());
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter entidade de persistência nula")
    void deveRetornarNuloAoConverterEntidadeDePersistenciaNula() {
        assertNull(mapper.toDomainEntity(null));
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter entidade de domínio nula")
    void deveRetornarNuloAoConverterEntidadeDeDominioNula() {
        assertNull(mapper.toEntity(null));
    }
}

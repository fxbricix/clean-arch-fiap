package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioTest {

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        TipoUsuario tipoUsuario = new TipoUsuario(2L, "ADMIN");

        Usuario usuario = new Usuario(1L, "Fabricio", new Email("fabricio@fiap.com"), tipoUsuario);

        assertEquals(1L, usuario.getId());
        assertEquals("Fabricio", usuario.getNome());
        assertEquals(new Email("fabricio@fiap.com"), usuario.getEmail());
        assertEquals(tipoUsuario, usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve criar um usuário sem ID")
    void deveCriarUsuarioSemId() {
        Usuario usuario = new Usuario("Fabricio", new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE"));

        assertNull(usuario.getId());
    }

    @Test
    @DisplayName("Deve remover espaços excedentes do nome")
    void deveNormalizarNome() {
        Usuario usuario = new Usuario("  Fabricio  ", new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE"));

        assertEquals("Fabricio", usuario.getNome());
    }

    @Test
    @DisplayName("Não deve criar usuário com nome nulo")
    void naoDeveCriarUsuarioComNomeNulo() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new Usuario(1L, null, new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE")));

        assertEquals("O nome do usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar usuário com nome em branco")
    void naoDeveCriarUsuarioComNomeEmBranco() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new Usuario(1L, "   ", new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE")));

        assertEquals("O nome do usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar usuário com nome maior que o limite")
    void naoDeveCriarUsuarioComNomeMuitoLongo() {
        String nomeMuitoLongo = "A".repeat(Usuario.TAMANHO_MAXIMO_NOME + 1);

        DominioException exception = assertThrows(DominioException.class,
                () -> new Usuario(1L, nomeMuitoLongo, new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE")));

        assertEquals("O nome do usuário deve ter no máximo 120 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar usuário com e-mail nulo")
    void naoDeveCriarUsuarioComEmailNulo() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new Usuario(1L, "Fabricio", null, new TipoUsuario("CLIENTE")));

        assertEquals("O e-mail do usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar usuário com tipo nulo")
    void naoDeveCriarUsuarioComTipoNulo() {
        DominioException exception = assertThrows(DominioException.class,
                () -> new Usuario(1L, "Fabricio", new Email("fabricio@fiap.com"), null));

        assertEquals("O tipo de usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve alterar o tipo de usuário")
    void deveAlterarTipoUsuario() {
        Usuario usuario = new Usuario("Fabricio", new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE"));
        TipoUsuario novoTipo = new TipoUsuario("DONO_RESTAURANTE");

        usuario.alterarTipo(novoTipo);

        assertEquals(novoTipo, usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Não deve alterar o tipo de usuário para nulo")
    void naoDeveAlterarTipoUsuarioParaNulo() {
        Usuario usuario = new Usuario("Fabricio", new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE"));

        DominioException exception = assertThrows(DominioException.class, () -> usuario.alterarTipo(null));

        assertEquals("O tipo de usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve identificar usuário dono de restaurante")
    void deveIdentificarDonoDeRestaurante() {
        Usuario usuario = new Usuario("Fabricio", new Email("fabricio@fiap.com"),
                new TipoUsuario("DONO_RESTAURANTE"));

        assertTrue(usuario.ehDonoDeRestaurante());
    }

    @Test
    @DisplayName("Deve identificar usuário que não é dono de restaurante")
    void deveIdentificarUsuarioQueNaoEhDonoDeRestaurante() {
        Usuario usuario = new Usuario("Fabricio", new Email("fabricio@fiap.com"), new TipoUsuario("CLIENTE"));

        assertFalse(usuario.ehDonoDeRestaurante());
    }
}

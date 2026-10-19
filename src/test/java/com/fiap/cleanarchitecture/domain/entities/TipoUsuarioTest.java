package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TipoUsuarioTest {

    @Test
    @DisplayName("Deve criar um TipoUsuario válido")
    public void deveCriarTipoUsuarioValido() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        assertEquals("ADMIN", tipoUsuario.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TipoUsuario com nome nulo")
    public void deveLancarExcecaoNomeNulo() {
        DominioException exception = assertThrows(DominioException.class, () -> new TipoUsuario(null));
        assertEquals("O nome do tipo de usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TipoUsuario com nome em branco")
    public void deveLancarExcecaoNomeEmBranco() {
        DominioException exception = assertThrows(DominioException.class, () -> new TipoUsuario("   "));
        assertEquals("O nome do tipo de usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TipoUsuario com nome muito longo")
    public void deveLancarExcecaoNomeMuitoLongo() {
        String nomeMuitoLongo = "A".repeat(51);
        DominioException exception = assertThrows(DominioException.class, () -> new TipoUsuario(nomeMuitoLongo));
        assertEquals("O nome deve ter no máximo 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Deve alterar o nome do TipoUsuario")
    public void deveAlterarNomeTipoUsuario() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        tipoUsuario.alterarNome("USER");
        assertEquals("USER", tipoUsuario.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar nome do TipoUsuario para nulo")
    public void deveLancarExcecaoAlterarNomeNulo() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        DominioException exception = assertThrows(DominioException.class, () -> tipoUsuario.alterarNome(null));
        assertEquals("O nome do tipo de usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar nome do TipoUsuario para em branco")
    public void deveLancarExcecaoAlterarNomeEmBranco() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        DominioException exception = assertThrows(DominioException.class, () -> tipoUsuario.alterarNome("   "));
        assertEquals("O nome do tipo de usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar nome do TipoUsuario para muito longo")
    public void deveLancarExcecaoAlterarNomeMuitoLongo() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        String nomeMuitoLongo = "A".repeat(51);
        DominioException exception = assertThrows(DominioException.class, () -> tipoUsuario.alterarNome(nomeMuitoLongo));
        assertEquals("O nome deve ter no máximo 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se o TipoUsuario é dono de restaurante")
    public void deveVerificarDonoDeRestaurante() {
        TipoUsuario tipoUsuario = new TipoUsuario("DONO_RESTAURANTE");
        assertTrue(tipoUsuario.ehDonoDeRestaurante());
    }

    @Test
    @DisplayName("Deve identificar os tipos de usuário sistêmicos")
    public void deveVerificarTipoSistemico() {
        assertTrue(new TipoUsuario(TipoUsuario.CLIENTE).ehTipoSistemico());
        assertTrue(new TipoUsuario(TipoUsuario.DONO_RESTAURANTE).ehTipoSistemico());
        assertFalse(new TipoUsuario("ADMIN").ehTipoSistemico());
    }

    @Test
    @DisplayName("Deve identificar quando o tipo não é de dono de restaurante")
    public void deveVerificarQueNaoEhDonoDeRestaurante() {
        assertFalse(new TipoUsuario(TipoUsuario.CLIENTE).ehDonoDeRestaurante());
    }
}

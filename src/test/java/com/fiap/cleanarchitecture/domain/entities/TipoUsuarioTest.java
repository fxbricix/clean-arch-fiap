package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TipoUsuarioTest {

    @Test
    @DisplayName("Deve criar um TipoUsuario válido")
    public void deveCriarTipoUsuarioValido() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        assert tipoUsuario.getNome().equals("ADMIN");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TipoUsuario com nome nulo")
    public void deveLancarExcecaoNomeNulo() {
        try {
            new TipoUsuario(null);
            assert false;
        } catch (DominioException e) {
            assert e.getMessage().equals("O nome do tipo de usuário é obrigatório");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TipoUsuario com nome em branco")
    public void deveLancarExcecaoNomeEmBranco() {
        try {
            new TipoUsuario("   ");
            assert false;
        } catch (DominioException e) {
            assert e.getMessage().equals("O nome do tipo de usuário é obrigatório");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar TipoUsuario com nome muito longo")
    public void deveLancarExcecaoNomeMuitoLongo() {
        String nomeMuitoLongo = "A".repeat(51);
        try {
            new TipoUsuario(nomeMuitoLongo);
            assert false;
        } catch (DominioException e) {
            assert e.getMessage().equals("O nome deve ter no máximo 50 caracteres");
        }
    }

    @Test
    @DisplayName("Deve alterar o nome do TipoUsuario")
    public void deveAlterarNomeTipoUsuario() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        tipoUsuario.alterarNome("USER");
        assert tipoUsuario.getNome().equals("USER");
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar nome do TipoUsuario para nulo")
    public void deveLancarExcecaoAlterarNomeNulo() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        try {
            tipoUsuario.alterarNome(null);
            assert false;
        } catch (DominioException e) {
            assert e.getMessage().equals("O nome do tipo de usuário é obrigatório");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar nome do TipoUsuario para em branco")
    public void deveLancarExcecaoAlterarNomeEmBranco() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        try {
            tipoUsuario.alterarNome("   ");
            assert false;
        } catch (DominioException e) {
            assert e.getMessage().equals("O nome do tipo de usuário é obrigatório");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar nome do TipoUsuario para muito longo")
    public void deveLancarExcecaoAlterarNomeMuitoLongo() {
        TipoUsuario tipoUsuario = new TipoUsuario("ADMIN");
        String nomeMuitoLongo = "A".repeat(51);
        try {
            tipoUsuario.alterarNome(nomeMuitoLongo);
            assert false;
        } catch (DominioException e) {
            assert e.getMessage().equals("O nome deve ter no máximo 50 caracteres");
        }
    }

    @Test
    @DisplayName("Deve verificar se o TipoUsuario é dono de restaurante")
    public void deveVerificarDonoDeRestaurante() {
        TipoUsuario tipoUsuario = new TipoUsuario("DONO_RESTAURANTE");
        assert tipoUsuario.ehDonoDeRestaurante();
    }
}

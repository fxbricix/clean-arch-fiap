package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.exceptions.RegraDeNegocioException;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.IUsuarioUseCase;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import com.fiap.cleanarchitecture.domain.valueobjects.Email;

import java.util.List;

public class UsuarioUseCaseImpl implements IUsuarioUseCase {

    private final IUsuarioGateway usuarioGateway;
    private final ITipoUsuarioGateway tipoUsuarioGateway;
    private final IRestauranteGateway restauranteGateway;

    public UsuarioUseCaseImpl(IUsuarioGateway usuarioGateway, ITipoUsuarioGateway tipoUsuarioGateway, IRestauranteGateway restauranteGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    @Override
    public Usuario criar(String nome, String email, Long tipoUsuarioId) {
        TipoUsuario tipo = buscarTipo(tipoUsuarioId);
        Usuario usuario = new Usuario(nome, new Email(email), tipo);
        if (usuarioGateway.existePorEmail(usuario.getEmail().valor())) {
            throw new ConflitoException("Já existe um usuário com o e-mail " + usuario.getEmail().valor());
        }
        return usuarioGateway.salvar(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioGateway.buscarPorId(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
    }

    @Override
    public List<Usuario> listar() {
        return usuarioGateway.listar();
    }

    @Override
    public Usuario associarTipo(Long usuarioId, Long tipoUsuarioId) {
        Usuario usuario = buscarPorId(usuarioId);
        TipoUsuario novoTipo = buscarTipo(tipoUsuarioId);

        if (usuario.ehDonoDeRestaurante()
                && !novoTipo.ehDonoDeRestaurante()
                && restauranteGateway.existePorDonoId(usuario.getId())) {
            throw new RegraDeNegocioException(
                    "Usuário é dono de restaurantes. Transfira ou exclua os restaurantes antes de alterar o tipo");
        }

        usuario.alterarTipo(novoTipo);
        return usuarioGateway.salvar(usuario);
    }

    private TipoUsuario buscarTipo(Long id) {
        if (id == null) {
            throw new DominioException("O tipo de usuário é obrigatório");
        }
        return tipoUsuarioGateway.buscarPorId(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Tipo de usuário não encontrado: " + id));
    }
}
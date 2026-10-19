package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.exceptions.RegraDeNegocioException;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.IRestauranteUseCase;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.domain.entities.Usuario;

import java.util.List;
import java.util.Optional;

public class RestauranteUseCaseImpl implements IRestauranteUseCase {

    private final IRestauranteGateway restauranteGateway;
    private final IUsuarioGateway usuarioGateway;

    public RestauranteUseCaseImpl(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public Restaurante criar(Restaurante restaurante) {
        if (restaurante.getIdDono() == null) {
            throw new RegraDeNegocioException("Restaurante deve ter um dono");
        }
        validarDono(restaurante.getIdDono());

        return restauranteGateway.salvar(restaurante);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return Optional.of(restauranteGateway.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado")));
    }

    @Override
    public List<Restaurante> listar() {
        return restauranteGateway.listar();
    }

    @Override
    public Restaurante atualizar(Long id, Restaurante restaurante) {
        Restaurante existente = buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado"));

        validarDono(restaurante.getIdDono());

        existente.alterarDados(
                restaurante.getNome(),
                restaurante.getEndereco(),
                restaurante.getTipoCozinha(),
                restaurante.getHorarioFuncionamento()
        );

        return restauranteGateway.salvar(existente);
    }

    @Override
    public void excluir(Long id) {
        buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado"));

        restauranteGateway.excluir(id);
    }

    private void validarDono(Long id) {
        Optional<Usuario> usuario = usuarioGateway.buscarPorId(id);
        if (!usuario.isPresent()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }

        if (!usuario.get().ehDonoDeRestaurante()) {
            throw new RegraDeNegocioException("Usuário não é dono de restaurante");
        }
    }
}

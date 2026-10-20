package com.fiap.cleanarchitecture.application.usecases;

import com.fiap.cleanarchitecture.application.exceptions.AcessoNegadoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.application.gateways.IItemCardapioGateway;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.application.interfaces.IItemCardapioUseCase;
import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public class ItemCardapioUseCaseImpl implements IItemCardapioUseCase {

    private final IItemCardapioGateway itemCardapioGateway;
    private final IRestauranteGateway restauranteGateway;
    private final IUsuarioGateway usuarioGateway;

    public ItemCardapioUseCaseImpl(IItemCardapioGateway itemCardapioGateway,
                                   IRestauranteGateway restauranteGateway,
                                   IUsuarioGateway usuarioGateway) {
        this.itemCardapioGateway = itemCardapioGateway;
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public ItemCardapio criar(Long restauranteId, Long usuarioId, String nome, String descricao,
                              BigDecimal preco, boolean apenasNoLocal, String caminhoFoto) {
        exigirDono(restauranteId, usuarioId);

        ItemCardapio item = new ItemCardapio(null, nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId);
        return itemCardapioGateway.salvar(item);
    }

    @Override
    public ItemCardapio buscarPorId(Long restauranteId, Long id) {
        exigirRestaurante(restauranteId);
        return buscarItem(id, restauranteId);
    }

    @Override
    public List<ItemCardapio> listar(Long restauranteId) {
        exigirRestaurante(restauranteId);
        return itemCardapioGateway.listarPorRestauranteId(restauranteId);
    }

    @Override
    public ItemCardapio atualizar(Long restauranteId, Long id, Long usuarioId, String nome, String descricao,
                                  BigDecimal preco, boolean apenasNoLocal, String caminhoFoto) {
        exigirDono(restauranteId, usuarioId);

        ItemCardapio item = buscarItem(id, restauranteId);
        item.alterarDados(nome, descricao, preco, apenasNoLocal, caminhoFoto);
        return itemCardapioGateway.salvar(item);
    }

    @Override
    public void excluir(Long restauranteId, Long id, Long usuarioId) {
        exigirDono(restauranteId, usuarioId);

        buscarItem(id, restauranteId);
        itemCardapioGateway.excluir(id);
    }

    private Restaurante exigirRestaurante(Long restauranteId) {
        return restauranteGateway.buscarPorId(restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Restaurante não encontrado"));
    }

    private ItemCardapio buscarItem(Long id, Long restauranteId) {
        return itemCardapioGateway.buscarPorIdERestauranteId(id, restauranteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item do cardápio não encontrado"));
    }

    private void exigirDono(Long restauranteId, Long usuarioId) {
        Restaurante restaurante = exigirRestaurante(restauranteId);

        usuarioGateway.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!restaurante.getIdDono().equals(usuarioId)) {
            throw new AcessoNegadoException("Usuário não é o dono do restaurante");
        }
    }
}

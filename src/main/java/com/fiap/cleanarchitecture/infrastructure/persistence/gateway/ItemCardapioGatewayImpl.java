package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.gateways.IItemCardapioGateway;
import com.fiap.cleanarchitecture.domain.entities.ItemCardapio;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.ItemCardapioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.mappers.ItemCardapioPersistenceMapper;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.ItemCardapioJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemCardapioGatewayImpl implements IItemCardapioGateway {

    private final ItemCardapioJpaRepository repository;
    private static final ItemCardapioPersistenceMapper MAPPER = new ItemCardapioPersistenceMapper();

    public ItemCardapioGatewayImpl(ItemCardapioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ItemCardapio salvar(ItemCardapio item) {
        ItemCardapioEntity saved = repository.save(MAPPER.toEntity(item));
        return MAPPER.toDomainEntity(saved);
    }

    @Override
    public Optional<ItemCardapio> buscarPorIdERestauranteId(Long id, Long restauranteId) {
        return repository.findByIdAndRestauranteId(id, restauranteId).map(MAPPER::toDomainEntity);
    }

    @Override
    public List<ItemCardapio> listarPorRestauranteId(Long restauranteId) {
        return repository.findByRestauranteIdOrderByIdAsc(restauranteId).stream()
                .map(MAPPER::toDomainEntity)
                .toList();
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException("Não foi possível excluir item do cardápio");
        }
    }
}

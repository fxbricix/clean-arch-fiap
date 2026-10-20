package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.gateways.IRestauranteGateway;
import com.fiap.cleanarchitecture.domain.entities.Restaurante;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.RestauranteEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.mappers.RestaurantePersistenceMapper;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.RestauranteJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestauranteGatewayImpl implements IRestauranteGateway {

    private final RestauranteJpaRepository repository;
    private static final RestaurantePersistenceMapper MAPPER = new RestaurantePersistenceMapper();

    public RestauranteGatewayImpl(RestauranteJpaRepository repository) {
        this.repository = repository;
    }


    @Override
    public Restaurante salvar(Restaurante restaurante) {
        RestauranteEntity entity = MAPPER.toEntity(restaurante);
        RestauranteEntity savedEntity = repository.save(entity);
        return MAPPER.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        RestauranteEntity entity = repository.findById(id).orElse(null);
        Restaurante restaurante = MAPPER.toDomainEntity(entity);
        return Optional.ofNullable(restaurante);
    }

    @Override
    public List<Restaurante> listar() {
        return repository.findAll().stream()
                .map(MAPPER::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException(
                    "Não foi possível excluir restaurante");
        }
    }

    @Override
    public boolean existePorDonoId(Long donoId) {
        return repository.existsByDonoId(donoId);
    }
}

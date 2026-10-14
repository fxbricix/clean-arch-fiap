package com.fiap.cleanarchitecture.infrastructure.persistence.repository;

import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.infrastructure.mapper.TipoUsuarioMapper;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TipoUsuarioGatewayImpl implements ITipoUsuarioGateway {

    public final TipoUsuarioJpaRepository repository;
    public static final TipoUsuarioMapper MAPPER = new TipoUsuarioMapper();

    public TipoUsuarioGatewayImpl(TipoUsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity tipoUsuarioEntity = MAPPER.toEntity(tipoUsuario);
        var usuarioSalvo = repository.save(tipoUsuarioEntity);
        return MAPPER.toDomainEntity(usuarioSalvo);
    }

    @Override
    public Optional<TipoUsuario> buscarPorId(Long id) {
        TipoUsuarioEntity tipoUsuarioEntity = repository.findById(id).orElse(null);
        return Optional.ofNullable(MAPPER.toDomainEntity(tipoUsuarioEntity));
    }

    @Override
    public List<TipoUsuario> listar() {
        return repository.findAll().stream()
                .map(MAPPER::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existePorNome(String nome) {
        return repository.existsByNome(nome);
    }

    @Override
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Override
    public TipoUsuario atualizar(TipoUsuario tipoUsuario) {
        return salvar(tipoUsuario);
    }
}

package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.gateways.ITipoUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.TipoUsuario;
import com.fiap.cleanarchitecture.infrastructure.persistence.mappers.TipoUsuarioPersistenceMapper;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.TipoUsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.TipoUsuarioJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TipoUsuarioGatewayImpl implements ITipoUsuarioGateway {

    private final TipoUsuarioJpaRepository repository;
    private static final TipoUsuarioPersistenceMapper MAPPER = new TipoUsuarioPersistenceMapper();

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
    @Transactional
    public void excluir(Long id) {
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException(
                    "Tipo de usuário em uso por usuários cadastrados e não pode ser excluído");
        }
    }

    @Override
    public TipoUsuario atualizar(TipoUsuario tipoUsuario) {
        return salvar(tipoUsuario);
    }
}

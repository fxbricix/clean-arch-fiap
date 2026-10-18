package com.fiap.cleanarchitecture.infrastructure.persistence.gateway;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.gateways.IUsuarioGateway;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.infrastructure.persistence.entities.UsuarioEntity;
import com.fiap.cleanarchitecture.infrastructure.persistence.mappers.UsuarioPersistenceMapper;
import com.fiap.cleanarchitecture.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioGatewayImpl implements IUsuarioGateway {

    private final UsuarioJpaRepository repository;
    private static final UsuarioPersistenceMapper MAPPER = new UsuarioPersistenceMapper();

    public UsuarioGatewayImpl(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        try {
            UsuarioEntity salvo = repository.save(MAPPER.toEntity(usuario));
            return MAPPER.toDomainEntity(salvo);
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException("E-mail já cadastrado ou tipo de usuário inválido");
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(MAPPER::toDomainEntity);
    }

    @Override
    public List<Usuario> listar() {
        return repository.findAll(Sort.by("nome")).stream()
                .map(MAPPER::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }
}
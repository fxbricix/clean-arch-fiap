package com.fiap.cleanarchitecture.infrastructure.web.controllers;

import com.fiap.cleanarchitecture.application.interfaces.ITipoUsuarioUseCase;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarTipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.TipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.mappers.TipoUsuarioWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipo-usuarios")
public class TipoUsuarioController {

    private final ITipoUsuarioUseCase useCase;
    private static final TipoUsuarioWebMapper MAPPER = new TipoUsuarioWebMapper();

    public TipoUsuarioController(ITipoUsuarioUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<TipoUsuarioDTO> criarTipoUsuario(@RequestBody @Valid CriarTipoUsuarioDTO criarTipoUsuarioDTO) {
        var domainEntity = MAPPER.toDomainEntity(criarTipoUsuarioDTO);
        var tipoCriado = useCase.criar(domainEntity);
        return ResponseEntity.status(201).body(MAPPER.toDTO(tipoCriado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> obterTipoUsuario(@PathVariable Long id) {
        var tipoUsuario = useCase.buscarPorId(id);
        return ResponseEntity.ok(MAPPER.toDTO(tipoUsuario));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TipoUsuarioDTO>> obterTodosTiposUsuarios() {
        return ResponseEntity.ok(useCase.listar().stream().map(MAPPER::toDTO).toList());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoUsuarioDTO> atualizarTipoUsuario(@PathVariable Long id, @RequestBody @Valid CriarTipoUsuarioDTO criarTipoUsuarioDTO) {
        var domainEntity = MAPPER.toDomainEntity(criarTipoUsuarioDTO);
        var tipoAtualizado = useCase.alterar(id, domainEntity.getNome());
        return ResponseEntity.ok(MAPPER.toDTO(tipoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTipoUsuario(@PathVariable Long id) {
        useCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

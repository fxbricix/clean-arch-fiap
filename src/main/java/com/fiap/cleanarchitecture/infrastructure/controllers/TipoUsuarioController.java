package com.fiap.cleanarchitecture.infrastructure.controllers;

import com.fiap.cleanarchitecture.application.interfaces.ITipoUsuarioUseCase;
import com.fiap.cleanarchitecture.infrastructure.controllers.dtos.CriarTipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.controllers.dtos.TipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.mapper.TipoUsuarioMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipo-usuarios")
public class TipoUsuarioController {

    private final ITipoUsuarioUseCase useCase;
    private static final TipoUsuarioMapper MAPPER = new TipoUsuarioMapper();

    public TipoUsuarioController(ITipoUsuarioUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public TipoUsuarioDTO criarUsuario(@RequestBody CriarTipoUsuarioDTO criarTipoUsuarioDTO) {
        var domainEntity = MAPPER.toDomainEntity(criarTipoUsuarioDTO);
        var tipoCriado = useCase.criar(domainEntity);
        return MAPPER.toDTO(tipoCriado);
    }

    @GetMapping("/{id}")
    public TipoUsuarioDTO obterUsuario(@PathVariable Long id) {
        var tipoUsuario = useCase.buscarPorId(id);
        return MAPPER.toDTO(tipoUsuario);
    }

    @GetMapping("/todos")
    public List<TipoUsuarioDTO> obterTodosUsuarios() {
        return useCase.listar()
                .stream().map(MAPPER::toDTO)
                .toList();
    }

    @PatchMapping("/{id}")
    public TipoUsuarioDTO atualizarUsuario(@PathVariable Long id, @RequestBody CriarTipoUsuarioDTO criarTipoUsuarioDTO) {
        var domainEntity = MAPPER.toDomainEntity(criarTipoUsuarioDTO);
        var tipoAtualizado = useCase.alterar(id, domainEntity.getNome());
        return MAPPER.toDTO(tipoAtualizado);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        useCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

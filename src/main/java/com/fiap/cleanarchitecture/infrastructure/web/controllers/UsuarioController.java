package com.fiap.cleanarchitecture.infrastructure.web.controllers;

import com.fiap.cleanarchitecture.application.interfaces.IUsuarioUseCase;
import com.fiap.cleanarchitecture.domain.entities.Usuario;
import com.fiap.cleanarchitecture.infrastructure.web.docs.UsuarioApi;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.UsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.mappers.UsuarioWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController implements UsuarioApi {

    private final IUsuarioUseCase useCase;
    private final UsuarioWebMapper mapper;

    public UsuarioController(IUsuarioUseCase useCase) {
        this.useCase = useCase;
        this.mapper = new UsuarioWebMapper();
    }

    @Override
    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario (@RequestBody @Valid CriarUsuarioDTO criarUsuarioDTO) {
        var usuario = useCase.criar(criarUsuarioDTO.nome(), criarUsuarioDTO.email(), criarUsuarioDTO.tipoUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(usuario));
    }

    @Override
    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId (@PathVariable Long id){
        var usuario = useCase.buscarPorId(id);
        return mapper.toDTO(usuario);
    }

    @Override
    @GetMapping("/listar")
    public List<UsuarioDTO> listarUsuarios(){
        var usuarios = useCase.listar();
        return usuarios.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @PutMapping("/{usuarioId}/tipo/{tipoUsuarioId}")
    public UsuarioDTO associarTipo(@PathVariable Long usuarioId, @PathVariable Long tipoUsuarioId) {
        var usuario = useCase.associarTipo(usuarioId, tipoUsuarioId);
        return mapper.toDTO(usuario);
    }
}

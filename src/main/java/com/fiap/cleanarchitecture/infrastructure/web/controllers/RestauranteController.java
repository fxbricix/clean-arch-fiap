package com.fiap.cleanarchitecture.infrastructure.web.controllers;

import com.fiap.cleanarchitecture.application.interfaces.IRestauranteUseCase;
import com.fiap.cleanarchitecture.infrastructure.web.docs.RestauranteApi;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarRestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.RestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.mappers.RestauranteWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes")
public class RestauranteController implements RestauranteApi {

    private final IRestauranteUseCase useCase;
    private final RestauranteWebMapper mapper;

    public RestauranteController(IRestauranteUseCase useCase) {
        this.useCase = useCase;
        this.mapper = new RestauranteWebMapper();
    }

    @Override
    @PostMapping
    public ResponseEntity<RestauranteDTO> criarRestaurante(@RequestBody @Valid CriarRestauranteDTO dto) {
        var restaurante = useCase.criar(mapper.toDomainEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(restaurante));
    }

    @Override
    @GetMapping("/{id}")
    public RestauranteDTO buscarPorId(@PathVariable Long id) {
        return mapper.toDTO(useCase.buscarPorId(id));
    }

    @Override
    @GetMapping("/listar")
    public List<RestauranteDTO> listarRestaurantes() {
        return useCase.listar().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @PutMapping("/{id}")
    public RestauranteDTO atualizarRestaurante(@PathVariable Long id,
                                               @RequestBody @Valid CriarRestauranteDTO dto) {
        var restaurante = useCase.atualizar(id, mapper.toDomainEntity(dto));
        return mapper.toDTO(restaurante);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirRestaurante(@PathVariable Long id) {
        useCase.excluir(id);
    }
}
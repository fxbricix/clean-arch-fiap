package com.fiap.cleanarchitecture.infrastructure.web.controllers;

import com.fiap.cleanarchitecture.application.interfaces.IItemCardapioUseCase;
import com.fiap.cleanarchitecture.infrastructure.web.docs.ItemCardapioApi;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarItemCardapioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.ItemCardapioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.mappers.ItemCardapioWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/itens")
public class ItemCardapioController implements ItemCardapioApi {

    private final IItemCardapioUseCase useCase;
    private final ItemCardapioWebMapper mapper;

    public ItemCardapioController(IItemCardapioUseCase useCase) {
        this.useCase = useCase;
        this.mapper = new ItemCardapioWebMapper();
    }

    @Override
    @PostMapping
    public ResponseEntity<ItemCardapioDTO> criarItem(@PathVariable Long restauranteId,
                                                     @RequestHeader("X-Usuario-Id") Long usuarioId,
                                                     @RequestBody @Valid CriarItemCardapioDTO dto) {
        var item = useCase.criar(restauranteId, usuarioId, dto.nome(), dto.descricao(), dto.preco(),
                dto.apenasNoLocal(), dto.caminhoFoto());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(item));
    }

    @Override
    @GetMapping("/{id}")
    public ItemCardapioDTO buscarPorId(@PathVariable Long restauranteId, @PathVariable Long id) {
        return mapper.toDTO(useCase.buscarPorId(restauranteId, id));
    }

    @Override
    @GetMapping("/listar")
    public List<ItemCardapioDTO> listarItens(@PathVariable Long restauranteId) {
        return useCase.listar(restauranteId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @PutMapping("/{id}")
    public ItemCardapioDTO atualizarItem(@PathVariable Long restauranteId,
                                         @PathVariable Long id,
                                         @RequestHeader("X-Usuario-Id") Long usuarioId,
                                         @RequestBody @Valid CriarItemCardapioDTO dto) {
        var item = useCase.atualizar(restauranteId, id, usuarioId, dto.nome(), dto.descricao(), dto.preco(),
                dto.apenasNoLocal(), dto.caminhoFoto());
        return mapper.toDTO(item);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirItem(@PathVariable Long restauranteId,
                            @PathVariable Long id,
                            @RequestHeader("X-Usuario-Id") Long usuarioId) {
        useCase.excluir(restauranteId, id, usuarioId);
    }
}

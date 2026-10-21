package com.fiap.cleanarchitecture.infrastructure.web.docs;

import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarItemCardapioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.ItemCardapioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Contrato documentado (OpenAPI) dos endpoints de itens do cardápio.
 * <p>
 * As operações de escrita exigem o cabeçalho {@code X-Usuario-Id}, que deve identificar o dono do restaurante.
 */
@Tag(name = "Itens do cardápio", description = "Gerenciamento dos itens do cardápio de um restaurante")
public interface ItemCardapioApi {

    @Operation(summary = "Cria um item no cardápio", description = "Apenas o dono do restaurante pode criar itens.")
    @ApiResponse(responseCode = "201", description = "Item criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou cabeçalho ausente",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é o dono do restaurante",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<ItemCardapioDTO> criarItem(@Parameter(description = "Id do restaurante", example = "1") Long restauranteId,
                                              @Parameter(description = "Id do usuário que executa a operação", example = "1") Long usuarioId,
                                              CriarItemCardapioDTO dto);

    @Operation(summary = "Busca um item do cardápio pelo id")
    @ApiResponse(responseCode = "200", description = "Item encontrado")
    @ApiResponse(responseCode = "404", description = "Item ou restaurante não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ItemCardapioDTO buscarPorId(@Parameter(description = "Id do restaurante", example = "1") Long restauranteId,
                                @Parameter(description = "Id do item", example = "1") Long id);

    @Operation(summary = "Lista os itens do cardápio de um restaurante")
    @ApiResponse(responseCode = "200", description = "Lista de itens")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    List<ItemCardapioDTO> listarItens(@Parameter(description = "Id do restaurante", example = "1") Long restauranteId);

    @Operation(summary = "Atualiza um item do cardápio", description = "Apenas o dono do restaurante pode atualizar itens.")
    @ApiResponse(responseCode = "200", description = "Item atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou cabeçalho ausente",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é o dono do restaurante",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item, restaurante ou usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ItemCardapioDTO atualizarItem(@Parameter(description = "Id do restaurante", example = "1") Long restauranteId,
                                  @Parameter(description = "Id do item", example = "1") Long id,
                                  @Parameter(description = "Id do usuário que executa a operação", example = "1") Long usuarioId,
                                  CriarItemCardapioDTO dto);

    @Operation(summary = "Exclui um item do cardápio", description = "Apenas o dono do restaurante pode excluir itens.")
    @ApiResponse(responseCode = "204", description = "Item excluído")
    @ApiResponse(responseCode = "400", description = "Cabeçalho ausente",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não é o dono do restaurante",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item, restaurante ou usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void excluirItem(@Parameter(description = "Id do restaurante", example = "1") Long restauranteId,
                     @Parameter(description = "Id do item", example = "1") Long id,
                     @Parameter(description = "Id do usuário que executa a operação", example = "1") Long usuarioId);
}

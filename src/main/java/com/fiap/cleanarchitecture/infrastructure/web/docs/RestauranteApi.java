package com.fiap.cleanarchitecture.infrastructure.web.docs;

import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarRestauranteDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.RestauranteDTO;
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
 * Contrato documentado (OpenAPI) dos endpoints de restaurantes.
 */
@Tag(name = "Restaurantes", description = "Gerenciamento de restaurantes")
public interface RestauranteApi {

    @Operation(summary = "Cria um restaurante", description = "O dono informado deve ser um usuário do tipo dono de restaurante.")
    @ApiResponse(responseCode = "201", description = "Restaurante criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Dono não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Usuário informado não pode ser dono de restaurante",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<RestauranteDTO> criarRestaurante(CriarRestauranteDTO dto);

    @Operation(summary = "Busca um restaurante pelo id")
    @ApiResponse(responseCode = "200", description = "Restaurante encontrado")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    RestauranteDTO buscarPorId(@Parameter(description = "Id do restaurante", example = "1") Long id);

    @Operation(summary = "Lista todos os restaurantes")
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes")
    List<RestauranteDTO> listarRestaurantes();

    @Operation(summary = "Atualiza um restaurante")
    @ApiResponse(responseCode = "200", description = "Restaurante atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Restaurante ou dono não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Usuário informado não pode ser dono de restaurante",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    RestauranteDTO atualizarRestaurante(@Parameter(description = "Id do restaurante", example = "1") Long id,
                                        CriarRestauranteDTO dto);

    @Operation(summary = "Exclui um restaurante")
    @ApiResponse(responseCode = "204", description = "Restaurante excluído")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    void excluirRestaurante(@Parameter(description = "Id do restaurante", example = "1") Long id);
}

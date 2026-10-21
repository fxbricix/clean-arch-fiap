package com.fiap.cleanarchitecture.infrastructure.web.docs;

import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarTipoUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.TipoUsuarioDTO;
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
 * Contrato documentado (OpenAPI) dos endpoints de tipos de usuário.
 */
@Tag(name = "Tipos de usuário", description = "Gerenciamento dos perfis de usuário (ex.: dono de restaurante, cliente)")
public interface TipoUsuarioApi {

    @Operation(summary = "Cria um tipo de usuário")
    @ApiResponse(responseCode = "201", description = "Tipo de usuário criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Já existe um tipo de usuário com este nome",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<TipoUsuarioDTO> criarTipoUsuario(CriarTipoUsuarioDTO dto);

    @Operation(summary = "Busca um tipo de usuário pelo id")
    @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<TipoUsuarioDTO> obterTipoUsuario(@Parameter(description = "Id do tipo de usuário", example = "1") Long id);

    @Operation(summary = "Lista todos os tipos de usuário")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de usuário")
    ResponseEntity<List<TipoUsuarioDTO>> obterTodosTiposUsuarios();

    @Operation(summary = "Altera o nome de um tipo de usuário")
    @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome já em uso ou tipo de usuário sistêmico não pode ser alterado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<TipoUsuarioDTO> atualizarTipoUsuario(@Parameter(description = "Id do tipo de usuário", example = "1") Long id,
                                                        CriarTipoUsuarioDTO dto);

    @Operation(summary = "Exclui um tipo de usuário")
    @ApiResponse(responseCode = "204", description = "Tipo de usuário excluído")
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Tipos de usuário sistêmicos não podem ser excluídos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> deletarTipoUsuario(@Parameter(description = "Id do tipo de usuário", example = "1") Long id);
}

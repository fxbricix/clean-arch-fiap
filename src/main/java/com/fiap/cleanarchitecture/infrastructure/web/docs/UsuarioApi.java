package com.fiap.cleanarchitecture.infrastructure.web.docs;

import com.fiap.cleanarchitecture.infrastructure.web.dtos.CriarUsuarioDTO;
import com.fiap.cleanarchitecture.infrastructure.web.dtos.UsuarioDTO;
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
 * Contrato documentado (OpenAPI) dos endpoints de usuários.
 */
@Tag(name = "Usuários", description = "Cadastro e consulta de usuários")
public interface UsuarioApi {

    @Operation(summary = "Cria um usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UsuarioDTO> criarUsuario(CriarUsuarioDTO dto);

    @Operation(summary = "Busca um usuário pelo id")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    UsuarioDTO buscarPorId(@Parameter(description = "Id do usuário", example = "1") Long id);

    @Operation(summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista de usuários")
    List<UsuarioDTO> listarUsuarios();

    @Operation(summary = "Associa um tipo a um usuário",
            description = "Troca o tipo do usuário. Um dono de restaurante não pode deixar de ser dono enquanto possuir restaurantes.")
    @ApiResponse(responseCode = "200", description = "Tipo associado")
    @ApiResponse(responseCode = "404", description = "Usuário ou tipo de usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Violação de regra de negócio",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    UsuarioDTO associarTipo(@Parameter(description = "Id do usuário", example = "1") Long usuarioId,
                            @Parameter(description = "Id do tipo de usuário", example = "2") Long tipoUsuarioId);
}

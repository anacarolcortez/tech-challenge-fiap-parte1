package com.fiap.tech_challenge.parte1.ms_users.controllers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.*;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.services.TokenService;
import com.fiap.tech_challenge.parte1.ms_users.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing users.
 * <p>
 * Provides endpoints to create, update, retrieve, activate/deactivate users,
 * and handle authentication-related operations.
 * </p>
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService service;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UsersController(UsersService service, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id UUID of the user to retrieve
     * @return ResponseEntity containing the user data if found
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Busca",
            description = "Retorna os dados de um usuário, pesquisado por ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsersResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<UsersResponseDTO> getById(@PathVariable UUID id) {
        logger.info("/findById -> {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param size number of users per page
     * @param page page index (zero-based)
     * @return ResponseEntity with the list of users
     */
    @GetMapping
    @Operation(
            summary = "Lista usuários",
            description = "Retorna uma lista paginada de usuários",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de usuários",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UsersResponseDTO.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<UsersResponseDTO>> findAllUsers(
            @RequestParam int size,
            @RequestParam int page
    ) {
        logger.info("/findAllUsers -> size: {} ,  offset: {}", size, page);
        var allUsers = this.service.findAllUsers(size, page);
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Creates a new user.
     *
     * @param dto User creation request data validated automatically
     * @return ResponseEntity containing the created user and a generated JWT token
     */
    @PostMapping
    @Operation(
            summary = "Cadastro",
            description = "Registra um novo usuário e retorna dados cadastrais com um token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "timestamp": "2025-05-31T15:00:00Z",
                  "status": 400,
                  "errors": [
                    "User field 'email' must be a valid email address",
                    "User must have at least one Address"
                  ]
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<CreateUserDTO> create(@RequestBody @Valid UsersRequestDTO dto) {
        logger.info("/createUser -> {}", dto);
        UsersResponseDTO user = service.createUser(dto);
        return ResponseEntity.ok(new CreateUserDTO(user, tokenService.generateToken(dto.login())));
    }

    /**
     * Authenticates a user and returns a JWT token if successful.
     *
     * @param data User login credentials validated automatically
     * @return ResponseEntity containing the JWT token
     */
    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Realiza a autenticação de um usuário no sistema e retorna um token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenJWTInfoDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "timestamp": "2025-05-31T15:00:00Z",
                  "status": 400,
                  "errors": [
                    "Field 'login' must not be blank",
                    "Field 'password' must not be blank"
                  ]
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "timestamp": "2025-05-31T15:00:00Z",
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "Invalid login or password"
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<TokenJWTInfoDTO> executeLogin(@RequestBody @Valid AuthenticationDataDTO data) {
        logger.info("/login -> {}", data);
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken(((User) authentication.getPrincipal()).getLogin());
        return ResponseEntity.ok(new TokenJWTInfoDTO(tokenJWT));
    }

    /**
     * Toggles user activation status.
     *
     * @param id       UUID of the user to activate/deactivate
     * @param activate true to activate, false to deactivate
     * @return ResponseEntity with confirmation message
     */
    @PatchMapping("/{id}")
    @Operation(
            summary = "Ativa/Inativa usuário",
            description = "Ativa ou inativa um usuário com base no valor da query param activate.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated / User deactivated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "text/plain",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid UUID or parameter", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<String> toggleActivation(
            @PathVariable UUID id,
            @RequestParam boolean activate
    ) {
        logger.info("/toggleActivation -> id: {}, activate: {}", id, activate);

        if (activate) {
            service.reactivateUser(id);
            return ResponseEntity.ok("User activated!");
        } else {
            service.deactivateUser(id);
            return ResponseEntity.ok("User deactivated!");
        }
    }

    /**
     * Changes the password of a user.
     *
     * @param id  UUID of the user whose password is to be changed
     * @param dto Change password request containing old and new passwords
     * @return ResponseEntity with confirmation message
     */
    @PatchMapping("/{id}/password")
    @Operation(
            summary = "Altera senha",
            description = "Realiza a alteração da senha de um usuário.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully!",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Password updated!"))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "timestamp": "2025-05-31T15:00:00Z",
                  "status": 400,
                  "errors": [
                    "Field 'oldPassword' is required",
                    "Field 'newPassword' is required"
                  ]
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<String> changePassword(
            @PathVariable UUID id,
            @RequestBody @Valid ChangePasswordRequestDTO dto
    ) {
        logger.info("/changePassword -> id: {}", id);
        service.changePassword(id, dto);
        return ResponseEntity.ok("Password updated successfully!");
    }

    /**
     * Updates user details.
     *
     * @param id  UUID of the user to update
     * @param dto Update user request data validated automatically
     * @return ResponseEntity containing the updated user data
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualiza",
            description = "Atualiza os dados cadastrais de um usuario, pesquisado por ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsersResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "timestamp": "2025-05-31T15:00:00Z",
                  "status": 400,
                  "errors": [
                    "User field 'email' must be a valid email address",
                    "User must have at least one address"
                  ]
                }
                """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<UsersResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDTO dto) {
        logger.info("/updateUser -> id: {}, body: {}", id, dto);
        var updatedUser = service.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

}

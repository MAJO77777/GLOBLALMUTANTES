package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Mutant Detector", description = "API para detección de mutantes")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(summary = "Verificar si un ADN es mutante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es mutante"),
            @ApiResponse(responseCode = "403", description = "No es mutante"),
            @ApiResponse(responseCode = "400", description = "ADN inválido",
                    content = @Content(schema = @Schema(implementation = Object.class)))
    })
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());
        return isMutant ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de verificaciones")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}

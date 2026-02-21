package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/blueprints")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) { this.services = services; }


    // GET /api/v1/blueprints
    @Operation(summary = "Get all blueprints")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blueprints retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseLab<Set<Blueprint>>> getAll() {
        Set<Blueprint> data = services.getAllBlueprints();
        return ResponseEntity.ok(
                new ApiResponseLab<>(200, "execute ok", data)
        );
    }

    // GET /api/v1/blueprints/{author}
    @Operation(summary = "Get all blueprints by author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blueprints found"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{author}")
    public ResponseEntity<ApiResponseLab<?>> byAuthor(@PathVariable String author) {
        try {
            var data = services.getBlueprintsByAuthor(author);
            return ResponseEntity.ok(
                    new ApiResponseLab<>(200, "execute ok", data)
            );
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseLab<>(404, e.getMessage(), null));
        }
    }

    // GET /api/v1/blueprints/{author}/{bpname}
    @Operation(summary = "Get blueprint by author and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blueprint found"),
            @ApiResponse(responseCode = "404", description = "Blueprint not found")
    })
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<ApiResponseLab<?>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            var data = services.getBlueprintsByAuthor(author);
            return ResponseEntity.ok(
                    new ApiResponseLab<>(200, "execute ok", services)
            );
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseLab<>(404, e.getMessage(), null));
        }
    }

    // POST /api/v1/blueprints
    @Operation(summary = "Create a new blueprint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Blueprint created"),
            @ApiResponse(responseCode = "400", description = "Invalid blueprint data")
    })
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseLab<>(201, "created", bp));
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseLab<>(400, e.getMessage(), null));
        }
    }

    // PUT /api/v1/blueprints/{author}/{bpname}/points
    @Operation(summary = "Add a point to an existing blueprint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Point added"),
            @ApiResponse(responseCode = "404", description = "Blueprint not found")
    })
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<?> addPoint(@PathVariable String author, @PathVariable String bpname,
                                      @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ApiResponseLab<>(202, "point added", null));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseLab<>(404, e.getMessage(), null));
        }
    }

    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }
}

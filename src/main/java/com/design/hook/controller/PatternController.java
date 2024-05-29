package com.design.hook.controller;

import com.design.hook.dto.PatternDTO;
import com.design.hook.service.PatternService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/patterns")
@RestController
public class PatternController {

    private final PatternService patternService;

    @Autowired
    public PatternController(final PatternService patternService) {
        this.patternService = patternService;
    }

    @Operation(summary = "Retrieves all patterns")
    @GetMapping
    public ResponseEntity<Collection<PatternDTO>> getPatterns() {

        return new ResponseEntity<>(patternService.getAllPatterns(), HttpStatus.OK);
    }

    @Operation(summary = "Adds a new pattern")
    @PostMapping
    public ResponseEntity<PatternDTO> addPattern(@RequestBody final PatternDTO patternDTO) {

        return new ResponseEntity<>(patternService.addPattern(patternDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieves a pattern")
    @GetMapping("/{id}")
    public ResponseEntity<PatternDTO> getPattern(@PathVariable("id") final long patternId) {

        return new ResponseEntity<>(patternService.getPattern(patternId), HttpStatus.OK);
    }

    @Operation(summary = "updates a pattern")
    @PutMapping("/{id}")
    public ResponseEntity<PatternDTO> updatePattern(@PathVariable("id") final long patternId,
                                                    @RequestParam("isLike") final String isLike) {

        return new ResponseEntity<>(patternService.updatePattern(patternId, isLike), HttpStatus.OK);
    }
}

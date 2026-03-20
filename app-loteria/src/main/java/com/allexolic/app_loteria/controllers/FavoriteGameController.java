package com.allexolic.app_loteria.controllers;

import com.allexolic.app_loteria.services.FavoriteGameService;
import com.allexolic.app_loteria.services.dto.FavoriteGameCreateRequest;
import com.allexolic.app_loteria.services.dto.FavoriteGameResponse;
import com.allexolic.app_loteria.services.dto.RecalculateGameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jogos")
@RequiredArgsConstructor
public class FavoriteGameController {
    private final FavoriteGameService service;

    @PostMapping("/favoritos")
    public ResponseEntity<FavoriteGameResponse> create(@RequestBody FavoriteGameCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PostMapping("/recalcular")
    public ResponseEntity<FavoriteGameResponse> recalculate(@RequestBody RecalculateGameRequest req) {
        if(req.gameId() == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.recalculate(req.gameId()));
    }

    @GetMapping("/favoritos")
    public ResponseEntity<List<FavoriteGameResponse>> listGames() {
        return ResponseEntity.ok(service.listGames());
    }
}

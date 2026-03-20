package com.allexolic.app_loteria.services.dto;

import java.util.List;

public record FavoriteGameResponse(Long id, List<Integer> balls, boolean awardWinningGame, int totalNumbersAwardsWinning) {
}

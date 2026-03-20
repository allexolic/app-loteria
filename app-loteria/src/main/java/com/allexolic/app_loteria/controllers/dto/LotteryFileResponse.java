package com.allexolic.app_loteria.controllers.dto;

import java.time.Instant;

public record LotteryFileResponse(Long id, String name, Instant insertDate) {
}

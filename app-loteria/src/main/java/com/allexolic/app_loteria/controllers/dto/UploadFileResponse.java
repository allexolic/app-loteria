package com.allexolic.app_loteria.controllers.dto;

import java.time.Instant;

public record UploadFileResponse(Long fileId, String fileName, long linesRead, long importedRecords, long ignoredLines, Instant uploadDate) {
}

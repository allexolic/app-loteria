package com.allexolic.app_loteria.controllers;

import com.allexolic.app_loteria.controllers.dto.LotteryFileResponse;
import com.allexolic.app_loteria.controllers.dto.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.allexolic.app_loteria.services.LotteryFileUploadService;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/loteria")
@RequiredArgsConstructor
public class LotteryFileController {
    private final LotteryFileUploadService service;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadFileResponse> upload(@RequestPart("file") MultipartFile file) throws Exception {
        if(file.isEmpty())
            return ResponseEntity.badRequest().build();
        val result = service.upload(file);
        val response = new UploadFileResponse(
                result.fileId(),
                result.fileName(),
                result.linesRead(),
                result.importedRecords(),
                result.ignoredLines(),
                Instant.now()
        );
        URI location = URI.create("/api/arquivos/" + response.fileId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping(path = "/arquivos")
    public ResponseEntity<List<LotteryFileResponse>> listFiles() {
        return ResponseEntity.ok(service.getFiles());
    }
}

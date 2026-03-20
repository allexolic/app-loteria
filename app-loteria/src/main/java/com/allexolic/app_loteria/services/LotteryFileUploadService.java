package com.allexolic.app_loteria.services;

import com.allexolic.app_loteria.controllers.dto.LotteryFileResponse;
import com.allexolic.app_loteria.entities.LotteryFile;
import com.allexolic.app_loteria.entities.LotteryFileDetail;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.allexolic.app_loteria.repositories.LotteryFileDetailsRepository;
import com.allexolic.app_loteria.repositories.LotteryFileRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LotteryFileUploadService {
    private final LotteryFileRepository lotteryFileRepository;
    private final LotteryFileDetailsRepository lotteryFileDetailsRepository;

    public List<LotteryFileResponse> getFiles() {
        return lotteryFileRepository.findAll().stream().map(this::toResponse).toList();
    }

    private LotteryFileResponse toResponse(LotteryFile lotteryFile) {
        return new LotteryFileResponse(
                lotteryFile.getId(),
                lotteryFile.getNome(),
                lotteryFile.getDataCadastro()
        );
    }

    @Transactional
    public UploadResult upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não encontrado");
        }
        val newFile = new LotteryFile();
        newFile.setNome(Optional.ofNullable(file.getOriginalFilename()).orElse("upload"));
        val savedFile = lotteryFileRepository.save(newFile);

        long linesRead = 0;
        long importedLines = 0;
        long ignoredLines = 0;

        try (InputStream raw = file.getInputStream();
            Workbook wb = new XSSFWorkbook(raw)) {
            Sheet sheet = wb.getNumberOfSheets() > 0 ? wb.getSheetAt(0) : null;

            if (sheet == null)
                throw new IllegalArgumentException("Planilha vazia.");
            Row header = sheet.getRow(sheet.getFirstRowNum());
            if (header == null)
                throw new IllegalArgumentException("Arquivo sem cabeçalho.");

            Map<String, Integer> col = mapHeader(header);
            require(col, "concurso");
            require(col, "data sorteio");

            for (int i = 1; i <= 15; i++)
                require(col, "bola" + i);

            List<LotteryFileDetail> buffer = new ArrayList<>(2000);

            for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) { ignoredLines++; continue; }
                linesRead++;

                try {
                    Integer concurso = readInt(row, col.get("concurso"));
                    LocalDate dataSorteio = readDate(row, col.get("data sorteio"));

                    Integer[] bolas = new Integer[15];
                    for (int i = 1; i <= 15; i++) {
                        bolas[i - 1] = readInt(row, col.get("bola" + i));
                    }

                    val items = new LotteryFileDetail();
                    items.setArquivo(savedFile);
                    items.setConcurso(concurso);
                    items.setDataSorteio(dataSorteio);

                    items.setBola1(bolas[0]);
                    items.setBola2(bolas[1]);
                    items.setBola3(bolas[2]);
                    items.setBola4(bolas[3]);
                    items.setBola5(bolas[4]);
                    items.setBola6(bolas[5]);
                    items.setBola7(bolas[6]);
                    items.setBola8(bolas[7]);
                    items.setBola9(bolas[8]);
                    items.setBola10(bolas[9]);
                    items.setBola11(bolas[10]);
                    items.setBola12(bolas[11]);
                    items.setBola13(bolas[12]);
                    items.setBola14(bolas[13]);
                    items.setBola15(bolas[14]);

                    buffer.add(items);

                    if (buffer.size() >= 2000) {
                        lotteryFileDetailsRepository.saveAll(buffer);
                        importedLines += buffer.size();
                        buffer.clear();
                    }
                } catch (Exception e) {
                    ignoredLines++;
                }
            }
            if (!buffer.isEmpty()) {
                lotteryFileDetailsRepository.saveAll(buffer);
                importedLines += buffer.size();
            }
        }
        return new UploadResult(savedFile.getId(), savedFile.getNome(), linesRead, importedLines, ignoredLines);
    }

    private Map<String, Integer> mapHeader(Row header) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell c : header) {
            String key = normalize(readStringCell(c));
            if (!key.isBlank()) map.put(key, c.getColumnIndex());
        }
        if (map.containsKey("data_sorteio") && !map.containsKey("data sorteio")) {
            map.put("data sorteio", map.get("data_sorteio"));
        }
        if (map.containsKey("data do sorteio") && !map.containsKey("data sorteio")) {
            map.put("data sorteio", map.get("data do sorteio"));
        }
        return map;
    }

    private void require(Map<String, Integer> col, String name) {
        if (!col.containsKey(name)) {
            throw new IllegalArgumentException("Coluna obrigatória não encontrada no XLSX: " + name);
        }
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    private String readStringCell(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Integer readInt(Row row, int colIdx) {
        Cell cell = row.getCell(colIdx);
        if (cell == null) throw new IllegalArgumentException("Célula vazia.");
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        String s = cell.toString().trim();
        if (s.isBlank()) throw new IllegalArgumentException("Número vazio.");
        if (s.endsWith(".0")) s = s.substring(0, s.length() - 2);
        return Integer.parseInt(s);
    }

    private LocalDate readDate(Row row, int colIdx) {
        Cell cell = row.getCell(colIdx);
        if (cell == null) throw new IllegalArgumentException("Data vazia.");

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        String raw = cell.toString().trim().replace("-", "/");

        if (raw.matches("\\d{2}/\\d{2}/\\d{4}")) {
            int d = Integer.parseInt(raw.substring(0, 2));
            int m = Integer.parseInt(raw.substring(3, 5));
            int y = Integer.parseInt(raw.substring(6, 10));
            return LocalDate.of(y, m, d);
        }
        throw new IllegalArgumentException("Invalid date format: " + raw);
    }

    public record UploadResult(Long fileId, String fileName, long linesRead, long importedRecords, long ignoredLines) {}
}

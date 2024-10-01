package com.mfa.report.endpoint.rest.controller;


import com.mfa.report.endpoint.rest.mapper.MissionMapper;
import com.mfa.report.model.Mission;
import com.mfa.report.service.MissionService;
import com.mfa.report.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/direction/")
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;
    private final MissionService missionService;
    private final MissionMapper missionMapper;

    @PostMapping("/mission/export/pdf")
    public ResponseEntity<byte[]> generateMissionPdf( @RequestBody List<String> missionIds) {
        log.info(missionIds.toString());
        List<Mission> missions = missionService.findMissionsByIds(missionIds);
        if (missions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Step 2: Generate the PDF
        byte[] pdfBytes = fileService.createMissionReport(missions);

        // Step 3: Set HTTP Headers and return PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "mission_report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/mission/export/excel")
    public ResponseEntity<ByteArrayResource> generateExcel(@RequestParam List<String> id) throws IOException {
        ByteArrayResource resource = fileService.createExcelFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=missions.xlsx")
                .contentLength(resource.contentLength())
                .body(resource);
    }
    }

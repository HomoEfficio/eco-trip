package io.homo_efficio.ecotrip.api.admin.controller;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import io.homo_efficio.ecotrip.api.admin.service.EcoProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
@RestController
@RequestMapping("/admin/eco-programs")
@RequiredArgsConstructor
public class EcoProgramController {

    private final EcoProgramService ecoProgramService;

    @PostMapping("/upload-programs-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir") +
                System.getProperty("file.separator") +
                "tmp-" + file.getOriginalFilename());
        file.transferTo(filePath);
        List<String> lines = Files.readAllLines(filePath);
        return ResponseEntity.ok(lines.get(0));
    }

    @PostMapping
    public ResponseEntity<EcoProgramDto> createEcoProgram(@Valid @RequestBody EcoProgramParam ecoProgramParam) {
        return ResponseEntity.ok(ecoProgramService.save(ecoProgramParam));
    }
}

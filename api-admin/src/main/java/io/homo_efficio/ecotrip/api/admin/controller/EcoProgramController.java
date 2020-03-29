package io.homo_efficio.ecotrip.api.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class EcoProgramController {

    @PostMapping("/upload-programs-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir") +
                System.getProperty("file.separator") +
                "tmp-" + file.getOriginalFilename());
        file.transferTo(filePath);
        List<String> lines = Files.readAllLines(filePath);
        return ResponseEntity.ok(lines.get(0));
    }
}

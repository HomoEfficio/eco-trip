package io.homo_efficio.ecotrip.api.admin.controller;

import io.homo_efficio.ecotrip.api.admin.dto.EcoProgramDto;
import io.homo_efficio.ecotrip.api.admin.dto.KeywordAndFrquencyByKeywordDto;
import io.homo_efficio.ecotrip.api.admin.dto.NameAndThemesByRegionDto;
import io.homo_efficio.ecotrip.api.admin.dto.RegionAndCountsByKeyword;
import io.homo_efficio.ecotrip.api.admin.param.EcoProgramParam;
import io.homo_efficio.ecotrip.api.admin.param.KeywordParam;
import io.homo_efficio.ecotrip.api.admin.param.RegionNameParam;
import io.homo_efficio.ecotrip.api.admin.service.EcoProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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
    public ResponseEntity<List<EcoProgramDto>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir") +
                System.getProperty("file.separator") +
                "tmp-" + file.getOriginalFilename());
        file.transferTo(filePath);

        return ResponseEntity.ok(ecoProgramService.loadEcoProgramsFromPath(filePath));
    }

    @PostMapping
    public ResponseEntity<EcoProgramDto> createEcoProgram(@Valid @RequestBody EcoProgramParam ecoProgramParam) {
        return ResponseEntity.ok(ecoProgramService.save(ecoProgramParam));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EcoProgramDto> updateEcoProgram(@PathVariable Long id,
                                                          @Valid @RequestBody EcoProgramParam ecoProgramParam) {
        return ResponseEntity.ok(ecoProgramService.save(ecoProgramParam));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EcoProgramDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ecoProgramService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EcoProgramDto>> findByRegion(@RequestParam("regionCode") Long regionCode) {
        return ResponseEntity.ok(ecoProgramService.findByRegion(regionCode));
    }

    @GetMapping("/by-region-name")
    public ResponseEntity<NameAndThemesByRegionDto> findProgramsByRegion(@RequestBody RegionNameParam param) {
        return ResponseEntity.ok(ecoProgramService.findByRegion(param));
    }

    @GetMapping("/by-desc-keyword")
    public ResponseEntity<RegionAndCountsByKeyword> findRegionAndCountsByDescKeyword(@RequestBody KeywordParam param) {
        return ResponseEntity.ok(ecoProgramService.findByDescKeyword(param));
    }

    @GetMapping("/by-detail-keyword")
    public ResponseEntity<KeywordAndFrquencyByKeywordDto> findKeywordAndCountsByDetailKeyword(@RequestBody KeywordParam param) {
        return ResponseEntity.ok(ecoProgramService.findWordCountsByDetailKeyword(param));
    }
}

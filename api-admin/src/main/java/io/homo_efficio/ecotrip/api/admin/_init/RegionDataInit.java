package io.homo_efficio.ecotrip.api.admin._init;

import io.homo_efficio.ecotrip.domain.entity.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RegionDataInit implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        File regionFile = new ClassPathResource("data/region").getFile();
        List<String> lines = Files.readAllLines(regionFile.toPath(), StandardCharsets.UTF_8);
        List<Region> regions = new ArrayList<>();
        for (String region : lines) {
            regions.add(new Region(null, region));
        }
        jdbcTemplate.batchUpdate("INSERT INTO region (`region_name`) values (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, regions.get(i).getName());
                    }

                    @Override
                    public int getBatchSize() {
                        return regions.size();
                    }
                });
    }
}

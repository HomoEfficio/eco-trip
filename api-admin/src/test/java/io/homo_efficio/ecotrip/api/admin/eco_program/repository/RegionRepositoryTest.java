package io.homo_efficio.ecotrip.api.admin.eco_program.repository;

import io.homo_efficio.ecotrip.domain.eco_program.entity.Region;
import io.homo_efficio.ecotrip.domain.eco_program.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
@DataJpaTest
public class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @DisplayName("새 서비스 지역 등록")
    @Test
    public void create_new_service_region() {

        Region region = new Region("서울특별시");

        Region dbRegion = regionRepository.save(region);

        assertThat(dbRegion.getId()).isNotNull();
        assertThat(dbRegion.getName()).isEqualTo("서울특별시");
    }
}

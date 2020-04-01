package io.homo_efficio.ecotrip.api.admin.eco_program.repository;

import io.homo_efficio.ecotrip.domain.eco_program.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.eco_program.entity.Region;
import io.homo_efficio.ecotrip.domain.eco_program.repository.EcoProgramRepository;
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
public class EcoProgramRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private EcoProgramRepository ecoProgramRepository;


    @DisplayName("새 프로그램 등록")
    @Test
    public void create_new_eco_program() {
        // given
        Region region = regionRepository.save(new Region("서울"));
        EcoProgram ecoProgram = new EcoProgram(null, "테스트 프로그램", "테스트 테마", region, null, null);

        // when
        EcoProgram dbEcoProgram = ecoProgramRepository.save(ecoProgram);

        // then
        assertThat(dbEcoProgram.getId()).isNotNull();
        assertThat(dbEcoProgram.getName()).isEqualTo("테스트 프로그램");
        assertThat(dbEcoProgram.getTheme()).isEqualTo("테스트 테마");
        assertThat(dbEcoProgram.getRegion().getName()).isEqualTo("서울");
    }
}

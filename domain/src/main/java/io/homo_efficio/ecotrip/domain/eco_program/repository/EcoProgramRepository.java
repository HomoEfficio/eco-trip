package io.homo_efficio.ecotrip.domain.eco_program.repository;

import io.homo_efficio.ecotrip.domain.eco_program.entity.EcoProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
public interface EcoProgramRepository extends JpaRepository<EcoProgram, Long>, EcoProgramRepositoryCustom {

    List<EcoProgram> findAllByRegion_Id(Long regionCode);
}

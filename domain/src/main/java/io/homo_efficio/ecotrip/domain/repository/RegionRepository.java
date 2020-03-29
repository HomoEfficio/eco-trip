package io.homo_efficio.ecotrip.domain.repository;

import io.homo_efficio.ecotrip.domain.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findAllByNameContaining(String name);
}

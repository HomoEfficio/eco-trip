package io.homo_efficio.ecotrip.domain.eco_program.repository;

import com.querydsl.core.types.Projections;
import io.homo_efficio.ecotrip.domain.eco_program.entity.EcoProgram;
import io.homo_efficio.ecotrip.domain.eco_program.projection.RegionAndCountPrj;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static io.homo_efficio.ecotrip.domain.eco_program.entity.QEcoProgram.ecoProgram;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-01
 */
public class EcoProgramRepositoryImpl extends QuerydslRepositorySupport implements EcoProgramRepositoryCustom {

    public EcoProgramRepositoryImpl() {
        super(EcoProgram.class);
    }

    @Override
    public List<RegionAndCountPrj> findRegionAndCountsByDescKeyword(String keyword) {
        return from(ecoProgram)
                .where(
                        ecoProgram.description.contains(keyword)
                )
                .groupBy(ecoProgram.region)
                .select(Projections.constructor(RegionAndCountPrj.class,
                        ecoProgram.region.name, ecoProgram.id.count()))
                .fetch();

    }

    @Override
    public List<String> findKeywordAndCountsByDetailKeyword(String keyword) {
        return from(ecoProgram)
                .where(
                        ecoProgram.detail.contains(keyword)
                )
                .select(ecoProgram.detail)
                .fetch();

    }
}

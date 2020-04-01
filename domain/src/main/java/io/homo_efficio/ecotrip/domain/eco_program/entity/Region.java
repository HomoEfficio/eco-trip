package io.homo_efficio.ecotrip.domain.eco_program.entity;

import io.homo_efficio.ecotrip.domain._common.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
@Entity
@Table(name = "region")
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_code")
    private Long id;

    @Column(name = "region_name")
    private final String name;

    protected Region() {
        this.name = null;
    }
}

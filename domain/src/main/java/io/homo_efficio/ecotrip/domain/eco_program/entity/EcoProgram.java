package io.homo_efficio.ecotrip.domain.eco_program.entity;

import io.homo_efficio.ecotrip.domain._common.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
@Entity
@Table(name = "eco_prgm")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
public class EcoProgram extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prgm_name")
    @Setter
    private String name;

    @Column(name = "theme")
    @Setter
    private String theme;

    @OneToOne
    @JoinColumn(name = "region_code")
    @Setter
    private Region region;

    @Column(name = "prgm_desc")
    @Setter
    private String description;

    @Lob
    @Column(name = "prgm_detail")
    @Setter
    private String detail;
}

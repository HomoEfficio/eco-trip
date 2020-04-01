package io.homo_efficio.ecotrip.domain.member.entity;

import io.homo_efficio.ecotrip.domain._common.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;
}

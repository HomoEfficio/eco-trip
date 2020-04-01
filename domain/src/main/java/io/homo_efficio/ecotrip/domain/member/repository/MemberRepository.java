package io.homo_efficio.ecotrip.domain.member.repository;

import io.homo_efficio.ecotrip.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-04-02
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}

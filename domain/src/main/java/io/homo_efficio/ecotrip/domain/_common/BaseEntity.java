package io.homo_efficio.ecotrip.domain._common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-29
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    protected LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    protected LocalDateTime lastModifiedDateTime;
}

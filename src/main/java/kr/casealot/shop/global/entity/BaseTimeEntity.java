package kr.casealot.shop.global.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @Column(name = "CREATED_DT")
    @CreatedDate
    private LocalDateTime createdDt;

    @Column(name = "MODIFIED_DT")
    @LastModifiedDate
    private LocalDateTime modifiedDt;

    @PrePersist
    public void onPrePersist() {
        this.createdDt = LocalDateTime.now();
        this.modifiedDt = this.createdDt;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDt = LocalDateTime.now();
    }

}

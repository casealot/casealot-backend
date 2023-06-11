package kr.casealot.shop.global.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(name = "CREATED_DT", updatable = false)
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDt;

    @Column(name = "MODIFIED_DT")
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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

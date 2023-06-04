package kr.casealot.shop.domain.file.entity;

import kr.casealot.shop.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UploadFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fileType;

    @URL
    @Column(nullable = false)
    private String url;

    @Column
    private Long fileSize;

    @Builder
    public UploadFile(String name, String fileType, String url, Long fileSize) {
        this.name = name;
        this.fileType = fileType;
        this.url = url;
        this.fileSize = fileSize;
    }

}

package kr.casealot.shop.domain.file.repository;

import kr.casealot.shop.domain.file.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UploadFileRepository extends JpaRepository<UploadFile, UUID> {
    void deleteByUuid(String uuid);
}

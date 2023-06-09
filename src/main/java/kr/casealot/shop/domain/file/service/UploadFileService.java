package kr.casealot.shop.domain.file.service;


import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public UploadFile create(UploadFile uploadFile) throws Exception{
        return uploadFileRepository.save(uploadFile);
    }

    @Transactional
    public void delete(UploadFile uploadFile) {
        uploadFileRepository.deleteByUuid(uploadFile.getUuid());
    }
}

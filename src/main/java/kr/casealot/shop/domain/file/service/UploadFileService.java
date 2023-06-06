package kr.casealot.shop.domain.file.service;


import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public UploadFile create(UploadFile uploadFile) throws Exception{
        return uploadFileRepository.save(uploadFile);
    }

}

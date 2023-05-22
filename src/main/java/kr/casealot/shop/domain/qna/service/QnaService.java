package kr.casealot.shop.domain.qna.service;

import kr.casealot.shop.domain.qna.dto.QnaDTO;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;

    public Qna createQna(QnaDTO qnaDTO){
        Qna qna = Qna.builder()
                .title(qnaDTO.getTitle())
                .content(qnaDTO.getContent())
                .photoUrl(qnaDTO.getPhotoUrl())
                .registrationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .build();
        return qnaRepository.save(qna);
    }

    public void updateQna(Long qnaId, QnaDTO qnaDto) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);

        qna = Qna.builder()
                .id(qna.getId())
                .title(qnaDto.getTitle())
                .content(qnaDto.getContent())
                .photoUrl(qnaDto.getPhotoUrl())
                .modificationDate(LocalDateTime.now())
                .build();

        qnaRepository.save(qna);
    }

    public void deleteQna(Long qnaId) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);

        qnaRepository.delete(qna);
    }

    //다시생각해보자
    public Page<Qna> getQnaList(Pageable pageable) {
        return qnaRepository.findAll(pageable);
    }
}

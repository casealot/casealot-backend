package kr.casealot.shop.domain.qna.service;

import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.dto.QnaDTO;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaCommentRepository qnaCommentRepository;

//     qna 등록
//    @Transactional
//    public Qna createQna(CreateQna qnaDTO){
//        Qna qna = Qna.builder()
//                .title(qnaDTO.getTitle())
//                .content(qnaDTO.getContent())
//                .photoUrl(qnaDTO.getPhotoUrl())
//                .registrationDate(LocalDateTime.now())
//                .modificationDate(LocalDateTime.now())
//                .build();
//        return qnaRepository.save(qna);
//    }


//    @Transactional
//    public QnaDTO createQna(String title, String content, String photoUrl) {
//        return QnaDTO.fromEntity(qnaRepository.save(
//                Qna.builder()
//                        .title(title)
//                        .content(content)
//                        .photoUrl(photoUrl)
//                        .registrationDate(LocalDateTime.now())
//                        .modificationDate(LocalDateTime.now())
//                        .build())
//        );
//    }

    @Transactional
    public QnaDTO createQna(QnaDTO qnaDTO) {
        Qna qna = Qna.builder()
                .title(qnaDTO.getTitle())
                .content(qnaDTO.getContent())
                .photoUrl(qnaDTO.getPhotoUrl())
                .registrationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .build();

        qnaRepository.save(qna);

        return QnaDTO.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .photoUrl(qna.getPhotoUrl())
                .registrationDate(qna.getRegistrationDate())
                .modificationDate(qna.getModificationDate())
                .build();
    }


    // qna 수정
    @Transactional
    public QnaDTO updateQna(Long qnaId, QnaDTO qnaDTO) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);

        qna.setTitle(qnaDTO.getTitle());
        qna.setContent(qnaDTO.getContent());
        qna.setPhotoUrl(qnaDTO.getPhotoUrl());
        qna.setModificationDate(LocalDateTime.now());

        qnaRepository.save(qna);

        return QnaDTO.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .photoUrl(qna.getPhotoUrl())
                .views(qna.getViews())
                .registrationDate(qna.getRegistrationDate())
                .modificationDate(qna.getModificationDate())
                .build();

    }

    // qna 조회
    @Transactional
    public Qna getQna(Long qnaId) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        qna.setViews(qna.getViews() + 1);
        List<QnaComment> commentList = qnaCommentRepository.findByQnaId(qnaId);
        qna.setQnaCommentList(commentList);
        qnaRepository.save(qna);
        return qna;
    }

    // qna 삭제
    @Transactional
    public void deleteQna(Long qnaId) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        qnaRepository.delete(qna);
    }

    // qna 목록
    @Transactional
    public List<QnaDTO> getQnaList(Pageable pageable) {
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        List<Qna> qnaList = qnaPage.getContent();

        List<QnaDTO> qnaDtoList = new ArrayList<>();
        for (Qna qna : qnaList) {
            QnaDTO qnaDto = new QnaDTO();
            qnaDto.setId(qna.getId());
            qnaDto.setTitle(qna.getTitle());
            qnaDto.setContent(qna.getContent());
            qnaDto.setPhotoUrl(qna.getPhotoUrl());
            qnaDto.setViews(qna.getViews());
            qnaDto.setRegistrationDate(qna.getRegistrationDate());
            qnaDto.setModificationDate(qna.getModificationDate());
            qnaDtoList.add(qnaDto);
        }

        return qnaDtoList;
    }
}

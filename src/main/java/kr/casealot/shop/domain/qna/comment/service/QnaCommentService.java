package kr.casealot.shop.domain.qna.comment.service;

import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final QnaCommentRepository qnaCommentRepository;
    private final QnaRepository qnaRepository;

    public QnaComment createQnaComment(Long qnaId, QnaCommentDTO qnaCommentDto) throws ChangeSetPersister.NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(ChangeSetPersister.NotFoundException::new);

        QnaComment qnaComment = QnaComment.builder()
                .qna(qna)
                .title(qnaCommentDto.getTitle())
                .content(qnaCommentDto.getContent())
                .registrationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .build();

        return qnaCommentRepository.save(qnaComment);
    }
}
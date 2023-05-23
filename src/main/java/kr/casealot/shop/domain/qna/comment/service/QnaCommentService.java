package kr.casealot.shop.domain.qna.comment.service;

import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.data.crossstore.ChangeSetPersister.*;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final QnaCommentRepository qnaCommentRepository;
    private final QnaRepository qnaRepository;

    public QnaComment createQnaComment(Long qnaId, QnaCommentDTO qnaCommentDto) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);

        QnaComment qnaComment = QnaComment.builder()
                .qna(qna)
                .title(qnaCommentDto.getTitle())
                .content(qnaCommentDto.getContent())
                .registrationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .build();

        return qnaCommentRepository.save(qnaComment);
    }

    public void deleteComment(Long qnaId, Long commentId) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        qna.getQnaCommentList().remove(qnaComment);
        qnaCommentRepository.delete(qnaComment);
    }

    public void updateComment(Long qnaId, Long commentId, QnaCommentDTO qnaCommentDTO) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        qnaComment.setTitle(qnaCommentDTO.getTitle());
        qnaComment.setContent(qnaCommentDTO.getContent());
        qnaComment.setModificationDate(LocalDateTime.now());
        qnaCommentRepository.save(qnaComment);
    }
}
package kr.casealot.shop.domain.qna.comment.controller;

import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.service.QnaCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.crossstore.ChangeSetPersister.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1")
public class QnaCommentController {

    private final QnaCommentService qnaCommentService;

    @PostMapping("/qna/{qna_id}/comments")
    public ResponseEntity<QnaComment> createQnaComment(
            @PathVariable Long qna_id,
            @RequestBody QnaCommentDTO qnaCommentDto) throws NotFoundException {

        QnaComment qnaComment = qnaCommentService.createQnaComment(qna_id, qnaCommentDto);
        return ResponseEntity.status(CREATED).body(qnaComment);
    }

    // 댓글 삭제
    @DeleteMapping("/qna/{qna_id}/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("qna_id")Long qnaId,
            @PathVariable("comment_id") Long commentId
    ) throws NotFoundException {
        qnaCommentService.deleteComment(qnaId, commentId);

        return ResponseEntity.ok("댓글 삭제 완료");
    }
    // 댓글 수정
    @PutMapping("/qna/{qna_id}/comments/{comment_id}")
    public ResponseEntity<String> updateComment(
            @PathVariable("qna_id")Long qnaId,
            @PathVariable("comment_id") Long commentId,
            @RequestBody QnaCommentDTO qnaCommentDTO
    ) throws NotFoundException {
        qnaCommentService.updateComment(qnaId, commentId, qnaCommentDTO);
        return ResponseEntity.ok("댓글 수정 완료");
    }
}

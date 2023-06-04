package kr.casealot.shop.domain.qna.comment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.service.QnaCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"QNA COMMENT API"}, description = "QNA COMMENT 관련 API")
@RequestMapping("/cal/v1")
public class QnaCommentController {

    private final QnaCommentService qnaCommentService;

    @PostMapping("/qna/{qna_id}/comments")
    public ResponseEntity<String> createQnaComment(
            @PathVariable Long qna_id,
            @RequestBody QnaCommentDTO qnaCommentDto, HttpServletRequest request
    ) throws NotFoundException {

        qnaCommentService.createQnaComment(qna_id, qnaCommentDto, request);

        return ResponseEntity.ok("create comment");
    }

    // 댓글 삭제
    @DeleteMapping("/qna/{qna_id}/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("qna_id")Long qnaId,
            @PathVariable("comment_id") Long commentId,
            HttpServletRequest request
    ) throws NotFoundException {

        qnaCommentService.deleteComment(qnaId, commentId, request);

        return ResponseEntity.ok("delete comment");
    }
    // 댓글 수정
    @PutMapping("/qna/{qna_id}/comments/{comment_id}")
    public ResponseEntity<String> updateComment(
            @PathVariable("qna_id")Long qnaId,
            @PathVariable("comment_id") Long commentId,
            @RequestBody QnaCommentDTO qnaCommentDTO,
            HttpServletRequest request
    ) throws NotFoundException {

        qnaCommentService.updateComment(qnaId, commentId, qnaCommentDTO, request);

        return ResponseEntity.ok("update comment");
    }
}

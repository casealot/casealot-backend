package kr.casealot.shop.domain.qna.comment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.service.QnaCommentService;
import kr.casealot.shop.global.common.APIResponse;
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
    public APIResponse<Void> createQnaComment(
            @PathVariable Long qna_id,
            @RequestBody QnaCommentDTO qnaCommentDto, HttpServletRequest request){

        return qnaCommentService.createQnaComment(qna_id, qnaCommentDto, request);
    }

    // 댓글 삭제
    @DeleteMapping("/qna/{qna_id}/comments/{comment_id}")
    public APIResponse<Void> deleteComment(
            @PathVariable("qna_id")Long qnaId,
            @PathVariable("comment_id") Long commentId,
            HttpServletRequest request){

        return qnaCommentService.deleteComment(qnaId, commentId, request);
    }
    // 댓글 수정
    @PutMapping("/qna/{qna_id}/comments/{comment_id}")
    public APIResponse<Void> updateComment(
            @PathVariable("qna_id")Long qnaId,
            @PathVariable("comment_id") Long commentId,
            @RequestBody QnaCommentDTO qnaCommentDTO,
            HttpServletRequest request ){

        return  qnaCommentService.updateComment(qnaId, commentId, qnaCommentDTO, request);
    }
}

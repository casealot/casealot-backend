package kr.casealot.shop.domain.qna.comment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentReqDTO;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentResDTO;
import kr.casealot.shop.domain.qna.comment.service.QnaCommentService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"QNA COMMENT API"}, description = "QNA COMMENT 관련 API")
@RequestMapping("/cal/v1/admin/qna")
public class QnaCommentController {

    private final QnaCommentService qnaCommentService;

    @PostMapping("/{qna_id}")
    public APIResponse<QnaCommentResDTO> createQnaComment(@PathVariable Long qna_id,
                                                          @RequestBody QnaCommentReqDTO qnaCommentReqDTO,
                                                          HttpServletRequest request,
                                                          Principal principal){

        return qnaCommentService.createQnaComment(qna_id, qnaCommentReqDTO, request, principal);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment_id}")
    public APIResponse<QnaCommentResDTO> deleteComment(@PathVariable("comment_id") Long commentId,
                                                       HttpServletRequest request,
                                                       Principal principal){

        return qnaCommentService.deleteComment(commentId, request, principal);
    }
    // 댓글 수정
    @PutMapping("/{comment_id}")
    public APIResponse<QnaCommentResDTO> updateComment( @PathVariable("comment_id") Long commentId,
                                                        @RequestBody QnaCommentReqDTO qnaCommentReqDTO,
                                                        HttpServletRequest request,
                                                        Principal principal){

        return  qnaCommentService.updateComment(commentId, qnaCommentReqDTO, request, principal);
    }
}

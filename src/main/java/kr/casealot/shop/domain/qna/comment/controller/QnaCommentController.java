package kr.casealot.shop.domain.qna.comment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
  @ApiOperation(value = "QNA 댓글 작성", notes = "QNA에 대한 댓글을 작성한다.")
  public APIResponse<QnaCommentResDTO> createQnaComment(
      @ApiParam(value = "QNA ID") @PathVariable Long qna_id,
      @ApiParam(value = "QNA 댓글 작성 DTO") @RequestBody QnaCommentReqDTO qnaCommentReqDTO,
      HttpServletRequest request,
      Principal principal) {

    return qnaCommentService.createQnaComment(qna_id, qnaCommentReqDTO, request, principal);
  }

  // 댓글 삭제
  @DeleteMapping("/{comment_id}")
  @ApiOperation(value = "QNA 댓글 삭제", notes = "QNA에 댓글을 단 사용자가 자신의 댓글을 삭제한다.")
  public APIResponse<QnaCommentResDTO> deleteComment(
      @ApiParam(value = "QNA 댓글 ID") @PathVariable("comment_id") Long commentId,
      HttpServletRequest request,
      Principal principal) {

    return qnaCommentService.deleteComment(commentId, request, principal);
  }

  // 댓글 수정
  @PutMapping("/{comment_id}")
  @ApiOperation(value = "QNA 댓글 수정", notes = "QNA에 댓글을 단 사용자가 자신의 댓글을 수정한다.")
  public APIResponse<QnaCommentResDTO> updateComment(
      @ApiParam(value = "QNA 댓글 ID") @PathVariable("comment_id") Long commentId,
      @ApiParam(value = "QNA 댓글 DTO") @RequestBody QnaCommentReqDTO qnaCommentReqDTO,
      HttpServletRequest request,
      Principal principal) {

    return qnaCommentService.updateComment(commentId, qnaCommentReqDTO, request, principal);
  }
}

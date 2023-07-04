package kr.casealot.shop.domain.notice.comment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import kr.casealot.shop.domain.notice.comment.service.NoticeCommentService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Api(tags = {"NOTICE COMMENT API"}, description = "NOTICE COMMENT 관련 API")
@RequestMapping("/cal/v1/notice")
public class NoticeCommentController {

    private final NoticeCommentService noticeCommentService;

    @PostMapping("/{notice_id}")
    @ApiOperation(value = "공지 댓글 생성", notes = "공지 댓글을 등록한다.")
    public APIResponse<NoticeCommentResDTO> createComment(
            @ApiParam(value = "공지 ID") @PathVariable("notice_id") Long noticeId,
            @ApiParam(value = "공지 댓글 DTO") @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            Principal principal) {

        return noticeCommentService.createComment(noticeId, noticeCommentReqDTO, principal);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment_id}")
    @ApiOperation(value = "공지 댓글 삭제", notes = "자신이 작성한 공지 댓글을 삭제한다.")
    public APIResponse<NoticeCommentResDTO> deleteComment(
            @ApiParam(value = "공지 댓글 ID") @PathVariable("comment_id") Long commentId,
            Principal principal
    ) {
        return noticeCommentService.deleteComment(commentId, principal);
    }

    // 댓글 수정
    @PutMapping("/{comment_id}")
    @ApiOperation(value = "공지 댓글 수정", notes = "자신이 작성한 공지 댓글을 수정한다.")
    public APIResponse<NoticeCommentResDTO> updateComment(
            @ApiParam(value = "공지 댓글 ID") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "공지 댓글 수정 DTO") @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            Principal principal) {

        return noticeCommentService.updateComment(commentId, noticeCommentReqDTO, principal);
    }
}
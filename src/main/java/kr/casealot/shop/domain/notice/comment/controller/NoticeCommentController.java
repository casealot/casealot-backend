package kr.casealot.shop.domain.notice.comment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import kr.casealot.shop.domain.notice.comment.service.NoticeCommentService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Api(tags = {"NOTICE COMMENT API"}, description = "NOTICE COMMENT 관련 API")
@RequestMapping("/cal/v1/notice")
public class NoticeCommentController {
    private final NoticeCommentService noticeCommentService;

    @PostMapping("/{notice_id}")
    public APIResponse<NoticeCommentResDTO> createComment(
            @PathVariable("notice_id") Long noticeId,
            @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            HttpServletRequest request,
            Principal principal){

        return noticeCommentService.createComment(noticeId, noticeCommentReqDTO, request, principal);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment_id}")
    public APIResponse<NoticeCommentResDTO> deleteComment(
            @PathVariable("comment_id") Long commentId,
            HttpServletRequest request,
            Principal principal
    ){
        return noticeCommentService.deleteComment(commentId, request, principal);
    }
    // 댓글 수정
    @PutMapping("/{comment_id}")
    public APIResponse<NoticeCommentResDTO> updateComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            HttpServletRequest request,
            Principal principal){

        return noticeCommentService.updateComment(commentId, noticeCommentReqDTO, request, principal);
    }
}
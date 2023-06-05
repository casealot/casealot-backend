package kr.casealot.shop.domain.notice.comment.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.service.NoticeCommentService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = {"NOTICE COMMENT API"}, description = "NOTICE COMMENT 관련 API")
@RequestMapping("/cal/v1")
public class NoticeCommentController {
    private final NoticeCommentService noticeCommentService;

    @PostMapping("/notice/{notice_id}/comments")
    public APIResponse<Void> createComment(
            @PathVariable("notice_id") Long noticeId,
            @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            HttpServletRequest request){

        return noticeCommentService.createComment(noticeId, noticeCommentReqDTO, request);
    }

    // 댓글 삭제
    @DeleteMapping("/notice/comments/{comment_id}")
    public APIResponse<Void> deleteComment(
            @PathVariable("comment_id") Long commentId,
            HttpServletRequest request
    ){
        return noticeCommentService.deleteComment(commentId, request);
    }
    // 댓글 수정
    @PutMapping("/notice/comments/{comment_id}")
    public APIResponse<Void> updateComment(
            @PathVariable("comment_id") Long commentId,
            @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            HttpServletRequest request){

        return noticeCommentService.updateComment(commentId, noticeCommentReqDTO, request);
    }
}

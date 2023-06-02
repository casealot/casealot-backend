package kr.casealot.shop.domain.notice.comment.controller;

import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentDTO;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.service.NoticeCommentService;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.service.QnaCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cal/v1")
public class NoticeCommentController {
    private final NoticeCommentService noticeCommentService;

    @PostMapping("/notice/{notice_id}/comments")
    public ResponseEntity<String> createComment(
            @PathVariable Long notice_id,
            @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            HttpServletRequest request) throws ChangeSetPersister.NotFoundException {

        noticeCommentService.createComment(notice_id, noticeCommentReqDTO, request);

        return ResponseEntity.ok("create notice");
    }

    // 댓글 삭제
    @DeleteMapping("/notice/{notice_id}/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("notice_id")Long noticeId,
            @PathVariable("comment_id") Long commentId,
            HttpServletRequest request
    ) throws ChangeSetPersister.NotFoundException {

        noticeCommentService.deleteComment(noticeId, commentId, request);

        return ResponseEntity.ok("delete notice");
    }
    // 댓글 수정
    @PutMapping("/notice/{notice_id}/comments/{comment_id}")
    public ResponseEntity<String> updateComment(
            @PathVariable("notice_id")Long noticeId,
            @PathVariable("comment_id") Long commentId,
            @RequestBody NoticeCommentReqDTO noticeCommentReqDTO,
            HttpServletRequest request
    ) throws ChangeSetPersister.NotFoundException {

        noticeCommentService.updateComment(noticeId, commentId, noticeCommentReqDTO, request);

        return ResponseEntity.ok("update notice");
    }
}

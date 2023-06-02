package kr.casealot.shop.domain.notice.controller;

import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cal/v1/notice")
public class NoticeController {
    private final NoticeService noticeService;

    // 공지 전체 조회
    @GetMapping
    public ResponseEntity<List<NoticeResDTO>> getNoticeList(Pageable pageable){
        List<NoticeResDTO> noticeResDTOList = noticeService.getNoticeList(pageable);
        return ResponseEntity.ok(noticeResDTOList);
    }

    // 특정 공지 조회
    @GetMapping("/{notice_id}")
    public ResponseEntity<NoticeResDTO> getNotice(@PathVariable Long notice_id) throws NotFoundException {
        NoticeResDTO noticeResDTO = noticeService.getNoticeById(notice_id);
        return ResponseEntity.ok(noticeResDTO);
    }

    // 공지 작성
    @PostMapping
    public ResponseEntity<String> createNotice(@RequestBody NoticeReqDTO noticeReqDTO, HttpServletRequest request){
        noticeService.createNotice(noticeReqDTO, request);

        return ResponseEntity.ok("create notice");
    }

    // 공지 수정
    @PutMapping("/{notice_id}")
    public ResponseEntity<String> updateNotice(@PathVariable Long notice_id,
                                               @RequestBody NoticeReqDTO noticeReqDTO,
                                               HttpServletRequest request) throws NotFoundException {
        noticeService.updateNotice(notice_id, noticeReqDTO, request);

        return ResponseEntity.ok("update notice");
    }

    // 공지 삭제
    @DeleteMapping("/{notice_id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long notice_id,
                                               HttpServletRequest request) throws NotFoundException {
        noticeService.deleteNotice(notice_id, request);

        return ResponseEntity.ok("delete notice");
    }
}

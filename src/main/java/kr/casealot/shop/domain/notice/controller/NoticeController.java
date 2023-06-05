package kr.casealot.shop.domain.notice.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.notice.dto.NoticeDetailDTO;
import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.service.NoticeService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"NOTICE API"}, description = "NOTICE 관련 API")
@RequestMapping("/cal/v1/notice")
public class NoticeController {
    private final NoticeService noticeService;

    // 공지 전체 조회
    @GetMapping
    public APIResponse<List<NoticeResDTO>> getNoticeList(Pageable pageable){

        return noticeService.getNoticeList(pageable);
    }

    // 특정 공지 조회
    @GetMapping("/{notice_id}")
    public APIResponse<NoticeDetailDTO> getNotice(@PathVariable("notice_id") Long noticeId) throws NotFoundException {

        return noticeService.getNotice(noticeId);
    }

    // 공지 작성
    @PostMapping
    public APIResponse<Void> createNotice(@RequestBody NoticeReqDTO noticeReqDTO, HttpServletRequest request){

        return noticeService.createNotice(noticeReqDTO, request);
    }

    // 공지 수정
    @PutMapping("/{notice_id}")
    public APIResponse<Void> updateNotice(@PathVariable("notice_id") Long noticeId,
                                          @RequestBody NoticeReqDTO noticeReqDTO,
                                          HttpServletRequest request) throws NotFoundException {

        return noticeService.updateNotice(noticeId, noticeReqDTO, request);
    }

    // 공지 삭제
    @DeleteMapping("/{notice_id}")
    public APIResponse<Void> deleteNotice(@PathVariable("notice_id") Long noticeId,
                                               HttpServletRequest request) throws NotFoundException {


        return noticeService.deleteNotice(noticeId, request);
    }
}

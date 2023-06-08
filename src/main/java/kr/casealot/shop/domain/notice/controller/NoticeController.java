package kr.casealot.shop.domain.notice.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.notice.dto.NoticeDetailDTO;
import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.service.NoticeService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

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
}
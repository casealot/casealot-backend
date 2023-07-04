package kr.casealot.shop.domain.notice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.service.NoticeService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Api(tags = {"NOTICE ADMIN API"}, description = "NOTICE ADMIN 관련 API")
@RequestMapping("/cal/v1/admin/notice")
public class NoticeAdminController {

    private final NoticeService noticeService;

    // 공지 등록
    @PostMapping
    @ApiOperation(value = "공지 등록", notes = "ADMIN 권한을 가진 사용자가 공지를 등록한다.")
    public APIResponse<NoticeResDTO> createNotice(
            @ApiParam(value = "공지 등록 DTO") @RequestBody NoticeReqDTO noticeReqDTO,
            Principal principal) {

        return noticeService.createNotice(noticeReqDTO, principal);
    }

    // 공지 수정
    @PutMapping("/{notice_id}")
    @ApiOperation(value = "공지 수정", notes = "ADMIN 권한을 가진 사용자가 공지를 수정한다.")
    public APIResponse<NoticeResDTO> updateNotice(
            @ApiParam(value = "공지 ID") @PathVariable("notice_id") Long noticeId,
            @RequestBody NoticeReqDTO noticeReqDTO,
            Principal principal) {

        return noticeService.updateNotice(noticeId, noticeReqDTO, principal);
    }

    // 공지 삭제
    @DeleteMapping("/{notice_id}")
    @ApiOperation(value = "공지 삭제", notes = "ADMIN 권한을 가진 사용자가 공지를 삭제한다.")
    public APIResponse<NoticeResDTO> deleteNotice(
            @ApiParam(value = "공지 ID") @PathVariable("notice_id") Long noticeId,
            Principal principal) {

        return noticeService.deleteNotice(noticeId, principal);
    }
}

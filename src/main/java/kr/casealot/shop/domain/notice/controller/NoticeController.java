package kr.casealot.shop.domain.notice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.casealot.shop.domain.notice.dto.NoticeDetailDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.service.NoticeService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@RestController
@RequiredArgsConstructor
@Api(tags = {"NOTICE API"}, description = "NOTICE 관련 API")
@RequestMapping("/cal/v1/notice/list")
public class NoticeController {

  private final NoticeService noticeService;

  // 공지 전체 조회
  @GetMapping
  @ApiOperation(value = "공지 전체 조회", notes = "모든 사용자가 전체 공지를 조회한다.")
  public APIResponse<List<NoticeResDTO>> getNoticeList(Pageable pageable) {
    return noticeService.getNoticeList(pageable);
  }

  // 특정 공지 조회
  @GetMapping("/{notice_id}")
  @ApiOperation(value = "공지 전체 조회", notes = "모든 사용자가 특정 공지를 조회한다.")
  public APIResponse<NoticeDetailDTO> getNotice(
      @ApiParam(value = "공지 ID") @PathVariable("notice_id") Long noticeId,
      Principal principal) {
    return noticeService.getNotice(noticeId, principal);
  }
}
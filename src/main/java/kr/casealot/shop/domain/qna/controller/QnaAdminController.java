package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.casealot.shop.domain.qna.dto.QnaResDTO;
import kr.casealot.shop.domain.qna.service.QnaService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"QNA ADMIN API"}, description = "QNA ADMIN 관련 API")
@RequestMapping("/cal/v1/admin/qna")
public class QnaAdminController {

    private final QnaService qnaService;

    @GetMapping("/list")
    @ApiOperation(value = "댓글이 달리지 않은 QNA 내역 조회", notes = "관리자가 댓글이 달리지 않은 QNA 내역을 조회한다.")
    public APIResponse<List<QnaResDTO>> getAdminQnaList(Pageable pageable) {

        return qnaService.getAdminQnaList(pageable);
    }

    @GetMapping("/today")
    @ApiOperation(value = "댓글이 달리지 않고, 오늘 작성된 QNA 내역 조회", notes = "관리자가 댓글이 달리지 않은, 오늘 작성된 QNA 내역을 조회한다.")
    public APIResponse<List<QnaResDTO>> getAdminTodayQnaList(Pageable pageable) {

        return qnaService.getAdminTodayQnaList(pageable);
    }
}

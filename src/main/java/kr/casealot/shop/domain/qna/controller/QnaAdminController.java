package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.dto.QnaResDTO;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
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

    private final QnaRepository qnaRepository;
    private final QnaService qnaService;

    @GetMapping("/list")
    public APIResponse<List<QnaResDTO>> getAdminQnaList(Pageable pageable) {

        return qnaService.getAdminQnaList(pageable);
    }
}

package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.dto.QnaReqDTO;
import kr.casealot.shop.domain.qna.dto.QnaResDTO;
import kr.casealot.shop.domain.qna.service.QnaService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"QNA API"}, description = "QNA 관련 API")
@RequestMapping("/cal/v1/qna")
public class QnaController {

    private final QnaService qnaService;

//    @PostMapping
//    public ResponseEntity<Qna> createQna(@RequestBody QnaDTO qnaDTO){
//        Qna qna = qnaService.createQna(qnaDTO);
//        return ResponseEntity.status(CREATED).body(qna);
//    }



    @PostMapping
    public APIResponse createQna(@RequestBody QnaReqDTO qnaReqDTO,
                                       HttpServletRequest request
    ) {

        return qnaService.createQna(qnaReqDTO, request);
    }

    @PutMapping("/{qna_id}")
    public APIResponse updateQna(@PathVariable("qna_id") Long qnaId,
                                       @RequestBody QnaReqDTO qnaReqDTO,
                                       HttpServletRequest request){



        return qnaService.updateQna(qnaId, qnaReqDTO, request);
    }

    @GetMapping("/{qna_id}")
    public APIResponse getQna(@PathVariable("qna_id") Long qnaId){


        return qnaService.getQna(qnaId);
    }

    @DeleteMapping("/{qna_id}")
    public APIResponse deleteQna(@PathVariable("qna_id") Long qnaId,
                                       HttpServletRequest request){

        return qnaService.deleteQna(qnaId, request);
    }

    @GetMapping("/list")
    public APIResponse<List<QnaResDTO>> getQnaList(Pageable pageable) {

        return qnaService.getQnaList(pageable);
    }
}

//<List<QnaDTO>>
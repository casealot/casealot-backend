package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.dto.QnaDetailDTO;
import kr.casealot.shop.domain.qna.dto.QnaReqDTO;
import kr.casealot.shop.domain.qna.dto.QnaResDTO;
import kr.casealot.shop.domain.qna.service.QnaService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
    public APIResponse<QnaResDTO> createQna(@RequestBody QnaReqDTO qnaReqDTO,
                                            HttpServletRequest request,
                                            Principal principal) {

        return qnaService.createQna(qnaReqDTO, request, principal);
    }

    @PutMapping("/{qna_id}")
    public APIResponse<QnaResDTO> updateQna(@PathVariable("qna_id") Long qnaId,
                                       @RequestBody QnaReqDTO qnaReqDTO,
                                       HttpServletRequest request,
                                       Principal principal){



        return qnaService.updateQna(qnaId, qnaReqDTO, request, principal);
    }

    @GetMapping("/list/{qna_id}")
    public APIResponse<QnaDetailDTO> getQna(@PathVariable("qna_id") Long qnaId, Principal principal){


        return qnaService.getQna(qnaId, principal);
    }

    @DeleteMapping("/{qna_id}")
    public APIResponse<QnaResDTO> deleteQna(@PathVariable("qna_id") Long qnaId,
                                            HttpServletRequest request,
                                            Principal principal){

        return qnaService.deleteQna(qnaId, request, principal);
    }

    @GetMapping("/list")
    public APIResponse<List<QnaResDTO>> getQnaList(Pageable pageable) {

        return qnaService.getQnaList(pageable);
    }
}

//<List<QnaDTO>>
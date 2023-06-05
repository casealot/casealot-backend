package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.dto.QnaDTO;
import kr.casealot.shop.domain.qna.dto.QnaDetailDTO;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.service.QnaService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.data.crossstore.ChangeSetPersister.*;

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
    public APIResponse<Void> createQna(@RequestBody QnaDTO qnaDTO,
                                       HttpServletRequest request
    ) {

        return qnaService.createQna(qnaDTO, request);
    }

    @PutMapping("/{qna_id}")
    public APIResponse<Void> updateQna(@PathVariable("qna_id") Long qnaId,
                                       @RequestBody QnaDTO qnaDTO,
                                       HttpServletRequest request){



        return qnaService.updateQna(qnaId, qnaDTO, request);
    }

    @GetMapping("/{qna_id}")
    public APIResponse<QnaDetailDTO> getQna(@PathVariable("qna_id") Long qnaId){


        return qnaService.getQna(qnaId);
    }

    @DeleteMapping("/{qna_id}")
    public APIResponse<Void> deleteQna(@PathVariable("qna_id") Long qnaId,
                                       HttpServletRequest request){

        return qnaService.deleteQna(qnaId, request);
    }

    @GetMapping("/list")
    public APIResponse<List<QnaDTO>> getQnaList(Pageable pageable) {

        return qnaService.getQnaList(pageable);
    }
}

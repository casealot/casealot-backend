package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.qna.dto.QnaDTO;
import kr.casealot.shop.domain.qna.dto.QnaDetailDTO;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.service.QnaService;
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
    public ResponseEntity<String> createQna(@RequestBody QnaDTO qnaDTO,
                                            HttpServletRequest request
    ) {
        qnaService.createQna(qnaDTO, request);
        return ResponseEntity.ok("create Q&A");
    }

    @PutMapping("/{qna_id}")
    public ResponseEntity<String> updateQna(@PathVariable("qna_id") Long qnaId,
                                            @RequestBody QnaDTO qnaDTO,
                                            HttpServletRequest request) throws NotFoundException {

        qnaService.updateQna(qnaId, qnaDTO, request);

        return ResponseEntity.ok("update Q&A");
    }

    @GetMapping("/{qna_id}")
    public ResponseEntity<QnaDetailDTO> getQna(@PathVariable("qna_id") Long qnaId) throws NotFoundException {

        QnaDetailDTO qnaDTO = qnaService.getQna(qnaId);
        return ResponseEntity.ok(qnaDTO);
    }

    @DeleteMapping("/{qna_id}")
    public ResponseEntity<String> deleteQna(@PathVariable("qna_id") Long qnaId,
                                            HttpServletRequest request
    ) throws NotFoundException {
        qnaService.deleteQna(qnaId, request);
        return ResponseEntity.ok("delete Q&A");
    }

    @GetMapping("/list")
    public ResponseEntity<List<QnaDTO>> getQnaList(Pageable pageable) {
        List<QnaDTO> qnaList = qnaService.getQnaList(pageable);
        return ResponseEntity.ok(qnaList);
    }
}

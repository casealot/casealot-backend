package kr.casealot.shop.domain.qna.controller;

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
@RequestMapping("/cal/v1/qna")
public class QnaController {

    private final QnaService qnaService;

//    @PostMapping
//    public ResponseEntity<Qna> createQna(@RequestBody QnaDTO qnaDTO){
//        Qna qna = qnaService.createQna(qnaDTO);
//        return ResponseEntity.status(CREATED).body(qna);
//    }


//    @PostMapping("/test")
//    public CreateQna.Response createQna(@Valid @RequestBody CreateQna.Request request){
//        return CreateQna.Response.from(
//                qnaService.createQna(
//                        request.getTitle(),
//                        request.getContent(),
//                        request.getPhotoUrl()
//                )
//        );
//    }
    @PostMapping
    public ResponseEntity<QnaDTO> createQna(@RequestBody QnaDTO qnaDTO,
                                            HttpServletRequest request
    ) {
        return ResponseEntity.ok(qnaService.createQna(qnaDTO, request));
    }

    @PutMapping("/{qna_id}")
    public ResponseEntity<QnaDTO> updateQna(@PathVariable("qna_id") Long qnaId,
                                            @RequestBody QnaDTO qnaDTO,
                                            HttpServletRequest request
    ) throws NotFoundException {

        return ResponseEntity.ok(qnaService.updateQna(qnaId, qnaDTO, request));
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

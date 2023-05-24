package kr.casealot.shop.domain.qna.controller;

import kr.casealot.shop.domain.qna.dto.QnaDTO;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.service.QnaService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1")
public class QnaController {
    private final QnaService qnaService;
    @PostMapping("/qna")
    public ResponseEntity<Qna> createQna(@RequestBody QnaDTO qnaDTO){
        Qna qna = qnaService.createQna(qnaDTO);
        return ResponseEntity.status(CREATED).body(qna);
    }

    @GetMapping("/qna/{qna_id}")
    public QnaDTO getQna(@PathVariable("qna_id") Long qnaId) throws ChangeSetPersister.NotFoundException {

        return qnaService.getQna(qnaId);
    }


    @PutMapping("/qna/{qna_id}")
    public ResponseEntity<String> updateQna(@PathVariable("qna_id") Long qnaId,
                                            @RequestBody QnaDTO qnaDto) throws ChangeSetPersister.NotFoundException {
        qnaService.updateQna(qnaId, qnaDto);
        return ResponseEntity.ok("Q&A가 성공적으로 업데이트되었습니다.");
    }

    @DeleteMapping("/qna/{qna_id}")
    public ResponseEntity<String> deleteQna(@PathVariable("qna_id") Long qnaId) throws ChangeSetPersister.NotFoundException {
        qnaService.deleteQna(qnaId);
        return ResponseEntity.ok("Q&A가 성공적으로 삭제되었습니다.");
    }


    //다시생각해보자
    @GetMapping("/qna/list")
    public ResponseEntity<List<Qna>> getQnaList(Pageable pageable) {
        Page<Qna> qnaPage = qnaService.getQnaList(pageable);
        List<Qna> qnaList = qnaPage.getContent();
        return ResponseEntity.ok(qnaList);
    }
}

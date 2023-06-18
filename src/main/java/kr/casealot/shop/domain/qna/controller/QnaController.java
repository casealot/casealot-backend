package kr.casealot.shop.domain.qna.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
  @ApiOperation(value = "QNA 작성", notes = "사용자가 QNA를 작성한다.")
  public APIResponse<QnaResDTO> createQna(
      @ApiParam(value = "QNA 등록/수정 DTO") @RequestBody QnaReqDTO qnaReqDTO,
      HttpServletRequest request,
      Principal principal) {

    return qnaService.createQna(qnaReqDTO, request, principal);
  }

  @PutMapping("/{qna_id}")
  @ApiOperation(value = "QNA 수정", notes = "사용자가 작성한 QNA를 수정한다.")
  public APIResponse<QnaResDTO> updateQna(
      @ApiParam(value = "QNA ID") @PathVariable("qna_id") Long qnaId,
      @ApiParam(value = "QNA 등록/수정 DTO") @RequestBody QnaReqDTO qnaReqDTO,
      HttpServletRequest request,
      Principal principal) {

    return qnaService.updateQna(qnaId, qnaReqDTO, request, principal);
  }

  @GetMapping("/list/{qna_id}")
  @ApiOperation(value = "QNA 조회", notes = "QNA를 조회한다.")
  public APIResponse<QnaDetailDTO> getQna(
      @ApiParam(value = "QNA ID") @PathVariable("qna_id") Long qnaId, Principal principal) {

    return qnaService.getQna(qnaId, principal);
  }

  @DeleteMapping("/{qna_id}")
  @ApiOperation(value = "QNA 삭제", notes = "사용자가 작성한 QNA를 삭제한다.")
  public APIResponse<QnaResDTO> deleteQna(
      @ApiParam(value = "QNA ID") @PathVariable("qna_id") Long qnaId,
      HttpServletRequest request,
      Principal principal) {

    return qnaService.deleteQna(qnaId, request, principal);
  }

  @GetMapping("/list")
  @ApiOperation(value = "QNA 조회", notes = "QNA 목록을 조회한다.")
  public APIResponse<List<QnaResDTO>> getQnaList(Pageable pageable) {

    return qnaService.getQnaList(pageable);
  }
}
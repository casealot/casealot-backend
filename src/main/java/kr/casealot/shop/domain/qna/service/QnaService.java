package kr.casealot.shop.domain.qna.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentResDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.dto.*;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.PermissionException;
import kr.casealot.shop.global.oauth.token.*;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final String API_NAME = "qna";

    private final QnaRepository qnaRepository;
    private final CustomerRepository customerRepository;
    private final AuthTokenProvider authTokenProvider;

//     qna 등록
//    @Transactional
//    public Qna createQna(CreateQna qnaDTO){
//        Qna qna = Qna.builder()
//                .title(qnaDTO.getTitle())
//                .content(qnaDTO.getContent())
//                .photoUrl(qnaDTO.getPhotoUrl())
//                .registrationDate(LocalDateTime.now())
//                .modificationDate(LocalDateTime.now())
//                .build();
//        return qnaRepository.save(qna);
//    }


    @Transactional
    public APIResponse<QnaResDTO> createQna(QnaReqDTO qnaReqDTO,
                                            HttpServletRequest request,
                                            Principal principal) {

        String customerId = principal.getName();

        if(customerId == null){
            return APIResponse.invalidAccessToken();
        }

        Customer customer = customerRepository.findById(customerId);

        Qna qna = Qna.builder()
                .title(qnaReqDTO.getTitle())
                .content(qnaReqDTO.getContent())
                .customer(customer)
                .build();

        qnaRepository.save(qna);

        QnaResDTO qnaResDTO = getQnaResDTO(qna, customerId);

        return APIResponse.success(API_NAME,qnaResDTO);
    }


    // qna 수정
    @Transactional
    public APIResponse<QnaResDTO> updateQna(Long qnaId,
                                            QnaReqDTO qnaReqDTO,
                                            HttpServletRequest request,
                                            Principal principal){
        Qna qna = qnaRepository.findById(qnaId).orElse(null);

        if(qna == null){
            return APIResponse.notExistRequest();
        }

        String customerId = principal.getName();

        Customer customer = customerRepository.findById(customerId);

        if(customerId == null){
            return APIResponse.invalidAccessToken();
        }

        if (!customerId.equals(customer.getId())) {
            return APIResponse.permissionDenied();
        }

        qna.setTitle(qnaReqDTO.getTitle());
        qna.setContent(qnaReqDTO.getContent());

        qnaRepository.save(qna);

        QnaResDTO qnaResDTO = getQnaResDTO(qna, customerId);

        return APIResponse.success(API_NAME, qnaResDTO);
    }

    // qna 조회
    @Transactional
    public APIResponse<QnaDetailDTO> getQna(Long qnaId){
        Qna qna = qnaRepository.findById(qnaId).orElse(null);

        if(qna == null){
            return APIResponse.notExistRequest();
        }
        // 조회수 증가
        qna.setViews(qna.getViews() + 1);

        QnaDetailDTO qnaDetailDTO = QnaDetailDTO.builder()
                .id(qna.getId())
                .customerId(qna.getCustomer().getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .views(qna.getViews())
                .createdDt(qna.getCreatedDt())
                .modifiedDt(qna.getModifiedDt())
                .build();

        List<QnaComment> qnaCommentList = qna.getQnaCommentList();
        List<QnaCommentResDTO> qnaCommentDTOList = new ArrayList<>();

        for (QnaComment qnaComment : qnaCommentList) {
            QnaCommentResDTO qnaCommentDTO = QnaCommentResDTO.builder()
                    .id(qnaComment.getId())
                    .customerId(qnaComment.getCustomer().getId())
                    .title(qnaComment.getTitle())
                    .content(qnaComment.getContent())
                    .createdDt(qnaComment.getCreatedDt())
                    .modifiedDt(qnaComment.getModifiedDt())
                    .build();

            qnaCommentDTOList.add(qnaCommentDTO);
        }

        qnaDetailDTO.setQnaCommentList(qnaCommentDTOList);

        return APIResponse.success(API_NAME, qnaDetailDTO);
    }


    // qna 삭제
    @Transactional
    public APIResponse<QnaResDTO> deleteQna(Long qnaId,
                                            HttpServletRequest request,
                                            Principal principal){
        Qna qna = qnaRepository.findById(qnaId).orElse(null);

        if(qna == null){
            return APIResponse.notExistRequest();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!(customerId.equals(qna.getCustomer().getId()) || !isAdmin)) {
            throw new PermissionException("올바르지 않은 권한입니다.");
        }

        qnaRepository.delete(qna);

        QnaResDTO qnaResDTO = getQnaResDTO(qna, customerId);

        return APIResponse.success(API_NAME, qnaResDTO);
    }

    // qna 목록
    public APIResponse<List<QnaResDTO>> getQnaList(Pageable pageable) {
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        List<Qna> qnaList = qnaPage.getContent();

        List<QnaResDTO> qnaResDTOList = new ArrayList<>();

        for (Qna qna : qnaList) {
            String customerId = qna.getCustomer().getId();
            QnaResDTO qnaResDTO = QnaResDTO.builder()
                    .id(qna.getId())
                    .customerId(customerId)
                    .title(qna.getTitle())
                    .content(qna.getContent())
                    .views(qna.getViews())
                    .createdDt(qna.getCreatedDt())
                    .modifiedDt(qna.getModifiedDt())
                    .build();
            qnaResDTOList.add(qnaResDTO);
        }

        return APIResponse.success(API_NAME, qnaResDTOList);
    }

    private static QnaResDTO getQnaResDTO(Qna qna, String customerId) {
        QnaResDTO qnaResDTO = new QnaResDTO();
        qnaResDTO.setId(qna.getId());
        qnaResDTO.setCustomerId(customerId);
        qnaResDTO.setTitle(qna.getTitle());
        qnaResDTO.setContent(qna.getContent());
        qnaResDTO.setViews(qna.getViews());
        qnaResDTO.setCreatedDt(qna.getCreatedDt());
        qnaResDTO.setModifiedDt(qna.getModifiedDt());
        return qnaResDTO;
    }

    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }
}

package kr.casealot.shop.domain.qna.comment.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentReqDTO;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentResDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final QnaCommentRepository qnaCommentRepository;
    private final QnaRepository qnaRepository;
    private final CustomerRepository customerRepository;

    public APIResponse<QnaCommentResDTO> createQnaComment(Long qnaId,
                                                          QnaCommentReqDTO qnaCommentReqDTO,
                                                          HttpServletRequest request,
                                                          Principal principal) {

        Qna qna = qnaRepository.findById(qnaId).orElse(null);

        if(qna == null){
            return APIResponse.notExistRequest();
        }
        String customerId = principal.getName();
        Customer customer = customerRepository.findById(customerId);

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            return APIResponse.permissionDenied();
        }

        QnaComment qnaComment = QnaComment.builder()
                .qna(qna)
                .customer(customer)
                .title(qnaCommentReqDTO.getTitle())
                .content(qnaCommentReqDTO.getContent())
                .build();

        qnaCommentRepository.save(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success("qna comment", qnaCommentResDTO);
    }

    public APIResponse<QnaCommentResDTO> deleteComment(Long commentId,
                                                       HttpServletRequest request,
                                                       Principal principal) {

        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElse(null);

        if(qnaComment == null){
            return APIResponse.notExistRequest();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            return APIResponse.permissionDenied();
        }

        qnaCommentRepository.delete(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success("qna comment", qnaCommentResDTO);
    }

    public APIResponse<QnaCommentResDTO> updateComment(Long commentId,
                                                       QnaCommentReqDTO qnaCommentReqDTO,
                                                       HttpServletRequest request,
                                                       Principal principal) {

        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElse(null);

        if(qnaComment == null){
            return APIResponse.notExistRequest();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            return APIResponse.permissionDenied();
        }

        qnaComment.setTitle(qnaCommentReqDTO.getTitle());
        qnaComment.setContent(qnaCommentReqDTO.getContent());


        qnaCommentRepository.save(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success("qna comment", qnaCommentResDTO);
    }


    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }

    private QnaCommentResDTO getQnaCommentResDTO(String customerId, QnaComment qnaComment) {
        QnaCommentResDTO qnaCommentResDTO = new QnaCommentResDTO();
        qnaCommentResDTO.setId(qnaComment.getId());
        qnaCommentResDTO.setCustomerId(customerId);
        qnaCommentResDTO.setTitle(qnaComment.getTitle());
        qnaCommentResDTO.setContent(qnaComment.getContent());
        qnaCommentResDTO.setCreatedDt(qnaComment.getCreatedDt());
        qnaCommentResDTO.setModifiedDt(qnaComment.getModifiedDt());
        return qnaCommentResDTO;
    }
}
package kr.casealot.shop.domain.qna.comment.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentReqDTO;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentResDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.NotFoundCommentException;
import kr.casealot.shop.global.exception.NotFoundWriteException;
import kr.casealot.shop.global.exception.PermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class QnaCommentService {
    private final String API_NAME = "qna comment";


    private final QnaCommentRepository qnaCommentRepository;
    private final QnaRepository qnaRepository;
    private final CustomerRepository customerRepository;

    public APIResponse<QnaCommentResDTO> createQnaComment(Long qnaId, QnaCommentReqDTO qnaCommentReqDTO, Principal principal) {

        Qna qna = qnaRepository.findById(qnaId).orElse(null);


        if (qna == null) {
            throw new NotFoundWriteException();
        }

        qna.setHasReply(true);
        String customerId = principal.getName();
        Customer customer = customerRepository.findById(customerId);

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new PermissionException();
        }

        QnaComment qnaComment = QnaComment.builder().qna(qna).customer(customer).content(qnaCommentReqDTO.getContent()).build();

        qnaCommentRepository.save(qnaComment);

        qnaRepository.save(qna);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success(API_NAME, qnaCommentResDTO);
    }

    public APIResponse<QnaCommentResDTO> deleteComment(Long commentId, Principal principal) {

        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElse(null);

        if (qnaComment == null) {
            throw new NotFoundCommentException();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new PermissionException();
        }

        qnaCommentRepository.delete(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success(API_NAME, qnaCommentResDTO);
    }

    public APIResponse<QnaCommentResDTO> updateComment(Long commentId, QnaCommentReqDTO qnaCommentReqDTO, Principal principal) {

        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElse(null);

        if (qnaComment == null) {
            throw new NotFoundCommentException();
        }

        String customerId = principal.getName();


        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new PermissionException();
        }

        qnaComment.setContent(qnaCommentReqDTO.getContent());


        qnaCommentRepository.save(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success(API_NAME, qnaCommentResDTO);
    }


    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }

    private QnaCommentResDTO getQnaCommentResDTO(String customerId, QnaComment qnaComment) {
        QnaCommentResDTO qnaCommentResDTO = new QnaCommentResDTO();
        qnaCommentResDTO.setId(qnaComment.getId());
        qnaCommentResDTO.setCustomerId(customerId);
        qnaCommentResDTO.setContent(qnaComment.getContent());
        qnaCommentResDTO.setCreatedDt(qnaComment.getCreatedDt());
        qnaCommentResDTO.setModifiedDt(qnaComment.getModifiedDt());
        return qnaCommentResDTO;
    }
}
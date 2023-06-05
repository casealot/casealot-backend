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

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final QnaCommentRepository qnaCommentRepository;
    private final QnaRepository qnaRepository;
    private final CustomerRepository customerRepository;
    private final AuthTokenProvider authTokenProvider;

    public APIResponse<QnaCommentResDTO> createQnaComment(Long qnaId,
                                                          QnaCommentReqDTO qnaCommentReqDTO,
                                                          HttpServletRequest request){

        Qna qna = qnaRepository.findById(qnaId).orElseThrow();
        String customerId = findCustomerId(request);
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


    public APIResponse<QnaCommentResDTO> deleteComment(Long commentId, HttpServletRequest request){

        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow();
        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if (!(customerId.equals(qnaComment.getCustomer().getId()) || !isAdmin)) {
            return APIResponse.permissionDenied();
        }

        qnaCommentRepository.delete(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success("qna comment", qnaCommentResDTO);
    }

    public APIResponse<QnaCommentResDTO> updateComment(Long commentId, QnaCommentReqDTO qnaCommentReqDTO, HttpServletRequest request) {

        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow();

        String customerId = findCustomerId(request);

        if (!(customerId.equals(qnaComment.getCustomer().getId()))) {
            return APIResponse.permissionDenied();
        }

        qnaComment.setTitle(qnaCommentReqDTO.getTitle());
        qnaComment.setContent(qnaCommentReqDTO.getContent());

        qnaCommentRepository.save(qnaComment);

        QnaCommentResDTO qnaCommentResDTO = getQnaCommentResDTO(customerId, qnaComment);

        return APIResponse.success("qna comment", qnaCommentResDTO);
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

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }

    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }
}
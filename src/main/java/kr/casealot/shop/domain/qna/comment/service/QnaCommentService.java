package kr.casealot.shop.domain.qna.comment.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;
import static org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final QnaCommentRepository qnaCommentRepository;
    private final QnaRepository qnaRepository;
    private final CustomerRepository customerRepository;
    private final AuthTokenProvider authTokenProvider;

    public APIResponse<Void> createQnaComment(Long qnaId,
                                              QnaCommentDTO qnaCommentDto,
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
                .title(qnaCommentDto.getTitle())
                .content(qnaCommentDto.getContent())
                .build();

        qnaCommentRepository.save(qnaComment);

        return APIResponse.success("공지 댓글 작성 성공", null);
    }

    public APIResponse<Void> deleteComment(Long qnaId, Long commentId, HttpServletRequest request){

        Qna qna = qnaRepository.findById(qnaId).orElseThrow();
        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow();
        String customerId = findCustomerId(request);
        String qnaCustomerId = qnaComment.getCustomer().getId();

        boolean isAdmin = checkAdminRole(customerId);

        if (!(customerId.equals(qnaCustomerId) || !isAdmin)) {
            return APIResponse.permissionDenied();
        }

        qna.getQnaCommentList().remove(qnaComment);
        qnaCommentRepository.delete(qnaComment);

        return APIResponse.success("공지 댓글 삭제 성공", null);
    }

    public APIResponse<Void> updateComment(Long qnaId, Long commentId, QnaCommentDTO qnaCommentDTO, HttpServletRequest request) {

        Qna qna = qnaRepository.findById(qnaId).orElseThrow();
        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow();

        String customerId = findCustomerId(request);
        String qnaCustomerId = qnaComment.getCustomer().getId();

        if (!(customerId.equals(qnaCustomerId))) {
            return APIResponse.permissionDenied();
        }

        qnaComment.setTitle(qnaCommentDTO.getTitle());
        qnaComment.setContent(qnaCommentDTO.getContent());
        qnaComment.setQna(qna);
        qnaCommentRepository.save(qnaComment);

        return APIResponse.success("공지 댓글 수정 성공", null);
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
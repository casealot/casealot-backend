package kr.casealot.shop.domain.qna.comment.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
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

    public void createQnaComment(Long qnaId,
                                 QnaCommentDTO qnaCommentDto,
                                 HttpServletRequest request) throws NotFoundException {

        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        String customerId = findCustomerId(request);
        Customer customer = customerRepository.findById(customerId);

        QnaComment qnaComment = QnaComment.builder()
                .qna(qna)
                .customer(customer)
                .title(qnaCommentDto.getTitle())
                .content(qnaCommentDto.getContent())
                .build();

        qnaCommentRepository.save(qnaComment);
    }

    public void deleteComment(Long qnaId, Long commentId, HttpServletRequest request) throws NotFoundException {

        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        String customerId = findCustomerId(request);
        String qnaCustomerId = qnaComment.getCustomer().getId();

        boolean isAdmin = checkAdminRole(customerId);

        if (!(customerId.equals(qnaCustomerId) || !isAdmin)) {
            throw new AccessDeniedException("You are not authorized to delete this QnaComment.");
        }

        qna.getQnaCommentList().remove(qnaComment);
        qnaCommentRepository.delete(qnaComment);

    }

    public void updateComment(Long qnaId, Long commentId, QnaCommentDTO qnaCommentDTO, HttpServletRequest request) throws NotFoundException {

        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        QnaComment qnaComment = qnaCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);

        String customerId = findCustomerId(request);
        String qnaCustomerId = qnaComment.getCustomer().getId();

        if (!(customerId.equals(qnaCustomerId))) {
            throw new AccessDeniedException("You are not authorized to update this QnaComment.");
        }

        qnaComment.setTitle(qnaCommentDTO.getTitle());
        qnaComment.setContent(qnaCommentDTO.getContent());
        qnaComment.setQna(qna);
        qnaCommentRepository.save(qnaComment);
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
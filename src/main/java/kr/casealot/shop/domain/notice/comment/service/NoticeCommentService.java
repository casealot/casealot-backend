package kr.casealot.shop.domain.notice.comment.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import kr.casealot.shop.domain.notice.comment.repository.NoticeCommentRepository;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.repository.NoticeRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class NoticeCommentService {

    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeRepository noticeRepository;
    private final AuthTokenProvider authTokenProvider;
    private final CustomerRepository customerRepository;

    public APIResponse<Void> createComment(Long noticeId,
                                           NoticeCommentReqDTO noticeCommentReqDTO,
                                           HttpServletRequest request){

        Notice notice = noticeRepository.findById(noticeId).orElseThrow();
        String customerId = findCustomerId(request);
        Customer customer = customerRepository.findById(customerId);

        NoticeComment noticeComment = NoticeComment.builder()
                .notice(notice)
                .customer(customer)
                .title(noticeCommentReqDTO.getTitle())
                .content(noticeCommentReqDTO.getContent())
                .build();

        noticeCommentRepository.save(noticeComment);

        return APIResponse.success("공지 댓글 등록 성공", null);
    }

    public APIResponse<Void> deleteComment(Long commentId, HttpServletRequest request){

        NoticeComment noticeComment = noticeCommentRepository.findById(commentId).orElseThrow();

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin || !customerId.equals(noticeComment.getCustomer().getId())){
            return APIResponse.permissionDenied();
        }

        noticeCommentRepository.delete(noticeComment);

        return APIResponse.success("공지 댓글 삭제 성공", null);
    }

    public APIResponse<Void> updateComment(Long commentId, NoticeCommentReqDTO noticeCommentReqDTO,
                              HttpServletRequest request){

        NoticeComment noticeComment = noticeCommentRepository.findById(commentId).orElseThrow();

        String customerId = findCustomerId(request);

        if(!customerId.equals(noticeComment.getCustomer().getId())){
            return APIResponse.permissionDenied();
        }

        noticeComment.setTitle(noticeCommentReqDTO.getTitle());
        noticeComment.setContent(noticeCommentReqDTO.getContent());

        noticeCommentRepository.save(noticeComment);

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

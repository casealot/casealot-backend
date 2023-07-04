package kr.casealot.shop.domain.notice.comment.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import kr.casealot.shop.domain.notice.comment.repository.NoticeCommentRepository;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.repository.NoticeRepository;
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
public class NoticeCommentService {

    private final NoticeCommentRepository noticeCommentRepository;
    private final NoticeRepository noticeRepository;
    private final CustomerRepository customerRepository;

    public APIResponse<NoticeCommentResDTO> createComment(Long noticeId,
                                                          NoticeCommentReqDTO noticeCommentReqDTO,
                                                          Principal principal) {

        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice == null) {
            throw new NotFoundWriteException();
        }

        String customerId = principal.getName();
        Customer customer = customerRepository.findById(customerId);

        NoticeComment noticeComment = NoticeComment.builder()
                .notice(notice)
                .customer(customer)
                .title(noticeCommentReqDTO.getTitle())
                .content(noticeCommentReqDTO.getContent())
                .build();

        noticeCommentRepository.save(noticeComment);

        NoticeCommentResDTO noticeCommentResDTO = getNoticeCommentResDTO(customerId, noticeComment);

        return APIResponse.success("notice comment", noticeCommentResDTO);
    }

    public APIResponse<NoticeCommentResDTO> deleteComment(Long commentId,
                                                          Principal principal) {

        NoticeComment noticeComment = noticeCommentRepository.findById(commentId).orElse(null);

        if (noticeComment == null) {
            throw new NotFoundCommentException();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin || !customerId.equals(noticeComment.getCustomer().getId())) {
            throw new PermissionException();
        }

        noticeCommentRepository.delete(noticeComment);

        NoticeCommentResDTO noticeCommentResDTO = getNoticeCommentResDTO(customerId, noticeComment);

        return APIResponse.success("notice comment", noticeCommentResDTO);
    }

    public APIResponse<NoticeCommentResDTO> updateComment(Long commentId,
                                                          NoticeCommentReqDTO noticeCommentReqDTO,
                                                          Principal principal) {

        NoticeComment noticeComment = noticeCommentRepository.findById(commentId).orElse(null);

        if (noticeComment == null) {
            throw new NotFoundCommentException();
        }

        String customerId = principal.getName();

        if (!customerId.equals(noticeComment.getCustomer().getId())) {
            throw new PermissionException();
        }

        noticeComment.setTitle(noticeCommentReqDTO.getTitle());
        noticeComment.setContent(noticeCommentReqDTO.getContent());

        noticeCommentRepository.save(noticeComment);

        NoticeCommentResDTO noticeCommentResDTO = getNoticeCommentResDTO(customerId, noticeComment);

        return APIResponse.success("notice comment", noticeCommentResDTO);
    }

    private NoticeCommentResDTO getNoticeCommentResDTO(String customerId, NoticeComment noticeComment) {
        NoticeCommentResDTO noticeCommentResDTO = new NoticeCommentResDTO();
        noticeCommentResDTO.setId(noticeComment.getId());
        noticeCommentResDTO.setCustomerId(customerId);
        noticeCommentResDTO.setTitle(noticeComment.getTitle());
        noticeCommentResDTO.setContent(noticeComment.getContent());
        noticeCommentResDTO.setCreatedDt(noticeComment.getCreatedDt());
        noticeCommentResDTO.setModifiedDt(noticeComment.getModifiedDt());
        return noticeCommentResDTO;
    }


    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }
}
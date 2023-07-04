package kr.casealot.shop.domain.notice.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.notice.comment.dto.CommentDetailDTO;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import kr.casealot.shop.domain.notice.dto.NoticeDetailDTO;
import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.repository.NoticeRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.NotFoundWriteException;
import kr.casealot.shop.global.exception.PermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CustomerRepository customerRepository;


    public APIResponse<List<NoticeResDTO>> getNoticeList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<Notice> noticeList = noticePage.getContent();

        List<NoticeResDTO> noticeResDTOList = new ArrayList<>();
        for (Notice notice : noticeList) {
            String customerId = notice.getCustomer().getId();
            NoticeResDTO noticeResDTO = NoticeResDTO.builder()
                    .id(notice.getId())
                    .customerId(customerId)
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .views(notice.getViews())
                    .createdDt(notice.getCreatedDt())
                    .modifiedDt(notice.getModifiedDt())
                    .build();

            noticeResDTOList.add(noticeResDTO);
        }

        return APIResponse.success("notice", noticeResDTOList);
    }

    @Transactional
    public APIResponse<NoticeDetailDTO> getNotice(Long noticeId, Principal principal) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice == null) {
            throw new NotFoundWriteException();
        }

        notice.setViews(notice.getViews() + 1);

        NoticeDetailDTO noticeDetailDTO = NoticeDetailDTO.builder()
                .id(notice.getId())
                .customerId(notice.getCustomer().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .views(notice.getViews())
                .createdDt(notice.getCreatedDt())
                .modifiedDt(notice.getModifiedDt())
                .build();

        List<NoticeComment> noticeCommentList = notice.getNoticeCommentList();
        List<CommentDetailDTO> commentDTOList = new ArrayList<>();


        for (NoticeComment noticeComment : noticeCommentList) {
            String available = "N";
            if (principal != null) {
                Customer customer = customerRepository.findById(principal.getName());
                if (Objects.equals(customer.getSeq(), noticeComment.getCustomer().getSeq())) {
                    available = "Y"; // 로그인한 사용자가 작성한 글인 경우 수정, 삭제 가능으로 설정
                }
            }
            CommentDetailDTO commentDetailDTO = CommentDetailDTO.builder()
                    .id(noticeComment.getId())
                    .customerId(noticeComment.getCustomer().getId())
                    .title(noticeComment.getTitle())
                    .content(noticeComment.getContent())
                    .available(available)
                    .createdDt(noticeComment.getCreatedDt())
                    .modifiedDt(noticeComment.getModifiedDt())
                    .build();

            commentDTOList.add(commentDetailDTO);
        }

        noticeDetailDTO.setNoticeCommentList(commentDTOList);

        return APIResponse.success("notice", noticeDetailDTO);
    }


    @Transactional
    public APIResponse<NoticeResDTO> createNotice(NoticeReqDTO noticeReqDTO,
                                                  Principal principal) {

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new PermissionException();
        }

        Customer customer = customerRepository.findById(customerId);

        Notice notice = Notice.builder()
                .title(noticeReqDTO.getTitle())
                .content(noticeReqDTO.getContent())
                .customer(customer)
                .build();

        noticeRepository.save(notice);

        NoticeResDTO noticeResDTO = getNoticeResDTO(notice, customerId);

        return APIResponse.success("notice", noticeResDTO);
    }

    @Transactional
    public APIResponse<NoticeResDTO> updateNotice(Long noticeId,
                                                  NoticeReqDTO noticeReqDTO,
                                                  Principal principal) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice == null) {
            throw new NotFoundWriteException();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new PermissionException();
        }

        notice.setTitle(noticeReqDTO.getTitle());
        notice.setContent(noticeReqDTO.getContent());

        noticeRepository.save(notice);

        NoticeResDTO noticeResDTO = getNoticeResDTO(notice, customerId);

        return APIResponse.success("notice", noticeResDTO);
    }


    @Transactional
    public APIResponse<NoticeResDTO> deleteNotice(Long noticeId,
                                                  Principal principal) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);

        if (notice == null) {
            throw new NotFoundWriteException();
        }

        String customerId = principal.getName();

        boolean isAdmin = checkAdminRole(customerId);

        if (!isAdmin) {
            throw new PermissionException();
        }

        noticeRepository.delete(notice);

        NoticeResDTO noticeResDTO = getNoticeResDTO(notice, customerId);

        return APIResponse.success("notice", noticeResDTO);
    }

    private NoticeResDTO getNoticeResDTO(Notice notice, String customerId) {
        NoticeResDTO noticeResDTO = new NoticeResDTO();
        noticeResDTO.setId(notice.getId());
        noticeResDTO.setCustomerId(customerId);
        noticeResDTO.setTitle(notice.getTitle());
        noticeResDTO.setContent(notice.getContent());
        noticeResDTO.setViews(notice.getViews());
        noticeResDTO.setCreatedDt(notice.getCreatedDt());
        noticeResDTO.setModifiedDt(notice.getModifiedDt());
        return noticeResDTO;
    }


    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getRoleType() == ADMIN;
    }
}
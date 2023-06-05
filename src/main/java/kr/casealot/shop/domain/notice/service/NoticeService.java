package kr.casealot.shop.domain.notice.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import kr.casealot.shop.domain.notice.dto.NoticeDetailDTO;
import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.repository.NoticeRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final CustomerRepository customerRepository;

    private final AuthTokenProvider authTokenProvider;


    public APIResponse<List<NoticeResDTO>> getNoticeList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<Notice> noticeList = noticePage.getContent();

        List<NoticeResDTO> noticeResDTOList = new ArrayList<>();
        for(Notice notice : noticeList){
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
    public APIResponse<NoticeDetailDTO> getNotice(Long noticeId){
        Notice notice = noticeRepository.findById(noticeId) .orElseThrow();

        notice.setViews(notice.getViews() + 1);

        NoticeDetailDTO noticeDetailDTO = NoticeDetailDTO.builder()
                .id(notice.getId())
                .customerId(notice.getCustomer().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .views(notice.getViews())
                .build();


        List<NoticeComment> noticeCommentList = notice.getNoticeCommentList();
        List<NoticeCommentResDTO> commentDTOList = new ArrayList<>();

        for (NoticeComment noticeComment : noticeCommentList) {
            NoticeCommentResDTO noticeCommentResDTO = NoticeCommentResDTO.builder()
                    .id(noticeComment.getId())
                    .customerId(noticeComment.getCustomer().getId())
                    .title(noticeComment.getTitle())
                    .content(noticeComment.getContent())
                    .createdDt(noticeComment.getCreatedDt())
                    .modifiedDt(noticeComment.getModifiedDt())
                    .build();

            commentDTOList.add(noticeCommentResDTO);
        }

        noticeDetailDTO.setNoticeCommentList(commentDTOList);

        return APIResponse.success("notice", noticeDetailDTO);
    }


    @Transactional
    public APIResponse<NoticeResDTO> createNotice(NoticeReqDTO noticeReqDTO, HttpServletRequest request) {

        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin){
            return APIResponse.permissionDenied();
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
    public APIResponse<NoticeResDTO> updateNotice(Long noticeId, NoticeReqDTO noticeReqDTO, HttpServletRequest request){
        Notice notice = noticeRepository.findById(noticeId).orElseThrow();

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin){
            return APIResponse.permissionDenied();
        }

        notice.setTitle(noticeReqDTO.getTitle());
        notice.setContent(noticeReqDTO.getContent());

        noticeRepository.save(notice);

        NoticeResDTO noticeResDTO = getNoticeResDTO(notice, customerId);

        return APIResponse.success("notice", noticeResDTO);
    }


    @Transactional
    public APIResponse<NoticeResDTO> deleteNotice(Long noticeId, HttpServletRequest request){
        Notice notice = noticeRepository.findById(noticeId).orElseThrow();

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin){
            return APIResponse.permissionDenied();
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

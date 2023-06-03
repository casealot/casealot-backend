package kr.casealot.shop.domain.notice.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentReqDTO;
import kr.casealot.shop.domain.notice.comment.dto.NoticeCommentResDTO;
import kr.casealot.shop.domain.notice.comment.entity.NoticeComment;
import kr.casealot.shop.domain.notice.dto.NoticeDetailDTO;
import kr.casealot.shop.domain.notice.dto.NoticeReqDTO;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import org.springframework.data.domain.Pageable;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.notice.dto.NoticeResDTO;
import kr.casealot.shop.domain.notice.entity.Notice;
import kr.casealot.shop.domain.notice.repository.NoticeRepository;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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



    public List<NoticeResDTO> getNoticeList(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        List<Notice> noticeList = noticePage.getContent();

        List<NoticeResDTO> noticeResDTOList = new ArrayList<>();
        for(Notice notice : noticeList){
            NoticeResDTO noticeResDTO = NoticeResDTO.builder()
                    .id(notice.getId())
                    .customerId(notice.getCustomer().getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .photoUrl(notice.getPhotoUrl())
                    .views(notice.getViews())
                    .build();

            noticeResDTOList.add(noticeResDTO);
        }

        return noticeResDTOList;
    }


    public NoticeDetailDTO getNotice(Long noticeId) throws NotFoundException {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NotFoundException::new);

        notice.setViews(notice.getViews() + 1);

        NoticeDetailDTO noticeDetailDTO = NoticeDetailDTO.builder()
                .id(notice.getId())
                .customerId(notice.getCustomer().getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .photoUrl(notice.getPhotoUrl())
                .views(notice.getViews())
                .build();

        List<NoticeComment> noticeCommentList = notice.getNoticeCommentList();
        List<NoticeCommentResDTO> noticeCommentResDTOList = new ArrayList<>();

        for (NoticeComment noticeComment : noticeCommentList) {
            NoticeCommentResDTO noticeCommentResDTO = NoticeCommentResDTO.builder()
                    .id(noticeComment.getId())
                    .noticeId(noticeComment.getNotice().getId())
                    .customerId(noticeComment.getCustomer().getId())
                    .title(noticeComment.getTitle())
                    .content(noticeComment.getContent())
                    .build();

            noticeCommentResDTOList.add(noticeCommentResDTO);
        }

        noticeDetailDTO.setNoticeCommentList(noticeCommentResDTOList);

        return noticeDetailDTO;
    }


    @Transactional
    public void createNotice(NoticeReqDTO noticeReqDTO, HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin){
            throw new AccessDeniedException("권한 없음");
        }

        Customer customer = customerRepository.findById(customerId);

        Notice notice = Notice.builder()
                .title(noticeReqDTO.getTitle())
                .content(noticeReqDTO.getContent())
                .photoUrl(noticeReqDTO.getPhotoUrl())
                .customer(customer)
                .build();

        noticeRepository.save(notice);
    }

    @Transactional

    public void updateNotice(Long noticeId, NoticeReqDTO noticeReqDTO, HttpServletRequest request) throws NotFoundException {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NotFoundException::new);

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin){
            throw new AccessDeniedException("권한 없음");
        }

        notice.setTitle(noticeReqDTO.getTitle());
        notice.setContent(noticeReqDTO.getContent());
        notice.setPhotoUrl(noticeReqDTO.getPhotoUrl());

        noticeRepository.save(notice);
    }


    @Transactional
    public void deleteNotice(Long noticeId, HttpServletRequest request) throws NotFoundException {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(NotFoundException::new);

        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if(!isAdmin){
            throw new AccessDeniedException("권한 없음");
        }

        noticeRepository.delete(notice);
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

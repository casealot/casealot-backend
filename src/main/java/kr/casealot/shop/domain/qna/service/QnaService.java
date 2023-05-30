package kr.casealot.shop.domain.qna.service;

import io.jsonwebtoken.Claims;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.qna.comment.dto.QnaCommentDTO;
import kr.casealot.shop.domain.qna.comment.entity.QnaComment;
import kr.casealot.shop.domain.qna.comment.repository.QnaCommentRepository;
import kr.casealot.shop.domain.qna.dto.QnaDTO;
import kr.casealot.shop.domain.qna.dto.QnaDetailDTO;
import kr.casealot.shop.domain.qna.entity.Qna;
import kr.casealot.shop.domain.qna.repository.QnaRepository;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static kr.casealot.shop.global.oauth.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final CustomerRepository customerRepository;
    private final QnaCommentRepository qnaCommentRepository;
    private final AuthTokenProvider authTokenProvider;

//     qna 등록
//    @Transactional
//    public Qna createQna(CreateQna qnaDTO){
//        Qna qna = Qna.builder()
//                .title(qnaDTO.getTitle())
//                .content(qnaDTO.getContent())
//                .photoUrl(qnaDTO.getPhotoUrl())
//                .registrationDate(LocalDateTime.now())
//                .modificationDate(LocalDateTime.now())
//                .build();
//        return qnaRepository.save(qna);
//    }


//    @Transactional
//    public QnaDTO createQna(String title, String content, String photoUrl) {
//        return QnaDTO.fromEntity(qnaRepository.save(
//                Qna.builder()
//                        .title(title)
//                        .content(content)
//                        .photoUrl(photoUrl)
//                        .registrationDate(LocalDateTime.now())
//                        .modificationDate(LocalDateTime.now())
//                        .build())
//        );
//    }

    @Transactional
    public QnaDTO createQna(QnaDTO qnaDTO, HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        String customerId = claims.getSubject();

        Customer customer = customerRepository.findById(customerId);

        Qna qna = Qna.builder()
                .title(qnaDTO.getTitle())
                .content(qnaDTO.getContent())
                .photoUrl(qnaDTO.getPhotoUrl())
                .customer(customer)
                .build();

        Qna savedQna = qnaRepository.save(qna);

        return QnaDTO.builder()
                .id(savedQna.getId())
                .customerId(savedQna.getCustomer().getId())
                .title(savedQna.getTitle())
                .content(savedQna.getContent())
                .photoUrl(savedQna.getPhotoUrl())
                .build();
    }


    // qna 수정
    @Transactional
    public QnaDTO updateQna(Long qnaId, QnaDTO qnaDTO, HttpServletRequest request) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);

        String customerId = findCustomerId(request);
        if(customerId.equals(qna.getCustomer().getId())) {
            qna.setTitle(qnaDTO.getTitle());
            qna.setContent(qnaDTO.getContent());
            qna.setPhotoUrl(qnaDTO.getPhotoUrl());

            qnaRepository.save(qna);

            return QnaDTO.builder()
                    .id(qna.getId())
                    .customerId(qna.getCustomer().getId())
                    .title(qna.getTitle())
                    .content(qna.getContent())
                    .photoUrl(qna.getPhotoUrl())
                    .views(qna.getViews())
                    .build();
        }else{
            throw new NotFoundException();
        }
    }

    // qna 조회
    @Transactional
    public QnaDetailDTO getQna(Long qnaId) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);

        // 조회수 증가
        qna.setViews(qna.getViews() + 1);

        QnaDetailDTO qnaDetailDTO = QnaDetailDTO.builder()
                .id(qna.getId())
                .customerId(qna.getCustomer().getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .photoUrl(qna.getPhotoUrl())
                .views(qna.getViews())
                .build();

        List<QnaComment> qnaCommentList = qna.getQnaCommentList();
        List<QnaCommentDTO> qnaCommentDTOList = new ArrayList<>();

        for (QnaComment qnaComment : qnaCommentList) {
            QnaCommentDTO qnaCommentDTO = QnaCommentDTO.builder()
                    .id(qnaComment.getId())
                    .qnaId(qnaComment.getQna().getId())
                    .customerId(qnaComment.getCustomer().getId())
                    .title(qnaComment.getTitle())
                    .content(qnaComment.getContent())
                    .build();

            qnaCommentDTOList.add(qnaCommentDTO);
        }

        qnaDetailDTO.setQnaCommentList(qnaCommentDTOList);

        return qnaDetailDTO;
    }


    // qna 삭제
    @Transactional
    public void deleteQna(Long qnaId, HttpServletRequest request) throws NotFoundException {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(NotFoundException::new);
        String customerId = findCustomerId(request);

        boolean isAdmin = checkAdminRole(customerId);

        if(customerId.equals(qna.getCustomer().getId()) || isAdmin){
            qnaRepository.delete(qna);
        }else{

        }
    }

    // qna 목록
    @Transactional
    public List<QnaDTO> getQnaList(Pageable pageable) {
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        List<Qna> qnaList = qnaPage.getContent();

        List<QnaDTO> qnaDtoList = new ArrayList<>();
        for (Qna qna : qnaList) {
            QnaDTO qnaDTO = new QnaDTO();
            qnaDTO.setId(qna.getId());
            qnaDTO.setCustomerId(qna.getCustomer().getId());
            qnaDTO.setTitle(qna.getTitle());
            qnaDTO.setContent(qna.getContent());
            qnaDTO.setPhotoUrl(qna.getPhotoUrl());
            qnaDTO.setViews(qna.getViews());
            qnaDtoList.add(qnaDTO);
        }

        return qnaDtoList;
    }

    private String findCustomerId(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(token);
        Claims claims = authToken.getTokenClaims();
        return claims.getSubject();
    }

    private boolean checkAdminRole(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        if(customer.getRoleType() == ADMIN){
            return true;
        }else{
            return false;
        }
    }

}

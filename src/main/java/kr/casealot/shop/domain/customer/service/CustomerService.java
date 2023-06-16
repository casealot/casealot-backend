package kr.casealot.shop.domain.customer.service;

import com.amazonaws.services.kms.model.NotFoundException;
import io.jsonwebtoken.Claims;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.casealot.shop.domain.auth.entity.BlacklistToken;
import kr.casealot.shop.domain.auth.entity.CustomerRefreshToken;
import kr.casealot.shop.domain.auth.repository.BlacklistTokenRepository;
import kr.casealot.shop.domain.auth.repository.CustomerRefreshTokenRepository;
import kr.casealot.shop.domain.cart.entity.Cart;
import kr.casealot.shop.domain.cart.repository.CartRepository;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.dto.CustomerMapper;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.dto.CustomerUpdateDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.file.entity.UploadFile;
import kr.casealot.shop.domain.file.service.S3UploadService;
import kr.casealot.shop.domain.file.service.UploadFileService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.config.properties.AppProperties;
import kr.casealot.shop.global.exception.DuplicateEmailException;
import kr.casealot.shop.global.exception.DuplicateIdException;
import kr.casealot.shop.global.exception.IncorrectPasswordException;
import kr.casealot.shop.global.exception.NotFoundUserException;
import kr.casealot.shop.global.oauth.entity.RoleType;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.CookieUtil;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  private final String API_NAME = "customer";

  private final CustomerRepository customerRepository;

  private final AppProperties appProperties;
  private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final CartRepository cartRepository;
  private final AuthTokenProvider authTokenProvider;
  private final AuthTokenProvider tokenProvider;
  private final BlacklistTokenRepository blacklistTokenRepository;
  private final S3UploadService s3UploadService;
  private final UploadFileService uploadFileService;
  private final CustomerMapper customerMapper;
  private final static String REFRESH_TOKEN = "refresh_token";

  public APIResponse<String> join(CustomerDto customerDto)
      throws DuplicateEmailException, DuplicateIdException {
    String encodedPassword = passwordEncoder.encode(customerDto.getPassword());

    // 아이디 중복 확인
    if (customerRepository.existsCustomerById(customerDto.getId())) {
      throw new DuplicateIdException();
    }

    // 이메일 중복 확인
    if (customerRepository.existsByEmail(customerDto.getEmail())) {
      throw new DuplicateEmailException();
    }

    Customer customer = Customer.builder()
        .id(customerDto.getId())
        .name(customerDto.getName())
        .password(encodedPassword)
        .phoneNumber(customerDto.getPhoneNumber())
        .email(customerDto.getEmail())
        .profileImageUrl(customerDto.getProfileImageUrl())
        .address(customerDto.getAddress())
        .addressDetail(customerDto.getAddressDetail())
        .roleType(RoleType.USER)
        .build();

    customerRepository.save(customer);

    return APIResponse.success(API_NAME, customer.getId() + " join success!");
  }

  public APIResponse<CustomerTokenDto> login(CustomerLoginDto customerLoginDto
      , HttpServletRequest request
      , HttpServletResponse response) {
    Customer customer = customerRepository.findById(customerLoginDto.getId());

    if (customer == null) {
      throw new NotFoundUserException();
    }

    // 비밀번호 일치 확인
    if (!passwordEncoder.matches(customerLoginDto.getPassword(), customer.getPassword())) {
      throw new IncorrectPasswordException();
    }

    RoleType roleType = customer.getRoleType();

    Date now = new Date();
    // 토큰 생성 (jwt)
    // 토큰 유효 기간 설정 (30분 후)
    //long jwtExpiry = now.getTime() + appProperties.getAuth().getTokenExpiry() + (60 * 60 * 24 * 30); //1달로 설정
    long jwtExpiry = now.getTime() + 1000 * 60 * 60; // 1시간

    AuthToken authToken = authTokenProvider.createAuthToken(
        customer.getId(),
        roleType.getCode(),
        new Date(jwtExpiry));

    String accessToken = authToken.getToken();

    // userId refresh token 으로 DB 확인
    CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findById(
        customer.getId());

    long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

    AuthToken refreshToken = null;
    if (customerRefreshToken != null) {
      refreshToken = authTokenProvider.convertAuthToken(customerRefreshToken.getRefreshToken());
      // refresh 토큰이 유효 됐을 경우 다시 재발급 한다.
      if (refreshToken.validate()) {
        refreshToken = tokenProvider.createAuthToken(
            appProperties.getAuth().getTokenSecret(),
            new Date(now.getTime() + refreshTokenExpiry)
        );
        customerRefreshToken.setRefreshToken(refreshToken.getToken());
        customerRefreshTokenRepository.saveAndFlush(customerRefreshToken);
      }
    } else {
      // 없을 경우 새로 발급한다.
      refreshToken = tokenProvider.createAuthToken(
          appProperties.getAuth().getTokenSecret(),
          new Date(now.getTime() + refreshTokenExpiry)
      );
      customerRefreshToken = new CustomerRefreshToken(customer.getId(), refreshToken.getToken());
      customerRefreshTokenRepository.saveAndFlush(customerRefreshToken);
    }

    int cookieMaxAge = (int) refreshTokenExpiry / 60;
    CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
    CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

    //로그인시 카트 없으면 생성 후 db에 저장
    Cart cart = cartRepository.findByCustomerId(customer.getId());
    if (cart == null) {
      cart = Cart.createCart(customer);
      cartRepository.save(cart);
    }

    return APIResponse.success("customerToken",
        new CustomerTokenDto(customer.getId(), accessToken, customerRefreshToken.getRefreshToken(),
            customer.getRoleType()));
  }

  @Transactional
  public APIResponse<String> logout(HttpServletRequest request) {
    String token = HeaderUtil.getAccessToken(request);

    AuthToken authToken = authTokenProvider.convertAuthToken(token);

    Claims claims = authToken.getTokenClaims();
    String userId = claims.getSubject();

    System.out.println("ID: " + userId);

    // 이미 블랙리스트에 등록된 토큰인 경우 삭제하도록 처리
    BlacklistToken existingToken = blacklistTokenRepository.findByBlacklistToken(token);
    if (existingToken != null) {
      blacklistTokenRepository.delete(existingToken);
    }

    // AccessToken을 블랙리스트에 추가
    BlacklistToken blacklistToken = new BlacklistToken();
    blacklistToken.setId(userId);
    blacklistToken.setBlacklistToken(token);
    blacklistTokenRepository.save(blacklistToken);

    return APIResponse.success("customerId", userId + " user logout!");
  }


  @Transactional
  public APIResponse<String> deleteCustomer(HttpServletRequest request, Principal principal) {
    String token = HeaderUtil.getAccessToken(request);

    AuthToken authToken = authTokenProvider.convertAuthToken(token);
    CustomerRefreshToken customerRefreshToken = customerRefreshTokenRepository.findById(
        principal.getName());

    Claims claims = authToken.getTokenClaims();
    String userId = claims.getSubject();
    customerRefreshTokenRepository.deleteById(customerRefreshToken.getRefreshTokenSeq());
    customerRepository.deleteById(principal.getName());
    log.info("ID: " + principal.getName() + "quit!");

    BlacklistToken blacklistToken = new BlacklistToken();
    blacklistToken.setId(userId);
    blacklistToken.setBlacklistToken(token);
    blacklistTokenRepository.save(blacklistToken);

    return APIResponse.success("customerId", principal.getName() + " user deleted !");
  }


  @Transactional
  public APIResponse modifyProfileWithImage(String id, MultipartFile profileImgFile)
      throws Exception {
    //회원 불러옴
    Customer savedCustomer = customerRepository.findById(id);

    UploadFile profileImg = null;
    if (null != profileImgFile) {
      //기존에 이미지가 있다면 삭제
      UploadFile savedCustomerProfileImg = savedCustomer.getProfileImg();
      if (null != savedCustomerProfileImg) {
        s3UploadService.deleteFileFromS3Bucket(savedCustomerProfileImg.getUrl());
        uploadFileService.delete(savedCustomerProfileImg);
      }

      String path = s3UploadService.uploadFile(profileImgFile);
      profileImg = uploadFileService.create(UploadFile.builder()
          .name(profileImgFile.getOriginalFilename())
          .url(path)
          .fileType(profileImgFile.getContentType())
          .fileSize(profileImgFile.getSize())
          .build());
      savedCustomer.setProfileImg(profileImg);
    }
    return APIResponse.success(API_NAME, savedCustomer);
  }

  @Transactional
  public APIResponse<Customer> updateCustomer(Principal principal,
      CustomerUpdateDto customerUpdateDto)
      throws Exception {

    Customer updatedCustomer = customerRepository.saveAndFlush(
        customerMapper.updateRequestDTOToEntity(principal.getName(), customerUpdateDto));
//    String customerId = principal.getName();
//    Optional<Customer> optionalCustomer = Optional.ofNullable(
//        customerRepository.findById(customerId));
//
//    if (optionalCustomer.isEmpty()) {
//      throw new NotFoundException("해당 아이디로 저장된 사용자가 없습니다.");
//    }
//
//    Customer savedCustomer = optionalCustomer.get();
//
//    // 변경된 이메일이 이미 존재하는지 확인
//    if (!savedCustomer.getEmail().equals(customerUpdateDto.getEmail()) &&
//        customerRepository.existsByEmail(customerUpdateDto.getEmail())) {
//      throw new DuplicateEmailException();
//    }
//
//    savedCustomer.setEmail(customerUpdateDto.getEmail());
//    savedCustomer.setName(customerUpdateDto.getName());
//    savedCustomer.setPhoneNumber(customerUpdateDto.getPhoneNumber());
//    savedCustomer.setAddress(customerUpdateDto.getAddress());
//    savedCustomer.setAddressDetail(customerUpdateDto.getAddressDetail());
//    savedCustomer.setProfileImageUrl(optionalCustomer.get().getProfileImageUrl());

//    Customer updatedCustomer = customerRepository.save(savedCustomer);
    return APIResponse.success(API_NAME, updatedCustomer);
  }
}
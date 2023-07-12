package kr.casealot.shop.domain.customer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerIdFindDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.dto.CustomerPasswordFindDto;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.dto.CustomerUpdateDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.domain.function.dto.MyPageDTO;
import kr.casealot.shop.domain.function.service.FunctionService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.domain.customer.exception.DuplicateEmailException;
import kr.casealot.shop.domain.customer.exception.DuplicateIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CUSTOMER API"}, description = "유저 관련 API")
@RequestMapping("/cal/v1/customer")
public class CustomerController {

  private final CustomerService customerService;
  private final FunctionService functionService;

  @PostMapping("/join")
  @ApiOperation(value = "회원 가입", notes = "회원 가입을 한다.")
  public APIResponse<String> join(
      @ApiParam(value = "회원 가입 요청 DTO") @RequestBody CustomerDto customerDto)
      throws DuplicateEmailException, DuplicateIdException {
    return customerService.join(customerDto);
  }

  @PostMapping("/login")
  @ApiOperation(value = "로그인", notes = "로그인을 한다.")
  public APIResponse<CustomerTokenDto> login(
      @ApiParam(value = "로그인 요청 DTO") @RequestBody CustomerLoginDto customerLoginDto,
      HttpServletRequest request,
      HttpServletResponse response) {
    return customerService.login(customerLoginDto, request, response);
  }

  @DeleteMapping("/logout")
  @ApiOperation(value = "로그아웃", notes = "로그아웃을 한다.")
  public APIResponse<String> logout(HttpServletRequest request) {
    return customerService.logout(request);
  }

  @DeleteMapping("/quit")
  @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴를 한다.")
  public APIResponse<String> quit(HttpServletRequest request, Principal principal) {
    return customerService.deleteCustomer(request, principal);
  }

  @PutMapping("/update")
  @ApiOperation(value = "회원 수정", notes = "회원 수정을 한다.")
  public APIResponse<Customer> update(Principal principal,
      @ApiParam(value = "회원 수정 요청 DTO") @RequestBody CustomerUpdateDto updateRequest)
      throws Exception {
    return customerService.updateCustomer(principal, updateRequest);
  }

  @GetMapping
  @ApiOperation(value = "회원 정보", notes = "회원 정보를 보여준다.")
  public APIResponse<CustomerDto> info(Principal principal) {
    return customerService.customerInfo(principal);
  }

  @PostMapping("/password")
  @ApiOperation(value = "비밀번호 찾기(변경)", notes = "ID, 이름, Eamil, 휴대폰 번호 일치하는 사용자를 찾아 비밀번호를 변경시킨다.")
  public APIResponse<String> findPassword(
      @ApiParam(value = "비밀번호 변경을 위한 개인정보 요청 DTO") @RequestBody CustomerPasswordFindDto customerPasswordFindDto,
      @ApiParam(value = "새로운 비밀번호") @RequestParam(required = false) String newPassword) {
    return customerService.findPassword(customerPasswordFindDto, newPassword);
  }

  @PostMapping("/id")
  @ApiOperation(value = "아이디 찾기(변경)", notes = "이름, Eamil 일치하는 사용자를 찾아 아이디를 알려준다.")
  public APIResponse<String> findId(
      @ApiParam(value = "아이디 변경을 위한 개인정보 요청 DTO") @RequestBody CustomerIdFindDto customerIdFindDto) {
    return customerService.findId(customerIdFindDto);
  }

  @GetMapping("/mypage")
  @ApiOperation(value = "마이페이지 프로필 사진 및 배송상태", notes = "나의 프로필 사진과, 내가 주문한 상품들의 배송상태를 볼 수 있게 한다.")
  public APIResponse<MyPageDTO> getMyPageData(Principal principal) {
    return functionService.getMyPageFunction(principal);
  }
}

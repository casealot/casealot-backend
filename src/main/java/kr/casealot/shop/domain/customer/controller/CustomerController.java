package kr.casealot.shop.domain.customer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.dto.CustomerUpdateDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.service.CustomerService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CUSTOMER API"}, description = "유저 관련 API")
@RequestMapping("/cal/v1/customer")
public class CustomerController {

  private final CustomerService customerService;

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
}

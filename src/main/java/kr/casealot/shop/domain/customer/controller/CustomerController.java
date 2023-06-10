package kr.casealot.shop.domain.customer.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.common.APIResponse;
import kr.casealot.shop.global.exception.DuplicateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CUSTOMER API"}, description = "유저 관련 API")
@RequestMapping("/cal/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/join")
    public APIResponse<String> join(@RequestBody CustomerDto customerDto) throws DuplicateException {
        return customerService.join(customerDto);
    }

    @PostMapping("/login")
    public APIResponse<CustomerTokenDto> login(@RequestBody CustomerLoginDto customerLoginDto
            , HttpServletRequest request
            , HttpServletResponse response) {
        return customerService.login(customerLoginDto, request, response);
    }

    @DeleteMapping("/logout")
    public APIResponse<String> logout(HttpServletRequest request) {
        return customerService.logout(request);
    }

    @DeleteMapping("/quit")
    public APIResponse<String> quit(HttpServletRequest request, Principal principal) {
        return customerService.deleteCustomer(request, principal);
    }
}

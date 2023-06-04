package kr.casealot.shop.domain.customer.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.dto.CustomerTokenDto;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"CUSTOMER API"}, description = "유저 관련 API")
@RequestMapping("/cal/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/join")
    public APIResponse<Long> join(@RequestBody CustomerDto customerDto) {
        return customerService.join(customerDto);
    }

    @PostMapping("/login")
    public APIResponse<CustomerTokenDto> login(@RequestBody CustomerLoginDto customerLoginDto) {
        return customerService.login(customerLoginDto);
    }

    @PostMapping("/logout")
    public APIResponse<String> logout(HttpServletRequest request) {
        return customerService.logout(request);
    }

    @DeleteMapping("/quit")
    public APIResponse<String> quit(HttpServletRequest request) {
        return customerService.deleteCustomer(request);
    }
}

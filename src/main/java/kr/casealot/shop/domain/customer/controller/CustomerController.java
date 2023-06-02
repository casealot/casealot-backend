package kr.casealot.shop.domain.customer.controller;

import io.swagger.annotations.Api;
import kr.casealot.shop.domain.auth.entity.CustomerToken;
import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.dto.CustomerLoginDto;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.oauth.token.AuthToken;
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
    public ResponseEntity<String> join(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.join(customerDto) + " user join!");
    }

    @PostMapping("/login")
    public CustomerToken login(@RequestBody CustomerLoginDto customerLoginDto) {
        return customerService.login(customerLoginDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        customerService.logout(request);

        return ResponseEntity.ok("logout okay");
    }

    @DeleteMapping("/quit")
    public ResponseEntity<String> quit(HttpServletRequest request) {
        customerService.deleteCustomer(request);

        return ResponseEntity.ok("quit okay");
    }
}

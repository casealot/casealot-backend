package kr.casealot.shop.domain.customer.controller;

import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.oauth.token.AuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.join(customerDto) + " user join!");
    }

    @PostMapping("/login")
    public AuthToken login(@RequestBody CustomerDto customerDto) {
        return customerService.login(customerDto);
    }
}

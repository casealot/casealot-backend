package kr.casealot.shop.domain.customer.controller;

import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.customer.service.CustomerService;
import kr.casealot.shop.global.oauth.token.AuthToken;
import kr.casealot.shop.global.oauth.token.AuthTokenProvider;
import kr.casealot.shop.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final AuthTokenProvider authTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.join(customerDto) + " user join!");
    }

    @PostMapping("/login")
    public AuthToken login(@RequestBody CustomerDto customerDto) {
        return customerService.login(customerDto);
    }

    @DeleteMapping("/quit")
    public ResponseEntity<String> quit(HttpServletRequest request) {
        String token = HeaderUtil.getAccessToken(request);

        String id = authTokenProvider.extractIdFromToken(token);
        System.out.println("ID: " + id);
        // Delete the user by ID
//        boolean deleted = customerRepository.deleteById(authTokenProvider.getUserIdFromToken(token));
//        if (deleted) {
//            return ResponseEntity.ok("User deleted successfully.");
//        } else {
        return ResponseEntity.ok(id);
    }
}

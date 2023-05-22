package kr.casealot.shop.domain.customer.controller;

import kr.casealot.shop.domain.customer.dto.CustomerDto;
import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cal/v1/customer")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @PostMapping("/join")
    public Long join(@RequestBody CustomerDto customerDto){
        return customerService.join(customerDto);
    }
}

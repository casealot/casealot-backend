package kr.casealot.shop.domain.customer.service;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.oauth.entity.ProviderType;
import kr.casealot.shop.global.oauth.entity.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public Customer signup(Customer customer) {
        Customer saveCustomer = Customer.builder()
                .id(customer.getId())
                .password(passwordEncoder.encode(customer.getPassword()))
                .email(customer.getEmail())
                .emailVerifiedYn("Y")
                .profileImageUrl("")
                .providerType(ProviderType.LOCAL)
                .roleType(RoleType.USER)
                .postNo(customer.getPostNo())
                .address(customer.getAddress())
                .addressDetail(customer.getAddressDetail()).build();
        customerRepository.save(saveCustomer);
        return customer;
    }

    public Customer login(Customer customer) {
        Customer savedCustomer = new Customer(
                customer.getId(),
                passwordEncoder.encode(customer.getPassword())
        );
        return customerRepository.findByIdAndPassword(customer.getId()
                , passwordEncoder.encode(customer.getPassword()));
    }
}

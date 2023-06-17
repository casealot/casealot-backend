package kr.casealot.shop.domain.customer.dto;

import kr.casealot.shop.domain.customer.entity.Customer;
import kr.casealot.shop.domain.customer.repository.CustomerRepository;
import kr.casealot.shop.global.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
  private final CustomerRepository customerRepository;
  public Customer updateRequestDTOToEntity(String customerId, CustomerUpdateDto request)
      throws DuplicateEmailException {
    Customer savedCustomer = customerRepository.findCustomerById(customerId);

    // 중복된 값을 방지하기 위해 이메일 중복 체크 로직 추가
    if (!savedCustomer.getEmail().equals(request.getEmail()) &&
        customerRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateEmailException();
    }

    savedCustomer.setEmail(request.getEmail());
    savedCustomer.setName(request.getName());
    savedCustomer.setPhoneNumber(request.getPhoneNumber());
    savedCustomer.setAddress(request.getAddress());
    savedCustomer.setAddressDetail(request.getAddressDetail());
    savedCustomer.setProfileImageUrl(savedCustomer.getProfileImageUrl());
    return savedCustomer;
  }

}

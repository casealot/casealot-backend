//package kr.casealot.shop.domain.customer.dto;
//
//import kr.casealot.shop.domain.customer.entity.Customer;
//import kr.casealot.shop.domain.customer.repository.CustomerRepository;
//import kr.casealot.shop.global.exception.DuplicateEmailException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//

//TODO:이용했던 방법 안되면 사용 예정

//@Component
//@RequiredArgsConstructor
//public class CustomerMapper {
//  private final CustomerRepository customerRepository;
//  public Customer updateRequestDTOToEntity(String customerId, CustomerUpdateDto request)
//      throws DuplicateEmailException {
//    Customer savedCustomer = customerRepository.findCustomerById(customerId);
//
//    // 중복된 값을 방지하기 위해 이메일 중복 체크 로직 추가
//    if (!savedCustomer.getEmail().equals(request.getEmail()) &&
//        customerRepository.existsByEmail(request.getEmail())) {
//      throw new DuplicateEmailException();
//    }
//
//    Customer updatedCustomer = Customer.builder()
//        .id(savedCustomer.getId())
//        .email(request.getEmail())
//        .password(savedCustomer.getPassword())
//        .name(request.getName())
//        .phoneNumber(request.getPhoneNumber())
//        .address(request.getAddress())
//        .providerType(savedCustomer.getProviderType())
//        .roleType(savedCustomer.getRoleType())
//        .addressDetail(request.getAddressDetail())
//        .profileImageUrl(savedCustomer.getProfileImageUrl())
//        .build();
//    return updatedCustomer;
//  }
//
//}

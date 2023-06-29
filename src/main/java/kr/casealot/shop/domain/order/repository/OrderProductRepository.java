package kr.casealot.shop.domain.order.repository;

import java.util.List;
import kr.casealot.shop.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {


  List<OrderProduct> findAllByOrderId(Long orderId);
//오더id 프로덕트id 커스터머seq

  boolean existsByProductIdAndCustomerSeq(Long productId, Long CustomerSeq);
}

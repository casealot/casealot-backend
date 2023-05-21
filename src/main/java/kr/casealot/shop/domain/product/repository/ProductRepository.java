package kr.casealot.shop.domain.product.repository;

import kr.casealot.shop.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(String id);
    Product deleteById(String id);
}

package kr.casealot.shop.domain.product.repository;

import kr.casealot.shop.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Page<Product> findAll(Pageable pageable);

    Product findByName(String name);

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);
}

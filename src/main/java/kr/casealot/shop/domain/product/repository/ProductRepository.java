package kr.casealot.shop.domain.product.repository;

import kr.casealot.shop.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Page<Product> findAll(Pageable pageable);

    Product findByName(String name);

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    @Query("SELECT distinct p.name FROM Product p")
    List<String> findProductNames();

    List<Product> findByCreatedDtBefore(LocalDateTime date);

    List<Product> findByCreatedDtAfter(LocalDateTime date);

    List<Product> findTop10ByOrderBySellsDesc();

    List<Product> findTop10ByOrderBySellsAsc();

    List<Product> findByType(String type);

    List<Product> findByCategory(String category);
}

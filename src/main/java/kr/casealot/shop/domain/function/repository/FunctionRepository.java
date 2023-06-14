package kr.casealot.shop.domain.function.repository;

import kr.casealot.shop.domain.function.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
}

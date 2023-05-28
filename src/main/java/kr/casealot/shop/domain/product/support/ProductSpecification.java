package kr.casealot.shop.domain.product.support;

import kr.casealot.shop.domain.product.dto.SearchFilter;
import kr.casealot.shop.domain.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 참고 문서
 * https://www.baeldung.com/rest-search-language-spring-jpa-criteria
 */
public class ProductSpecification implements Specification<Product> {
    private final String searchQuery;
    private final List<SearchFilter> criteriaList;

    public ProductSpecification(String query, List<SearchFilter> searchCriteria) {
        this.searchQuery = query;
        this.criteriaList = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> r, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.like(r.get("name"), "%" + searchQuery + "%"));
        for (SearchFilter criteria : criteriaList) {
            switch (criteria.getOperation()) {
                case ":": {
                    predicates.add(builder.equal(r.get(criteria.getKey()), criteria.getValue()));
                    break;
                }
                case "%": {
                    predicates.add(builder.like(r.get(criteria.getKey()), "%" + criteria.getValue() + "%"));
                    break;
                }
                case "<": {
                    predicates.add(builder.lessThanOrEqualTo(r.get(criteria.getKey()), criteria.getValue()));
                    break;
                }
                case ">": {
                    predicates.add(builder.greaterThanOrEqualTo(r.get(criteria.getKey()), criteria.getValue()));
                    break;
                }
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}

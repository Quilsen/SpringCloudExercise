package org.example.internalservice.repository;

import org.example.internalservice.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> buidlSpec(String name,
                                                       String description,
                                                       BigDecimal price) {
        Specification<Product> spec = Specification.where(null);

        if (StringUtils.hasText(name)) {
            spec = spec.and(nameContains(name));
        }
        if (StringUtils.hasText(description)) {
           spec = spec.and(descriptionContains(description));
        }
        if (price != null) {
            spec = spec.and(priceEqual(price));
        }
        return spec;
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),  "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> descriptionContains(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Product> priceEqual(BigDecimal price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("price"), price);
    }
}

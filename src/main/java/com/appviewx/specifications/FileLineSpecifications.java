package com.appviewx.specifications;

import com.appviewx.model.primarydb.FileLine;
import org.springframework.data.jpa.domain.Specification;

public class FileLineSpecifications {

    public static Specification<FileLine> column1LikeSpec(String column1PartStr) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("column1"), column1PartStr);
        };
    }


}

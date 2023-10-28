package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.filter.GlobalOperator;
import com.dihson103.onlinelearning.dto.filter.Operator;
import com.dihson103.onlinelearning.dto.filter.SearchRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dihson103.onlinelearning.dto.filter.GlobalOperator.AND;

@Service
public class FiltersSpecification<T> {

    public Specification<T> getSpecification(List<SearchRequestDto> searchRequestDtos, GlobalOperator globalOperator){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            searchRequestDtos.forEach(searchRequestDto -> {
                Predicate predicate;
                switch (searchRequestDto.getOperator()){
                    case EQUAL -> {
                        predicate = criteriaBuilder.equal(root.get(
                                searchRequestDto.getColumn()),
                                searchRequestDto.getValue()
                        );
                    }
                    case LIKE -> {
                        predicate = criteriaBuilder.like(root.get(
                                searchRequestDto.getColumn()),
                                "%" + searchRequestDto.getValue() + "%"
                        );
                    }
                    case IN -> {
                        String[] values = searchRequestDto.getValue().split("\\|");
                        values = Arrays.stream(values)
                                .map(String::trim)
                                .toArray(String[]::new);
                        predicate = root.get(searchRequestDto.getColumn()).in(Arrays.asList(values));
                    }
                    case GREATER_THAN -> {
                        predicate = criteriaBuilder.greaterThan(root.get(
                                searchRequestDto.getColumn()),
                                searchRequestDto.getValue()
                        );
                    }
                    case LESS_THAN -> {
                        predicate = criteriaBuilder.lessThan(root.get(
                                searchRequestDto.getColumn()),
                                searchRequestDto.getValue()
                        );
                    }
                    case BETWEEN -> {
                        String[] values = searchRequestDto.getValue().split("\\|");
                        Integer[] list = Arrays.stream(values)
                                .map(value -> Integer.parseInt(value.trim()))
                                .toArray(Integer[]::new);
                        predicate = criteriaBuilder.between(root.get(
                                searchRequestDto.getColumn()),
                                list[0],
                                list[1]
                        );
                    }
                    case JOIN -> {
                        predicate = criteriaBuilder.equal(root.join(
                                searchRequestDto.getJoinTable()).get(searchRequestDto.getColumn()),
                                searchRequestDto.getValue()
                        );
                    }
                    default -> throw new IllegalArgumentException("");
                }

                predicates.add(predicate);
            });

            if(globalOperator.equals(AND)){
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}

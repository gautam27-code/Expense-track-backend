package com.project.expensepilot.specification;

import com.project.expensepilot.model.Expense;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSpecification {

    public static Specification<Expense> getExpensesByUserAndFilters(
            int userId, String type, String category, String startDate, String endDate, String description) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (type != null && !type.isEmpty() && !type.equalsIgnoreCase("all")) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            if (category != null && !category.isEmpty() && !category.equalsIgnoreCase("all")) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            if (startDate != null && !startDate.isEmpty()) {
                try {
                    // Assuming the date is stored in a string format that can be compared directly.
                    // For "yyyy-MM-dd", string comparison should work for GreaterThanOrEqual.
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
                } catch (Exception e) {
                    // Log error or handle - for now, we ignore invalid date formats
                }
            }

            if (endDate != null && !endDate.isEmpty()) {
                try {
                    // Assuming the date is stored in a string format that can be compared directly.
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
                } catch (Exception e) {
                    // Log error or handle
                }
            }

            if (description != null && !description.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


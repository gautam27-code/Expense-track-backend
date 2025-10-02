package com.project.expensepilot.repo;

import com.project.expensepilot.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer>, JpaSpecificationExecutor<Expense> {
    List<Expense> findByUser_Id(int userId);
}

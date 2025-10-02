package com.project.expensepilot.service;

import com.project.expensepilot.model.Expense;
import com.project.expensepilot.model.UserEntity;
import com.project.expensepilot.repo.ExpenseRepo;
import com.project.expensepilot.repo.UserRepo;
import com.project.expensepilot.specification.ExpenseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private UserRepo userRepo;

    public void addExpense(Expense expense) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        expense.setUser(user);
        expenseRepo.save(expense);
    }

    public List<Expense> getAllExpenses(String type, String category, String startDate, String endDate, String description, String sortField, String sortOrder) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Specification<Expense> spec = ExpenseSpecification.getExpensesByUserAndFilters(user.getId(), type, category, startDate, endDate, description);

        Sort sort = Sort.by("date").descending(); // Default sort
        if (sortField != null && !sortField.isEmpty()) {
            Sort.Direction direction = (sortOrder != null && sortOrder.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, sortField);
        }

        return expenseRepo.findAll(spec, sort);
    }

    public void deleteExpense(int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Expense expense = expenseRepo.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));

        if (expense.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("You are not authorized to delete this expense");
        }
        expenseRepo.deleteById(id);
    }

    public Expense getExpenseById(int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Expense expense = expenseRepo.findById(id).orElse(null);

        if (expense != null && expense.getUser().getId() != user.getId()) {
            return null; // Or throw AccessDeniedException
        }
        return expense;
    }

    public Expense updateExpense(Expense updatedExpense) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Expense existingExpense = expenseRepo.findById(updatedExpense.getId()).orElseThrow(() -> new RuntimeException("Expense not found"));

        if (existingExpense.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("You are not authorized to update this expense");
        }
        updatedExpense.setUser(user);
        return expenseRepo.save(updatedExpense);
    }

//    public void deleteExpense(int id) {
//        expenses.removeIf(expense -> expense.getId() == id);
//    }
}
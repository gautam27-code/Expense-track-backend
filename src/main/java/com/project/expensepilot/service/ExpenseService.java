package com.project.expensepilot.service;

import com.project.expensepilot.model.Expense;
import com.project.expensepilot.repo.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    public void addExpense(Expense expense) {
        expenseRepo.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepo.findAll();
    }

    public void deleteExpense(int id) {
        expenseRepo.deleteById(id);
    }

    public Expense getExpenseById(int id) {
        return expenseRepo.findById(id).orElse(null);
    }

    public Expense updateExpense(Expense updatedExpense) {
        return expenseRepo.save(updatedExpense);
    }

//    public void deleteExpense(int id) {
//        expenses.removeIf(expense -> expense.getId() == id);
//    }
}
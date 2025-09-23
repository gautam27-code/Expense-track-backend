package com.project.expensepilot.controller;

import com.project.expensepilot.model.Expense;
import com.project.expensepilot.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/")
    public String greet(){
        return "Hello World! Welcome to my ExpensePilot";
    }

    @PostMapping("/expense")
    public void createExpense(@RequestBody Expense expense) {
        expenseService.addExpense(expense);
    }

    @GetMapping("/expenses")
    public Iterable<Expense> getExpenses() {
        return expenseService.getAllExpenses();
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable int id) {
        Expense existingExpense = expenseService.getExpenseById(id);
        if (existingExpense != null) {
            expenseService.deleteExpense(id);
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatus.valueOf(404));
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable int id, @RequestBody Expense updatedExpense) {
        Expense existingExpense = expenseService.getExpenseById(id);
        if (existingExpense != null) {
            updatedExpense.setId(id);
            Expense savedExpense = expenseService.updateExpense(updatedExpense);
            return new ResponseEntity<>(savedExpense, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    
}

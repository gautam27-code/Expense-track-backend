package com.project.expensepilot.controller;

import com.project.expensepilot.model.Expense;
import com.project.expensepilot.model.UserEntity;
import com.project.expensepilot.repo.UserRepo;
import com.project.expensepilot.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String greet(){
        return "Hello World! Welcome to my ExpensePilot";
    }

    @PostMapping("/expense")
    public ResponseEntity<?> createExpense(@RequestBody Expense expense) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            expense.setUser(user);
            expenseService.addExpense(expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(expense);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add expense: " + e.getMessage());
        }
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "date") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        List<Expense> expenses = expenseService.getAllExpenses(type, category, startDate, endDate, description, sortField, sortOrder);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
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

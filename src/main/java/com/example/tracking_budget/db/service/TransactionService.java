package com.example.tracking_budget.db.service;

import com.example.tracking_budget.db.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    double getTotalIncomeForUser(Long userId);

    double getTotalExpenseForUser(Long userId);

    void save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    void delete(Long id);
}

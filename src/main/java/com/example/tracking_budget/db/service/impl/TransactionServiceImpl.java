package com.example.tracking_budget.db.service.impl;

import com.example.tracking_budget.db.entity.Transaction;
import com.example.tracking_budget.db.repo.TransactionRepository;
import com.example.tracking_budget.db.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public double getTotalIncomeForUser(Long userId) {
        Double totalIncome = transactionRepository.getTotalIncomeForUser(userId);
        return totalIncome != null ? totalIncome : 0.0;
    }

    public double getTotalExpenseForUser(Long userId) {
        Double totalExpense = transactionRepository.getTotalExpenseForUser(userId);
        return totalExpense != null ? totalExpense : 0.0;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }
}

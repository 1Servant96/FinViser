package com.example.tracking_budget.db.repo;

import com.example.tracking_budget.db.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.userId = :userId AND t.type = 'INCOME'")
    Double getTotalIncomeForUser(@Param("userId") Long userId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.userId = :userId AND t.type = 'EXPENSE'")
    Double getTotalExpenseForUser(@Param("userId") Long userId);

}

package com.example.tracking_budget.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Double amount;
    private String type; // Income or Expense
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
    private String comment;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // getters, setters, etc.
}
